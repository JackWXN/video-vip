package com.video.vip.entity.dto.passport;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.Date;

/**
 * 用户帐号实体
 *
 * @author 何智琦
 */
@Data
public class PassportDTO{
    private static final long serialVersionUID = -7542096466761455392L;
    /**
     * 主键自增
     */
    private Long id;
    /**
     * 帐号手机号，用户的手机号
     */
    private String pPhone;
    /**
     * 帐号登录密码，md5加密
     */
    private String pPwd;
    /**
     * 帐号密码盐，密码+密码盐生成md5
     */
    private String pPwdSalt;
    /**
     * 帐号状态，AccountStatusEnum，【启用：ASE_E；禁用：ASE_D】
     */
    private String pStatus;
    /**
     * 帐号邮箱
     */
    private String ppMail;
    /**
     * 尝试修改密码天，和当天尝试修改密码次数联合使用，格式yyyyMMrr
     */
    private String epwdDate;
    /**
     * 当天尝试修改密码次数，和尝试修改密码天联合使用。每天清零，每天尝试修改密码次数最多5次
     */
    private Integer epwdDateCount;
    /**
     * 重置当天修改密码次数，和尝试修改密码天联合使用。最高重置3次
     */
    private Integer epwdResetCount;
    /**
     * 预处理密码，md5加密。确认生效后才替换主密码
     */
    private String ppReadyPwd;
    /**
     * 预处理密码盐，确认生效后才替换主密码盐
     */
    private String ppReadyPwdSalt;
    /**
     * 业务类型id,默认为0，未知,BusinessTypeEnum
     */
    private Integer businessTypeId;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 最后一次修改日期
     */
    private Date updateDate;

    /**
     * 版本号
     */
    private Integer version;


    /**
     * 用户token，不保存在数据库中，
     */
    private String token;
    /**
     * 用户的角色信息，不保存在数据库中，
     */
    private JSONObject roleJson;
    /**
     * 是不是新注册的用户，1是0否
     */
    private String registerIs;
}
