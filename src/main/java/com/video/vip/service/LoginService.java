package com.video.vip.service;

import com.video.vip.basics.dto.Result;
import com.video.vip.entity.dto.passport.PassportDTO;
import com.video.vip.util.enums.passport.PassportOperationTypeEnum;

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
     * 根据密码登录
     * @param passportOperationTypeEnum PassportOperationTypeEnum枚举
     * @param account 账号
     * @param pwdAes 登录的密码（aes加密后的密码），key:PassportConstantUtil.PWD_AES_KEY
     * @param tokenExpiresMs token的过期时间，单位毫秒
     * @return 返回账号实体（包含token）
     */
    Result<PassportDTO> login(PassportOperationTypeEnum passportOperationTypeEnum, String account, String pwdAes, Long tokenExpiresMs);
}
