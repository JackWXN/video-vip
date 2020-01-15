package com.video.vip.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by wxn on 2020/1/14
 */
@Data
@ApiModel(description = "用户信息实体")
public class UserInfoVO implements Serializable {
    private static final long serialVersionUID = -6585514814052529348L;

    @ApiModelProperty(value = "用户id")
    private String id;

    @ApiModelProperty(value = "用户pid")
    private String pid;

    @ApiModelProperty(value = "用户手机号")
    private String phone;

    @ApiModelProperty(value = "注册日期")
    private String registrationDate;

    @ApiModelProperty(value = "用户来源")
    private String source;

    @ApiModelProperty(value = "推荐人手机号")
    private String referrerPhone;

    @ApiModelProperty(value = "VIP状态 枚举 VipStatusEnum 0:未开通 1:已开通 2:已过期")
    private String vipStatus;

    @ApiModelProperty(value = "vip类型。枚举 VipTypeEnum 1:体验卡 2:月卡 3:季卡 4:半年卡 5:年卡 6:终身卡")
    private String vipType;

    @ApiModelProperty(value = "vip开通时间")
    private String vipStartDate;

    @ApiModelProperty(value = "vip到期时间")
    private String vipEndDate;

    @ApiModelProperty(value = "用户所属平台。枚举 UserPlatformEnum 0:C端普通用户 1:B端运营人员")
    private String userPlatform;
}
