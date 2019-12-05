package com.video.vip.util.token;

import com.video.vip.basics.dto.Result;
import com.video.vip.basics.util.encryptions.AESUtil;
import com.video.vip.basics.util.encryptions.Md5Util;
import com.video.vip.basics.util.enums.ResultEnum;
import com.video.vip.basics.util.pwd.CheckStrengthUtil;
import com.video.vip.basics.util.pwd.PassportConstantUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;


/**
 * 密码相关操作工具类
 * @author wxn
 */
@Slf4j
public class PasswordUtil {
    /**
     * 对aes密码解密后加密
     * @param pwdAes 前端aes加密后的密码
     * @param pwdSalt 密码盐
     * @param isComplexity 是否进行密码规范校验
     * @return 返回data密码
     */
    public static Result<String> encryption(String pwdAes, String pwdSalt, boolean isComplexity){
        log.debug("对aes密码解密后加密开始：pwdAes:{},pwdSalt:{},isComplexity:{}",pwdAes,pwdSalt,isComplexity);
        Result<String> result = Result.newSuccess();
        if(StringUtils.isEmpty(pwdSalt)){
            pwdSalt = "";
        }
        result.setData(PasswordUtil.aesDecrypt(pwdAes, PassportConstantUtil.PWD_AES_KEY));
        if(StringUtils.isEmpty(result.getData())){
            log.warn("aes密码解密返回值为空，非法请求：pwdAes:{},pwdSalt:{},isComplexity:{}",pwdAes,pwdSalt,isComplexity);
            result = Result.newResult(ResultEnum.UNLAWFUL_REQUEST,"");
        }else{
            log.debug("开始进行密码规范校验:pwdAes:{},pwdSalt:{},isComplexity:{}",pwdAes,pwdSalt,isComplexity);
            if(isComplexity){
                if(result.getData().length()<PassportConstantUtil.PWD_MIN_LENGTH){
                    log.warn("密码长度不能小于{}位",PassportConstantUtil.PWD_MIN_LENGTH);
                    result = Result.newResult(ResultEnum.FAIL,"密码长度不能小于"+PassportConstantUtil.PWD_MIN_LENGTH+"位");
                }else if(result.getData().length()>PassportConstantUtil.PWD_MAX_LENGTH){
                    log.warn("密码长度不能大于{}位",PassportConstantUtil.PWD_MAX_LENGTH);
                    result = Result.newResult(ResultEnum.FAIL,"密码长度不能大于"+PassportConstantUtil.PWD_MAX_LENGTH+"位");
                }else{
                    int intComplexity = CheckStrengthUtil.checkPasswordStrength(result.getData());
                    log.debug("密码复杂度级别：intComplexity:{}",intComplexity);
                    if(intComplexity<=PassportConstantUtil.PWD_LEAVE){
                        log.warn("密码复杂度小于{}，不能通过：intComplexity:{}",PassportConstantUtil.PWD_LEAVE,intComplexity);
                        result = Result.newResult(ResultEnum.FAIL,"请输入复杂密码，至少由大小写字母（至少4个字母）+数字组成");
                    }
                }
            }
            if(result.isSuccess()){
                String md5Pwd = Md5Util.getEncryInfo(result.getData() + pwdSalt);
                result.setData(md5Pwd);
            }
        }
        log.debug("对aes密码解密后加密结束：result:{}",result.toJSONString());
        return result;
    }

    /**
     * 生成密码盐
     * @return
     */
    public static String generatePwdSalt(){
        return CheckStrengthUtil.genCodes(6, 1).get(0);
    }

    /**
     * aes加密
     * @param content 要加密的内容
     * @param key 加密key
     * @return
     */
    public static String aesEncrypt(@NonNull String content, @NonNull String key){
        String strBase64Result = null;
        try {
            strBase64Result = AESUtil.encrypt2(content, key, PassportConstantUtil.PWD_AES_IV);
        } catch (Exception e) {
            log.error("aes加密失败：",e);
        }
        return strBase64Result;
    }

    /**
     * aes解密
     * @param content 要解密的内容
     * @param key 解密key
     * @return
     */
    public static String aesDecrypt(@NonNull String content, @NonNull String key){
        // 解密
        String base64StrResult = null;
        try {
            base64StrResult = AESUtil.desEncrypt2(content, key, PassportConstantUtil.PWD_AES_IV);
        } catch (Exception e) {
            log.error("aes解密失败：",e);
        }
        return base64StrResult;
    }
}
