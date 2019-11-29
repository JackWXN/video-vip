package com.video.vip.basics.intercept.sql.monitor.mybatis;

import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.Properties;

/**
 * 加载sql监控配置文件
 * @author wxn
 */
@Slf4j
public class PropertiesMonitorMybatisUtil {
	private static final String FILE_PATH = "sqlMM.properties";
	private static final Properties PROP = new Properties();
	public static void init(){
		InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(FILE_PATH);
		try {
			if(inputStream==null){
				log.warn("classpatch下没有找到sql监控相关配置文件sqlMM.properties");
			}else{
				PROP.load(inputStream);
				log.info("加载sql监控相关配置完成");
			}
		} catch (Exception e) {
			log.warn("加载sql监控相关配置异常，使用默认配置: {},{}",e.getClass(),e.getMessage(),e);
		}
	}
	public static String getValue(String key){
		return PROP.getProperty(key);
	}
}
