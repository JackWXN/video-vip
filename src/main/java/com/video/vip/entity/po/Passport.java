package com.video.vip.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.video.vip.entity.po.base.BaseModel;
import lombok.Data;

/**
 * 用户帐号实体
 *
 * @author wxn
 */
@Data
@TableName(value = "passport")
public class Passport extends BaseModel {
    private static final long serialVersionUID = -7542096466761455392L;
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

}
