package com.video.vip.basics.util.encryptions;

import java.security.MessageDigest;


/**
 * MD5 加密
 * @author Administrator
 *
 */
public class Md5Util {
	
	private static final String MD5_CONS = "9fbank";
	
	/**
	 * 获取加密信息
	 * @param plainText
	 * @return
	 */
	public static String getEncryInfo( String plainText ) { 
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' }; 
		try { 
			byte[] strTemp = (plainText + MD5_CONS).getBytes();   
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");   
			mdTemp.update(strTemp);   
			byte[] md = mdTemp.digest();   
			int j = md.length;   
			char str[] = new char[j * 2];   
			int k = 0;   
			for (int i = 0; i < j; i++) {   
			    byte byte0 = md[i];   
			    str[k++] = hexDigits[byte0 >>> 4 & 0xf];   
			    str[k++] = hexDigits[byte0 & 0xf];   
			}
			return new String(str); 
		} catch (Exception e) { 
			e.printStackTrace(); 
		} 
		return "";
	} 
	
	/**
	 * 验证加密信息是否与原信息相同
	 * @param resource
	 * @param encryStr
	 * @return
	 */
	public static Boolean isEqueal(String resource , String encryStr){
		String encryResult = getEncryInfo(resource);
		return encryResult.equals(encryStr);
	}
	
	public static void main(String[] args) {
		System.out.println(getEncryInfo("sysadmin"));
	}
}
