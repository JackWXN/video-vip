package com.video.vip.basics.intercept.sql.monitor.mybatis;

import com.video.vip.basics.util.basics.Numbers;
import com.video.vip.basics.util.enums.sql.SqlTypeEnum;
import com.video.vip.basics.util.sql.SqlMonitorConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.text.DateFormat;
import java.util.*;
import java.util.regex.Matcher;

/**
 * sql拦截处理
 * @author wxn
 */
@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class , Integer.class}) })
@Component
@Slf4j
public class MonitorAndBusinessIntt implements Interceptor {

    private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
    private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();
    private static final ReflectorFactory DEFAULT_REFLECTOR_FACTORY = new DefaultReflectorFactory();
    private static final String DLI_H = "h";
    private static final char SQL_OPEN = '1';
    private static final String DLI_TARGET = "target";

    /**
     * 获取sql
     * @param configuration
     * @param boundSql
     * @param sqlId
     * @param time
     * @param editSql
     * @return
     */
    private String getSql(Configuration configuration, BoundSql boundSql, String sqlId, long time, String editSql) {
        String sql = showSql(configuration, boundSql, editSql);
        StringBuilder str = new StringBuilder(100);
        str.append(sqlId);
        str.append("-=>");
        str.append(sql);
        return str.toString();
    }

    /**
     * 格式化获取参数值
     * @param obj
     * 传入的参数值
     * @return
     */
    private String getParameterValue(Object obj) {
        String value = null;
        if (obj instanceof String) {
            value = "'" + obj.toString() + "'";
        } else if (obj instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            value = "'" + formatter.format(new Date()) + "'";
        } else {
            if (obj != null) {
                value = obj.toString();
            } else {
                value = "";
            }

        }
        return Matcher.quoteReplacement(value);
    }

    /**
     * 生成显示的sql
     * @param configuration
     * @param boundSql 原始sql
     * @param editSql
     * @return
     */
    private String showSql(Configuration configuration, BoundSql boundSql, String editSql) {
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        String sql = editSql.replaceAll("[\\s]+", " ");
        sql = setParam(sql);
        if (parameterMappings.size() > 0 && parameterObject != null) {
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                sql = sql.replaceFirst(SqlMonitorConstant.WH_KEYWORD, getParameterValue(parameterObject));
            } else {
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                for (ParameterMapping parameterMapping : parameterMappings) {
                    String propertyName = parameterMapping.getProperty();
                    if (metaObject.hasGetter(propertyName)) {
                        Object obj = metaObject.getValue(propertyName);
                        sql = sql.replaceFirst(SqlMonitorConstant.WH_KEYWORD, getParameterValue(obj));
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        Object obj = boundSql.getAdditionalParameter(propertyName);
                        sql = sql.replaceFirst(SqlMonitorConstant.WH_KEYWORD, getParameterValue(obj));
                    }
                }
            }
        }
        return sql;
    }


    /**
     * 替换?为问号关键字
     * @param sql 执行的sql
     */
    public static String setParam(String sql) {
        StringBuffer gsql = new StringBuffer();
        char charKey = ' ';
        boolean isNext = false;
        for (int i = 0; i < sql.length(); i++) {
            char charSql = sql.charAt(i);
            //如果这一次应该直接生效
            if (isNext) {
                isNext = false;
            } else if (charSql == '\\') {
                //如果是转义符则直接跳过下一个
                isNext = true;
            } else if (charSql == '\'' && charKey == ' ') {
                //如果是'并且是开始
                charKey = '\'';
            } else if (charSql == '\'' && charKey == '\'') {
                //如果是'并且是结束
                charKey = ' ';
            } else if (charSql == '"' && charKey == ' ') {
                //如果是"并且是开始
                charKey = '"';
            } else if (charSql == '"' && charKey == '"') {
                //如果是'并且是结束
                charKey = ' ';
            }
            if (charSql == '?' && charKey == ' ') {
                gsql.append(SqlMonitorConstant.WH_KEYWORD);
            } else {
                gsql.append(charSql);
            }

        }
        return gsql.toString();
    }

    /**
     * 判断sql是增删改查
     * @param sql
     * @return 0：其他，1：新增，2：删除，3：修改，4：查询
     */
    public int isCRUD(String sql){
        int intCRUD = 0;
        if(StringUtils.isNotBlank(sql)){
            sql = sql.replaceAll("\\s"," ").toUpperCase();
            //去掉符号
            sql = sql.replaceAll("\\pP"," ");
            String[] strs = sql.split(" ");
            for(String str : strs){
                if("INSERT".equals(str)){
                    intCRUD = 1;
                    break;
                }else if("DELETE".equals(str)){
                    intCRUD = 2;
                    break;
                }else if("UPDATE".equals(str)){
                    intCRUD = 3;
                    break;
                }else if("SELECT".equals(str)){
                    intCRUD = 4;
                    break;
                }
            }
        }
        return intCRUD;
    }

    /**
     * sql处理
     * @param invocation
     * @return
     * @throws Throwable
     */
    @SuppressWarnings("unused")
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaStatementHandler = MetaObject.forObject(statementHandler, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY,DEFAULT_REFLECTOR_FACTORY);
        // 分离代理对象链
        while (metaStatementHandler.hasGetter(DLI_H)) {
            Object object = metaStatementHandler.getValue(DLI_H);
            metaStatementHandler = MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY,DEFAULT_REFLECTOR_FACTORY);
        }
        // 分离最后一个代理对象的目标类
        while (metaStatementHandler.hasGetter(DLI_TARGET)) {
            Object object = metaStatementHandler.getValue(DLI_TARGET);
            metaStatementHandler = MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY,DEFAULT_REFLECTOR_FACTORY);
        }
        String updateVersionAddSql = "";
        Object objParams = metaStatementHandler.getValue("delegate.boundSql.parameterObject");
        String originalSql = (String) metaStatementHandler.getValue("delegate.boundSql.sql");
        //判断增删改查类型
        int isCRUD = isCRUD(originalSql);
        //如果是查询，则加上limit
        if (isCRUD== SqlTypeEnum.SELECT.getCode()) {
            originalSql = "SELECT T_L_S_.* FROM ("+originalSql+") T_L_S_ LIMIT "+ SqlMonitorConstant.SELECT_SQL_DEFAULT_LIMIT+" ";
        }
        Configuration configuration = (Configuration) metaStatementHandler.getValue("delegate.configuration");

        MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");
        String sqlId = mappedStatement.getId();
        BoundSql boundSql = mappedStatement.getBoundSql(objParams);
        Configuration configuration2 = mappedStatement.getConfiguration();
        Object returnValue = null;
        long start = System.currentTimeMillis();
        String sql = getSql(configuration2, boundSql, sqlId, 0, originalSql);
        String strCode = Numbers.logUuid();
        boolean boolPrint = false;
        // 如果是新增开启记录
        if (SqlMonitorConstant.SQL_MONITOR.charAt(0) == SQL_OPEN && !boolPrint) {
            if (isCRUD == SqlTypeEnum.INSERT.getCode()) {
                boolPrint = true;
            }
        }
        // 如果是删除开启记录
        if (SqlMonitorConstant.SQL_MONITOR.charAt(1) == SQL_OPEN && !boolPrint) {
            if (isCRUD == SqlTypeEnum.DELETE.getCode()) {
                boolPrint = true;
            }
        }
        // 如果是删除开启记录
        if (SqlMonitorConstant.SQL_MONITOR.charAt(2) == SQL_OPEN && !boolPrint) {
            if (isCRUD == SqlTypeEnum.UPDATE.getCode()) {
                boolPrint = true;
            }
        }
        // 如果是删除开启记录
        if (SqlMonitorConstant.SQL_MONITOR.charAt(3) == SQL_OPEN && !boolPrint) {
            if (isCRUD == SqlTypeEnum.SELECT.getCode()) {
                boolPrint = true;
            }
        }
        if (isCRUD != SqlTypeEnum.INSERT.getCode()
                && isCRUD != SqlTypeEnum.DELETE.getCode()
                && isCRUD != SqlTypeEnum.UPDATE.getCode()
                && isCRUD != SqlTypeEnum.SELECT.getCode()) {
            boolPrint = true;
        }

        if (boolPrint){
            log.info(strCode + ":" + sql);
        }
        long startSql = System.currentTimeMillis();
        returnValue = invocation.proceed();
        long end = System.currentTimeMillis();
        //sql执行时间
        long timeConsuming = (end - startSql);
        //传递的参数
        Map<String,Object> mapInfo = new HashMap<String,Object>();
        mapInfo.put("sql", sql.substring(sql.indexOf("-=>")+3));
        mapInfo.put("class", sql.split("-=>")[0]);
        mapInfo.put("strCode", strCode);
        mapInfo.put("boolPrint", boolPrint);
        //sql执行时间
        mapInfo.put("timeConsuming", timeConsuming);
        SqlMonitorConstant.MAP_DB_SQL_INFO.put(Thread.currentThread().getName(), mapInfo);
        //如果开启了打印sql并且是查询语句则打印执行时间。增删改的执行时间在SqlInterceptorURetu里展示
        if (boolPrint&&isCRUD == SqlTypeEnum.SELECT.getCode()){
            log.info("{}:执行时间{}毫秒",strCode,timeConsuming);
        }
        return returnValue;
    }

    @Override
    public Object plugin(Object target) {
        // 当目标类是StatementHandler类型时，才包装目标类，否者直接返回目标本身,减少目标被代理的
        // 次数
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    @Override
    public void setProperties(Properties properties) {
    }

}
