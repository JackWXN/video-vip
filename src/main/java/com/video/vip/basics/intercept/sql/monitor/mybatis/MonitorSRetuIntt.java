package com.video.vip.basics.intercept.sql.monitor.mybatis;

import com.alibaba.fastjson.JSONObject;
import com.video.vip.basics.util.basics.DateUtil;
import com.video.vip.basics.util.rest.WebAppTraceFilter;
import com.video.vip.basics.util.sql.SqlMonitorConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

/**
 * 数据库操作结果拦截器：查询结果拦截
 * @Intercepts
 * 定义Signature数组,因此可以拦截多个,但是只能拦截类型为：
 * Executor
 * ParameterHandler
 * StatementHandler
 * ResultSetHandler
 * @author wxn
 */
@Intercepts({ @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = { Statement.class }) })
@Component
@Slf4j
public class MonitorSRetuIntt implements Interceptor {

    /**
     * sql打印到指定文件
     */
    private static final Logger sqlintt = LoggerFactory.getLogger("sql.monitor");

    @SuppressWarnings("finally")
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        try {
            Map<String, Object> mapInfo = SqlMonitorConstant.MAP_DB_SQL_INFO.get(Thread
                    .currentThread().getName());
            // 如果没有相关的sql执行信息，则退出
            if (mapInfo != null && mapInfo.size() > 0) {
                Object[] args = invocation.getArgs();
                // 获取到当前的Statement
                Statement stmt = (Statement) args[0];
                // 通过Statement获得当前结果集
                ResultSet resultSet = stmt.getResultSet();
                int intRetuCount = 0;
                // 是否记录sql
                boolean boolIsRecord = false;
                // sql执行时间（毫秒）
                Long longExecTime = (Long) mapInfo.get("timeConsuming");
                if(resultSet==null){
                    intRetuCount = stmt.getUpdateCount();
                    boolIsRecord = true;
                }else{
                    resultSet.last();
                    // 返回的行数
                    intRetuCount = resultSet.getRow();
                    // 结果集大小
                    resultSet.absolute(0);
                }
                if (longExecTime >= SqlMonitorConstant.SLOW_SQL_MILLISECOND) {
                    // 如果是慢sql
                    boolIsRecord = true;
                } else if (intRetuCount >= SqlMonitorConstant.SELECT_SQL_RETURN_COUNT) {
                    // 如果返回的条数超限
                    boolIsRecord = true;
                }
                // else if(longObjSize>=Constants.SELECT_SQL_RETURN_OBJ_SIZE){
                // //如果结果集大小超限
                // boolIsRecord = true;
                // }
                if (boolIsRecord) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("sql",mapInfo.get("sql").toString());
                    jsonObject.put("class",mapInfo.get("class").toString());
                    jsonObject.put("execCount",intRetuCount);
                    jsonObject.put("execTime",longExecTime);
                    jsonObject.put("execType","select");
                    jsonObject.put("execDate", DateUtil.getDateTime(new Date(),"yyyy-MM-dd HH:mm:ss.SSS"));
                    jsonObject.put("strCode",StringUtils.isBlank(MDC.get(WebAppTraceFilter.HEADER_NAME))
                            ?MDC.get(WebAppTraceFilter.HEADER_NAME):mapInfo.get("strCode"));
                    jsonObject.put("sqlType","mysql");

                    // 记录sql执行情况
                    sqlintt.info("{}",jsonObject.toJSONString());
//                    sqlintt.info(mapInfo.get("sql").toString() + "!@!"
//                                    + longExecTime + "!@!" + intRetuCount
//                            // +"!@!"+longObjSize
//                    );
                }
            }
        } catch (Exception e) {
            log.error("查询结果拦截出现错误：", e);
        } finally {

        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }

}
