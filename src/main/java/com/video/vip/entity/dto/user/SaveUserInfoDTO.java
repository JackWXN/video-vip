package com.video.vip.entity.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by wxn on 2020/1/14
 */
@Data
@ApiModel(description = "新增用户信息实体")
public class SaveUserInfoDTO implements Serializable {
    private static final long serialVersionUID = 3725916604425611976L;

    @ApiModelProperty(value = "用户姓名")
    private String name;

    @ApiModelProperty(value = "用户姓名头像")
    private String headImgUrl;

    @ApiModelProperty(value = "用户来源 枚举 UserSourceEnum 0:自己注册 1:好友分享")
    private Integer source;

    @ApiModelProperty(value = "该用户介绍人pid")
    private String referrerPid;

    @ApiModelProperty(value = "用户所属平台。枚举 UserPlatformEnum 0:C端普通用户 1:B端运营人员")
    private Integer userPlatform;
}
