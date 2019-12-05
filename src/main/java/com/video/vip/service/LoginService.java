package com.video.vip.service;

import com.video.vip.basics.dto.Result;
import com.video.vip.basics.util.enums.YesOrNoEnum;
import com.video.vip.entity.dto.passport.PassportDTO;
import com.video.vip.util.enums.passport.PassportOperationTypeEnum;

import java.util.Map;

/**
 * 帐号登录相关操作
 *
 * @author wxn
 */
public interface LoginService {

    /**
     * 验证token是否有效
     * @param token 用户token令牌数据
     * @return 如果不是null则有效，如果是null则失效
     */
    String validateToken(String token);

    /**
     * 登出
     * @param token
     * @return
     */
    Result logoutByToken(String token);

    /**
     * 注册
     * @param passportOperationTypeEnum PassportOperationTypeEnum枚举
     * @param account 账号
     * @param pwdAes 密码
     * @param tokenExpiresMs token过期时间
     * @return
     */
    Result register(PassportOperationTypeEnum passportOperationTypeEnum, String account, String pwdAes, Long tokenExpiresMs);

    /**
     * 登录
     * @param passportOperationTypeEnum PassportOperationTypeEnum枚举
     * @param account 账号
     * @param pwdAes 登录的密码（aes加密后的密码），key:PassportConstantUtil.PWD_AES_KEY
     * @param tokenExpiresMs token的过期时间，单位毫秒
     * @return 返回账号实体（包含token）
     */
    Result<PassportDTO> login(PassportOperationTypeEnum passportOperationTypeEnum, String account, String pwdAes, Long tokenExpiresMs);

    /**
     * 根据指定类型修改密码(目前支持pid、phone和mail方式)
     * @param passportOperationTypeEnum PassportOperationTypeEnum枚举
     * @param account 账号，pid|phone|mail
     * @param newPasswordAes 要修改的密码（aes加密后的密码），key:PassportConstantUtil.PWD_AES_KEY
     * @param oldPasswordAes 老密码（aes加密后的密码），如果传递了老密码则校验老密码是否正确，key:PassportConstantUtil.PWD_AES_KEY
     * @param isReadyPwd 是否进行预处理密码，如果是是，则不直接修改主密码
     * @param params 传递的参数(如果修改类型是手机号，则必须传递手机验证码key：pvcode)
     * @return
     */
    Result updateOldPassword(String logStr, PassportOperationTypeEnum passportOperationTypeEnum, String account, String newPasswordAes, String oldPasswordAes, YesOrNoEnum isReadyPwd, Map<String,Object> params);
}
