package com.video.vip.entity.dto.passport;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 账号+密码注册实体
 * @author wxn
 */
@Data
@ApiModel(description = "账号+密码登录实体")
public class PassportRegisterDTO implements Serializable {
    private static final long serialVersionUID = -456789140224268820L;

    @ApiModelProperty(value = "账号（邮箱或手机号）")
    private String account;

    @ApiModelProperty(value = "aes加密密码")
    private String pwdAes;

}
