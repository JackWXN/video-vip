package com.video.vip.basics.util.properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * 加载slq监控相关配置文件
 */
public class PropertiesSqlMUtil {
	private static final Logger logger = LoggerFactory.getLogger(PropertiesSqlMUtil.class);
	public static final Properties prop = new Properties();
	
	public static String getValue(String key){
		return prop.getProperty(key);
	}

}
