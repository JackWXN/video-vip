package com.video.vip.basics.util.pwd;

/**
 * 帐号管理常量类
 * @author wxn
 */
public class PassportConstantUtil {
    /**
     * token中登陆时间索引名称
     */
    public static final String TOKEN_LOGIN_DATE_INDEX = "login_date";

    /**
     * token中手机号长度
     */
    public static final int TOKEN_PHONE_LENGTH = 11;


    /**
     * 密码复杂度保存通过级别
     */


    public static final int PWD_LEAVE = 5;



    /**
     * 用户注册时每个人控制并发访问的锁定时间（秒）
     */
    public static final int LOCK_REDIS_REGISTER_SECOND = 8;

    /**
     * 当天尝试修改密码最大次数
     */
    public static final short EDIT_TODAY_PWD_MAX_COUNT = 5;

    /**
     * 当天尝试修改密码次数重置最大次数
     */
    public static final short EDIT_PWD_MAX_COUNT_RESET_MAX_COUNT = 3;

    /**
     * 密码aes加密 key
     */
    public static final String PWD_AES_KEY = "d#so6Gz5BvM^yijl";
    /**
     * 密码aes加密IV
     */
    public static final String PWD_AES_IV = PWD_AES_KEY;
    /**
     * 注册锁key
     */
    public static final String REGISTER_LOCK_KEY = "pp-RgC9sGRf9Rjfs0FCRHw-";
    /**
     * 密码最小长度
     */
    public static final int PWD_MIN_LENGTH = 8;
    /**
     * 密码最大长度
     */
    public static final int PWD_MAX_LENGTH = 32;


}
