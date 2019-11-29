package com.video.vip.basics.util.encryptions;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

/**
 * AES对称加密
 * @author wxn
 */
@Slf4j
public class AESUtil {
    /**
     * AES加密字符串
     * @param content
     *            需要被加密的字符串
     * @param password
     *            加密需要的密码
     * @return 密文
     */
    public static byte[] encrypt(String content, String password) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");// 创建AES的Key生产者
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(password.getBytes());
            kgen.init(128, random);// 利用用户密码作为随机数初始化出
            // 128位的key生产者
            //加密没关系，SecureRandom是生成安全随机数序列，password.getBytes()是种子，只要种子相同，序列就一样，所以解密只要有password就行
            SecretKey secretKey = kgen.generateKey();// 根据用户密码，生成一个密钥
            byte[] enCodeFormat = secretKey.getEncoded();// 返回基本编码格式的密钥，如果此密钥不支持编码，则返回
            // null。
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");// 转换为AES专用密钥
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化为加密模式的密码器
            byte[] result = cipher.doFinal(byteContent);// 加密
            return result;
        } catch (Exception e) {

            log.error("AES加密出现异常",e);
        }
        return null;
    }

    /**
     * 解密AES加密过的字符串
     * @param content
     *            AES加密过过的内容
     * @param password
     *            加密时的密码
     * @return 明文
     */
    public static byte[] decrypt(byte[] content, String password) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");// 创建AES的Key生产者
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(password.getBytes());
            kgen.init(128, random);
            SecretKey secretKey = kgen.generateKey();// 根据用户密码，生成一个密钥
            byte[] enCodeFormat = secretKey.getEncoded();// 返回基本编码格式的密钥
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");// 转换为AES专用密钥
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, key);// 初始化为解密模式的密码器
            byte[] result = cipher.doFinal(content);
            return result; // 明文
        } catch (Exception e) {
            log.error("AES解密出现异常",e);
        }
        return null;
    }

    /**
     * 加密方法
     * @param data  要加密的数据
     * @param key 加密key
     * @param iv 加密iv
     * @return 加密的结果
     * @throws Exception
     */
    public static String encrypt2(String data, String key, String iv) throws Exception {
        try {

            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");//"算法/模式/补码方式"
            int blockSize = cipher.getBlockSize();

            byte[] dataBytes = data.getBytes();
            int plaintextLength = dataBytes.length;
            if (plaintextLength % blockSize != 0) {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }

            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);

            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            byte[] encrypted = cipher.doFinal(plaintext);

            return new Base64().encodeToString(encrypted);

        } catch (Exception e) {
            log.error("aes加密2，异常",e);
            return null;
        }
    }

    /**
     * 解密方法
     * @param data 要解密的数据
     * @param key  解密key
     * @param iv 解密iv
     * @return 解密的结果
     * @throws Exception
     */
    public static String desEncrypt2(String data, String key, String iv) throws Exception {
        try {
            byte[] encrypted1 = new Base64().decode(data);
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original);
            if(StringUtils.isNotBlank(originalString)){
                originalString = originalString.trim();
            }
            return originalString;
        } catch (Exception e) {
            log.error("aes解密2，异常",e);
            return null;
        }
    }

    /**
     * 使用默认的key和iv加密
     * @param data
     * @return
     * @throws Exception
     */
    public static String encrypt(String data,String key,String iv) throws Exception {
        return encrypt2(data, key, iv);
    }

    /**
     * 使用默认的key和iv解密
     * @param data
     * @return
     * @throws Exception
     */
    public static String desEncrypt(String data,String key,String iv) throws Exception {
        return desEncrypt2(data, key, iv);
    }


    private static final String passwordAES = "AHdihSDFIkHgjL@##9_123kjsdfHS-=aFWbhf";
    public static void main(String[] args) throws Exception {
//        for (int i=0;i<200;i++){
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    log.info("--------------1-----------------");
//                    byte[] encrypt = AESUtil.encrypt("1234567890123", passwordAES);
////                    String phone = PhoneUtil.encryption("1234567890123");
////                        String token = JavaWebTokenUtil.createJWT(System.currentTimeMillis()
////                                ,"asdf","a:aaaa"
////                                ,"1234567890123",30000L);
////                        log.info("token:"+token);
////                        log.info("验证："+JavaWebTokenUtil.parseJWT(token));
//                    log.info("---------------2----------------");
//                }
//            }).start();
//        }

//        String content = "sdfSDF123_";
////        String password = "aHdihSDFIkHgjL@##9123kjsdfHS_-=aFWbhf";
//        String password = "1111";
//        System.out.println("加密之前：" + content);
//        // 加密
//        byte[] encrypt = AESUtil.encrypt(content, password);
//        System.out.println("加密后的内容：" + new String(encrypt));
//
//        //如果想要加密内容不显示乱码，可以先将密文转换为16进制
//        String hexStrResult = ParseSystemUtil.parseByte2HexStr(encrypt);
//        System.out.println("16进制的密文："  + hexStrResult);
//////
//////        //如果的到的是16进制密文，别忘了先转为2进制再解密
////        byte[] twoStrResult = ParseSystemUtil.parseHexStr2Byte(hexStrResult);
////
////        // 解密
////        byte[] decrypt = AESUtil.decrypt(twoStrResult, password);
////        System.out.println("解密后的内容：" + new String(decrypt));




        /////////////////////////////////////////////////////////
        String content = "  sdfSDF   123_  ";
        String key = "d#so6Gz5BvM^yijl";
        String iv = key;
        System.out.println("加密之前：" + content);
        String strPwd = "";
        strPwd = AESUtil.encrypt2(content,key,iv);
        System.out.println("加密之后："+strPwd);
        System.out.println("解密之后："+AESUtil.desEncrypt2(strPwd, key, iv)+"++++");
//        byte[] encrypt = AESUtil.encrypt2(content, password);
//        strPwd = new String(encrypt);
    }

}
