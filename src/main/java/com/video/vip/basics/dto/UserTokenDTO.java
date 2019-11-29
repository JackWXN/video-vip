package com.video.vip.basics.dto;


import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户token解析相关
 * @author 何智琦
 */
@Data
public class UserTokenDTO implements Serializable {

    private static final long serialVersionUID = 940036276561731092L;
    /**
     * 用户帐号唯一标识
     */
    private Long pid;
    /**
     * 用户登录token
     */
    private String token;
    /**
     * 用户appid
     */
    private String appId;
    /**
     * 用户角色集合
     */
    private JSONObject roles;
    /**
     * 用户手机号
     */
    private String phone;
    /**
     * 用户登录时间
     */
    private Date loginDate;
}
