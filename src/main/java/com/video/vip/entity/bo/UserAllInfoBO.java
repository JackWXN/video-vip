package com.video.vip.entity.bo;

import com.video.vip.entity.po.base.BaseModel;
import lombok.Data;

import java.util.Date;

/**
 * Created by wxn on 2020/1/14
 */
@Data
public class UserAllInfoBO extends BaseModel {
    private static final long serialVersionUID = 3037938580935750524L;

    /**
     * 用户pid
     */
    private Long pid;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 用户头像
     */
    private String headImgUrl;

    /**
     * 用户来源。枚举 UserSourceEnum
     * 0:自己注册 1:好友分享
     */
    private Integer source;

    /**
     * 该用户介绍人pid
     */
    private Long referrerPid;

    /**
     * vip状态。枚举 VipStatusEnum
     * 0:未开通 1:已开通 2:已过期
     */
    private Integer vipStatus;

    /**
     * vip类型。枚举 VipTypeEnum
     * 1:体验卡 2:月卡 3:季卡 4:半年卡 5:年卡 6:终身卡
     */
    private Integer vipType;

    /**
     * vip开通时间
     */
    private Date vipStartDate;

    /**
     * vip到期时间
     */
    private Date vipEndDate;

    /**
     * 用户所属平台。枚举 UserPlatformEnum
     * 0:C端普通用户 1:B端运营人员
     */
    private Integer userPlatform;

    /**
     * 手机号
     */
    private String phone;
}
