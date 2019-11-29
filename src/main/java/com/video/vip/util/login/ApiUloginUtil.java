package com.video.vip.util.login;

import com.video.vip.util.enums.PassportOperationTypeEnum;
import lombok.NonNull;

/**
 * api通用登录工具类
 * @author wxn
 */
public class ApiUloginUtil {
    /**
     * 邮件注册rediskey
     */
    public static final String MAIL_REGISTER_REDIS_KEY = "mrrk18827";
    /**
     * 验证邮件有效期（单位：秒）
     */
    public static final Integer VALI_MAIL_OVERTIME_SECOND = 900;
    /**
     * tolen的默认有效期（单位：毫秒）
     */
    public static final Long TOKEN_OVERTIME_MS = 7*24*60*60*1000L;

    /**
     * 区分账号类型是手机号还是邮箱
     * @param passport 传递过来的账号
     * @return
     */
    public static PassportOperationTypeEnum chackPassportType(@NonNull String passport){
        PassportOperationTypeEnum passportOperationTypeEnum = null;
        if(passport.indexOf("@")>-1){
            passportOperationTypeEnum = PassportOperationTypeEnum.MAIL;
        }else{
            passportOperationTypeEnum = PassportOperationTypeEnum.PHONE;
        }
        return passportOperationTypeEnum;
    }
}
