package com.video.vip.basics.intercept.sql.monitor.mybatis;

import com.video.vip.basics.util.sql.SqlMonitorConstant;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.CallableStatement;
import java.util.Properties;

/**
 * 数据库操作结果拦截器：存储过程拦截
 * @Intercepts
 * 定义Signature数组,因此可以拦截多个,但是只能拦截类型为：
 * Executor
 * ParameterHandler
 * StatementHandler
 * ResultSetHandler
 * @author wxn
 */
@Intercepts({ @Signature(type = ResultSetHandler.class, method = "handleOutputParameters", args = { CallableStatement.class }) })
@Component
public class MonitorCRetuIntt implements Interceptor {
    private static final Logger logger = LoggerFactory.getLogger(MonitorCRetuIntt.class);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        SqlMonitorConstant.MAP_DB_SQL_INFO.remove(Thread.currentThread().getName());
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
