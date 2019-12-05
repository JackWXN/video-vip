package com.video.vip.entity.dto.passport;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 根据老密码修改新密码
 * @author wxn
 */
@Data
@ApiModel(description = "根据老密码修改新密码")
public class PasswordUpdateDTO implements Serializable {
    private static final long serialVersionUID = 6687173224086292650L;

    @ApiModelProperty(value = "老密码aes加密后的密码")
    private String oldPasswordAes;

    @ApiModelProperty(value = "新密码aes加密后的密码")
    private String newPasswordAes;
}
