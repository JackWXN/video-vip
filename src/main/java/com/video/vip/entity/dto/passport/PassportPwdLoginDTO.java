package com.video.vip.entity.dto.passport;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 账号+密码登录实体
 * @author wxn
 */
@Data
@ApiModel(description = "账号+密码登录实体")
public class PassportPwdLoginDTO implements Serializable {
    private static final long serialVersionUID = -456789140224268820L;

    @ApiModelProperty(value = "账号（邮箱或手机号）")
    private String account;

    @ApiModelProperty(value = "图片验证码")
    private String imgVcode;

    @ApiModelProperty(value = "aes加密密码")
    private String pwdAes;

    @ApiModelProperty(value = "前端key,图片验证码的时传的唯一key")
    private String randomKey;



    @ApiModelProperty(value = "用户来源 枚举 UserSourceEnum 0:自己注册 1:好友分享")
    private Integer source;

    @ApiModelProperty(value = "该用户介绍人pid")
    private String referrerPid;

    @ApiModelProperty(value = "用户所属平台。枚举 UserPlatformEnum 0:C端普通用户 1:B端运营人员")
    private Integer userPlatform;
}
