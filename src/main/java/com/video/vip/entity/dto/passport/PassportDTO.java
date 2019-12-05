package com.video.vip.entity.dto.passport;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.Date;

/**
 * 用户帐号实体
 *
 * @author wxn
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
    private String phone;
    /**
     * 帐号登录密码，md5加密
     */
    private String password;
    /**
     * 帐号密码盐，密码+密码盐生成md5
     */
    private String passwordSalt;
    /**
     * 帐号状态，UserStatusEnum，1:禁用,2:启用,3:冻结
     */
    private int status;

    /**
     * 帐号，用来登录的帐号名称
     */
    private String account;
    /**
     * 帐号邮箱
     */
    private String mail;
    /**
     * 尝试修改密码天，和当天尝试修改密码次数联合使用，格式yyyyMMrr
     */
    private String passwordEditDate;
    /**
     * 当天尝试修改密码次数，和尝试修改密码天联合使用。每天清零，每天尝试修改密码次数最多5次
     */
    private Integer passwordEditDateCount;
    /**
     * 重置当天修改密码次数，和尝试修改密码天联合使用。最高重置3次
     */
    private Integer passwordEditResetCount;
    /**
     * 预处理密码，md5加密。确认生效后才替换主密码
     */
    private String ppReadyPwd;
    /**
     * 预处理密码盐，确认生效后才替换主密码盐
     */
    private String ppReadyPwdSalt;

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
