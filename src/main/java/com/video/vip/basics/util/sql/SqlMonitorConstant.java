package com.video.vip.basics.util.sql;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * sql监控常量类
 */
public class SqlMonitorConstant {

    /**
     * 问号替换关键字
     */
    public final static String WH_KEYWORD = "<lkz9nzqhS1V7q>";

    /**
     * 线程传递信息集合
     */
    public static Map<String,Map<String,Object>> MAP_DB_SQL_INFO = new ConcurrentHashMap<String,Map<String,Object>>();

    /**
     * 加载的sql监控配置文件名称
     */
    public final static String SQL_MONITOR_CONFIG_NAME = "config.properties";

    /**
     * 是否开启sql打印，四位数字，分别对应增删改查。1为开启0为不开启。默认开启增删改打印
     */
    public static String SQL_MONITOR = "1111";


    /**
     * 超过多少秒算慢sql（毫秒）
     */
    public static int SLOW_SQL_MILLISECOND = 1000;

    /**
     * 查询的返回结果超过多少条进行记录
     */
    public static int SELECT_SQL_RETURN_COUNT = 1000;

    /**
     * 返回的对象大小超过多少进行记录(字节)
     */
    public static int SELECT_SQL_RETURN_OBJ_SIZE = 10240;

    /**
     * 默认返回条数
     */
    public static int SELECT_SQL_DEFAULT_LIMIT = 1001;
}
