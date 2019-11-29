package com.video.vip.basics.util.properties;

import com.video.vip.basics.util.CoreAchieveConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 获取service.properties配置文件的值
 * @author wxn
 */
@Slf4j
public class ProperServicesUtils {

	public static String getServicePro(String key){
		String value=null;
		if(StringUtils.isNotBlank(key)){
			value= CoreAchieveConstants.MAP_PROPERTY_SERVICE.get(key);
		}
		if(value==null){
			Properties p = new Properties();
			InputStream inStream = null;
			try {
				inStream = ProperServicesUtils.class.getClassLoader().getResourceAsStream("service.properties");
				p.load(inStream);
				value = p.getProperty(key);
				if(value==null){
					value="";
				}
				CoreAchieveConstants.MAP_PROPERTY_SERVICE.put(key,value);
			} catch (IOException ee) {
				log.error("读取属性文件异常：key:{},value:{}",key,value,ee);
			}finally {
				if(inStream!=null){
					try {
						inStream.close();
					} catch (IOException e) {
						log.error("关闭流文件异常：key:{},value:{}",key,value,e);
					}
				}
			}
		}
        return value;
	}

	public static void main(String[] args){
		String s = getServicePro("log.dir");
		System.out.println(s);
	}
}
