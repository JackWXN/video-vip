package com.video.vip.basics.intercept.sql.monitor.mybatis;

import com.alibaba.fastjson.JSONObject;
import com.video.vip.basics.util.basics.DateUtil;
import com.video.vip.basics.util.rest.WebAppTraceFilter;
import com.video.vip.basics.util.sql.SqlMonitorConstant;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.sql.Statement;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

/**
 * 数据库操作结果拦截器：新增或修改结果拦截
 *
 * @Intercepts 定义Signature数组,因此可以拦截多个,但是只能拦截类型为： Executor ParameterHandler
 *             StatementHandler ResultSetHandler
 * @author wxn
 */
@Intercepts({ @Signature(type = StatementHandler.class, method = "update", args = { Statement.class }) })
@Component
public class MonitorURetuIntt implements Interceptor {

    private static final Logger logger = LoggerFactory.getLogger(MonitorURetuIntt.class);

    /**
     * sql打印到指定文件
     */
    private static final Logger sqlintt = LoggerFactory.getLogger("sql.monitor");


    @SuppressWarnings("finally")
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object obj = null;
        int intRetu = -1;
        try {
            long startSql = System.currentTimeMillis();
            // 执行sql
            obj = invocation.proceed();
            long end = System.currentTimeMillis();
            // sql执行时间
            long timeConsuming = (end - startSql);

            Map<String, Object> mapInfo = SqlMonitorConstant.MAP_DB_SQL_INFO.get(Thread
                    .currentThread().getName());
            if (obj != null) {
                intRetu = Integer.parseInt(obj.toString());
            }
            //是否打印执行时间
            if(mapInfo.get("strCode")!=null&&mapInfo.get("boolPrint")!=null){
                //如果开启了打印
                if((Boolean)(mapInfo.get("boolPrint"))){
                    logger.info(mapInfo.get("strCode") + ":执行时间 " + timeConsuming);
                }
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("sql",mapInfo.get("sql").toString());
            jsonObject.put("class",mapInfo.get("class").toString());
            jsonObject.put("execCount",intRetu);
            jsonObject.put("execTime",timeConsuming);
            jsonObject.put("execType","update");
            jsonObject.put("execDate", DateUtil.getDateTime(new Date(),"yyyy-MM-dd HH:mm:ss.SSS"));
            jsonObject.put("strCode",StringUtils.isBlank(MDC.get(WebAppTraceFilter.HEADER_NAME))
                    ?MDC.get(WebAppTraceFilter.HEADER_NAME):mapInfo.get("strCode"));
            jsonObject.put("sqlType","mysql");
            // 记录sql执行情况
            sqlintt.info("{}",jsonObject.toJSONString());
//            sqlintt.info(mapInfo.get("sql").toString() + "!@!" + timeConsuming
//                            + "!@!" + intRetu
//                    // +"!@!"+longObjSize
//            );
        } catch (Exception e) {
            logger.error("新增或修改结果拦截出现错误：" , e);
        } finally {
            SqlMonitorConstant.MAP_DB_SQL_INFO.remove(Thread.currentThread().getName());
        }
        return obj;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }
}
