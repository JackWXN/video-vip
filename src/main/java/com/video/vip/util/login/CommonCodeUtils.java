package com.video.vip.util.login;

import lombok.extern.slf4j.Slf4j;

/**
 * 通用工具-图片验证码工具类
 * @author wxn
 */
@Slf4j
public class CommonCodeUtils {
    /**
     * 用户图片验证码redis key
     */
    public static final String REDIS_IMG_VCODE_KEY = "img_code_";
    /**
     * 用户图片验证码redis超时时间（秒）
     */
    public static final Long REDIS_IMG_VCODE_KEY_OVERTIME = 60L;
    /**
     * 用户短信验证码非首次redis校验图片验证码KEY
     */
    public static final  String SMS_REDIS_NOT_FIRST_KEY = "lbavcrs_";

}
