package com.video.vip.entity.dto.passport;

import lombok.Data;

import java.io.Serializable;

/**
 * 重置当天尝试修改密码总次数
 * @author wxn
 */
@Data
public class PassportPwdResetDTO implements Serializable {
    private static final long serialVersionUID = -6392873994551146533L;
    /**
     * 要重置的账号（邮箱或手机号）
     */
    private String account;
}
