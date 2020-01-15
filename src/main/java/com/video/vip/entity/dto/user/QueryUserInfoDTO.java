package com.video.vip.entity.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by wxn on 2020/1/14
 */
@Data
@ApiModel(description = "查询用户信息实体")
public class QueryUserInfoDTO implements Serializable {
    private static final long serialVersionUID = 3725916604425611976L;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    @ApiModelProperty(value = "VIP状态 枚举 VipStatusEnum 0:未开通 1:已开通 2:已过期")
    private Integer vipStatus;

    @ApiModelProperty(value = "用户来源 枚举 UserSourceEnum 0:自己注册 1:好友分享")
    private Integer source;

    @ApiModelProperty(value = "用户手机号")
    private String phone;

    @ApiModelProperty(value = "用户所属平台。枚举 UserPlatformEnum 0:C端普通用户 1:B端运营人员")
    private Integer userPlatform;
}
