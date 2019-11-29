package com.video.vip.basics.util.encryptions;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;

/**
 * SHA加密工具类
 * @author wxn
 */
@Slf4j
public class SHAUtil {
    public static void main(String[] args) throws Exception {
//        String strPass = "asdfjklalsdjflas!@#$%^&*()_+_{}:\">?<1234567890-=[];'./,<jdflasldfjlasdfkasdflaskf索拉卡大姐夫拉萨京东方垃圾斯蒂芬 阿拉山口剪短发拉时代峻峰asdf asdf ";
        String strPass = "cesh测试";
        System.out.println(SHAUtil.getSHA512Str(strPass));
    }
    /***
     *  利用Apache的工具类实现SHA-256加密
     * @param str 加密后的报文
     * @return
     */
    public static String getSHA256Str(String str){
        MessageDigest messageDigest;
        String encdeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash = messageDigest.digest(str.getBytes("UTF-8"));
            encdeStr = Hex.encodeHexString(hash);
        } catch (Exception e) {
            log.error("SHA256加密异常",e);
        }
        return encdeStr;
    }

    /***
     *  利用Apache的工具类实现SHA-512加密
     * @param str 加密后的报文
     * @return
     */
    public static String getSHA512Str(String str){
        MessageDigest messageDigest;
        String encdeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-512");
            byte[] hash = messageDigest.digest(str.getBytes("UTF-8"));
            encdeStr = Hex.encodeHexString(hash);
        } catch (Exception e) {
            log.error("SHA512加密异常",e);
        }
        return encdeStr;
    }
}
