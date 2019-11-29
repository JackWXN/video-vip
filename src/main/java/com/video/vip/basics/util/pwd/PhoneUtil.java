package com.video.vip.basics.util.pwd;

import com.video.vip.basics.util.basics.ParseSystemUtil;
import com.video.vip.basics.util.encryptions.AESUtil;
import com.video.vip.basics.util.properties.ProperServicesUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * 手机号相关操作
 * @author wxn
 */
@Slf4j
public class PhoneUtil {

    /**
     * 跳过验证的手机号，多个用,隔开
     */
    public static final String NVALI_PHONE = ProperServicesUtils.getServicePro("nvali.phone");
    /**
     * 跳过验证的手机号密码，多个公用一个
     */
    public static final String NVALI_PVALI = ProperServicesUtils.getServicePro("nvali.pvali");
    /**
     * 万能验证码（只能在开发和测试使用,生产不允许配置），如果不配置默认为不使用
     */
    public static final String NVALI_PHONE_ALL = ProperServicesUtils.getServicePro("nvali.phone.all");


    private static final int[] ORDER = {3,1,4,9,11,8,6,5,10,7,2};
    private static final String PASSWORD_AES = "AHdihSDFIkHgjL@##9_123kjsdfHS-=aFWbhf";
    /**
     * 手机号加密
     * @param phone 手机号
     * @return 返回加密后的手机号
     */
    public static String encryption(@NonNull String phone){
        log.debug("开始手机号加密phone:{}",phone);
        phone = transposition(phone,true);
        log.debug("打乱顺序后的手机号进行aes加密phone:{}",phone);
        //加密
        try{
            log.debug("将密文转换成16进制");
            byte[] encrypt = AESUtil.encrypt(phone, PASSWORD_AES);
            //将密文转换为16进制
            phone = ParseSystemUtil.parseByte2HexStr(encrypt);
        }catch (Exception e){
            log.error("手机号加密出现异常：{}",e.getMessage(),e);
        }
        log.debug("加密后的手机号{}",phone);
        return phone;
    }

    /**
     * 手机号解密
     * @param phone 手机号
     * @return 返回解密后的手机号
     */
    public static String decrypt(@NonNull String phone){
        log.debug("打乱顺序后的手机号进行aes解密phone:{}",phone);
        //16进制密文，先转为2进制再解密
        byte[] twoStrResult = ParseSystemUtil.parseHexStr2Byte(phone);
        // 解密
        byte[] decrypt = AESUtil.decrypt(twoStrResult, PASSWORD_AES);
        phone = transposition(new String(decrypt),false);
        log.debug("解密后的手机号{}",phone);
        return phone;
    }

    /**
     * 手机号变换位置
     * @param phone 手机号
     * @param isEncryption 是否是加密，如果是加密是正序否则为反序
     * @return
     */
    public static String transposition(@NonNull String phone, boolean isEncryption){
        log.debug("手机号重新排列phone:{},isEncryption:{}",phone,isEncryption);
        if(phone.length()>= PassportConstantUtil.TOKEN_PHONE_LENGTH){
            int[] tPhone = new int[phone.length()];
            log.debug("手机号大于等于11位，继续执行");
            //如果是打乱排序
            if(isEncryption){
                log.debug("打乱排序");
                for(int i=0;i<phone.length();i++){
                    if(i<11){
                        for(int j=0;j<ORDER.length;j++){
                            //将手机号与指定排序位置调换
                            if(i==ORDER[j]-1){
                                tPhone[j] = Integer.parseInt(phone.charAt(i)+"");
                                break;
                            }
                        }
                    }else{
                        tPhone[i] = Integer.parseInt(phone.charAt(i)+"");
                    }
                }
            //如果是回归排序
            }else{
                log.debug("回归排序");
                for(int i=0;i<phone.length();i++){
                    if(i<11){
                        for(int j=0;j<ORDER.length;j++){
                            //将手机号与指定排序位置调换
                            if(i==ORDER[j]-1){
                                tPhone[i] = Integer.parseInt(phone.charAt(j)+"");
                                break;
                            }
                        }
                    }else{
                        tPhone[i] = Integer.parseInt(phone.charAt(i)+"");
                    }
                }
            }
            StringBuffer strbPhone = new StringBuffer("");
            //将手机号数组转换成手机号
            for (int intPhone : tPhone){
                strbPhone.append(intPhone);
            }
            phone = strbPhone.toString();
        }else{
            log.warn("手机号小于11位");
        }
        return phone;
    }

    public static void main(String[] args) {
        String phone = "1234567890123";
        String encryption = PhoneUtil.encryption(phone);
        String decrypt = PhoneUtil.decrypt(encryption);
    }
}
