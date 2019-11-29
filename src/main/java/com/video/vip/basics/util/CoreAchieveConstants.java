package com.video.vip.basics.util;


import com.video.vip.basics.util.basics.IPUtil;
import com.video.vip.basics.util.basics.Numbers;
import org.springframework.core.annotation.Order;

import java.util.HashMap;
import java.util.Map;

/**
 * 核心实现常量
 * @author wxn
 */
@Order(10)
public class CoreAchieveConstants {
    /**
     * 核心实现包是否初始化完成
     */
    public static boolean CORE_ACHIEVE_INIT = false;
    /**
     * service.properties文件值存储
     */
    public static Map<String,String> MAP_PROPERTY_SERVICE = new HashMap<String,String>();
    /**
     * 本次项目启动的唯一标识
     */
    public final static String SERVER_START_CODE = "ssc-"+ Numbers.uuid();
    /**
     * 获取本机ip
     */
    public final static String LOCAL_IP = IPUtil.getLocalIP();
    /**
     * 当前的服务器唯一标识key
     */
    public final static String LOCK_SERVER_KEY = "_SLSRKey";

}
