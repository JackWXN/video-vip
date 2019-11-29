package com.video.vip.basics.util.basics;

import org.springframework.util.StringUtils;

import java.util.List;

/**
 * StringUtlis
 * @author lixiping
 * @date 2016-7-16
 * @description：
 */
@SuppressWarnings("rawtypes")
public class StringUtil {


	public static String[] list2Array(List list) {
		String[] strs = new String[list.size()];
		for (int i = 0; i < strs.length; i++) {
			strs[i] = (String) list.get(i);
		}
		return strs;
	}
	
	public static Object[] list2ObjArray(List list){
		Object[] strs = new Object[list.size()];
		for (int i = 0; i < strs.length; i++) {
			strs[i] = list.get(i);
		}
		return strs;
	}
	
	public static String getReplaceString(String str) {
		if (!StringUtils.isEmpty(str)) {
			str = str.replaceAll("\\\\", "\\\\\\\\"); 
			str = str.replaceAll("\"", "\\\\\""); 
			str = str.replaceAll("\r", ""); 
			str = str.replaceAll("\n", ""); 
			str = str.replaceAll("\r\n", ""); 
		} else { 
			str = ""; 
		} 
		return str; 
	}
	
	public static String upperCaseFirstChar(String str){
		char firstChar = str.toUpperCase().charAt(0);
		str = firstChar + str.substring(1);
		return str;
	}
    
    public static String getNotNull(String value){
    	return value==null?"":value;
    }
	
	public static void main(String[] args) {
	}

	/**
	 * 判断是否是数字
	 *
	 * @param sourceString
	 * @return
	 */
	public static boolean isNumber(String sourceString) {
		if (StringUtils.isEmpty(sourceString)){
			return false;
		}
		char[] sourceChar = sourceString.toCharArray();
		for (int i = 0; i < sourceChar.length; i++){
			if ((sourceChar[i] < '0') || (sourceChar[i] > '9')){
				return false;
			}
		}
		return true;
	}
}
