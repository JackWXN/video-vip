package com.video.vip.basics.util.enums;

import lombok.Getter;
import lombok.Setter;

/**
 * 接口返回对象枚举
 * @author wxn
 */
public enum ResultEnum {
    /**
     * 注册
     */
    SUCCESS_REGISTER("注册成功",100),
    REGISTER_ESISTS_YES("注册用户已经存在",-101),
    REGISTER_PHONE_ESISTS_YES("注册手机号已经存在",-102),
    REGISTER_PP_ESISTS_YES("注册帐号已经存在",-103),
    REGISTER_ERROR("注册失败",-104),
    REGISTER_NO("未注册失败",-105),


    //成功类枚举，区间1-1000
    SUCCESS_NO_LATEST_DATA("执行成功，而且已无最新数据",3),
    SUCCESS("执行成功",1),
    RECEIVE_SUCCESS("接收成功",2),
    //处理中类型，区间1001-2000
    PROCESSING("处理中",1001),
    //待处理类型，0
    PENDING("待处理",0),
    //失败类型枚举负数
    FAIL("失败",-1),
    INVALID_REQUEST("发出的请求有错误",-2),
    UNAUTHORIZED("没有权限",-3),
    FORBIDDEN("禁止访问",-4),
    INTERNAL_SERVER_ERROR("服务器发生错误，无法判断发出的请求是否成功",-8),
    UNKNOWN_ERROR("未知错误",-9),
    NOT_NULL("不能为空",-10),
    IP_tWHITE_LIST_CHECK_NOT_THROUGH("IP白名单校验未通过",-11),
    NOT_FOUND_ENUM_TYPE("未找到所选择的类型",-12),
    NOT_FOUND("没有找到",-13),
    FREQUENT_ACCESS("请不要频繁访问",-14),
    AGE_FAIL("年龄不符合要求",-15),
    REPEAT_REQUEST("重复请求",-16),
    REPEAT_ESISTS_YES("已经存在",-17),
    //黑名单用户
    BLACKLIST_USER_YES("是黑名单用户",-50),
    //系统返回异常
    EXCEPTION("服务器开小差~",-150),
    EXCEPTION_SYS("服务器开小差151~",-151),
    IMGVCODE_LOGIN_PUT("请输入图片验证码",-200),
    IMGVCODE_VALI_FAIL("图片验证码校验失败",-201),
    UNLAWFUL_REQUEST("非法请求",-202),

    /**
     * 登录错误返回码
     */
    LOGIN_PHONE_VCOE_FAILURE("手机验证码校验失败",-400),
    LOGIN_TOKEN_GENERATE_FAILURE("token生成失败",-401),
    LOGIN_UNREGISTERED("未注册",-402),
    LOGIN_UNBOUND("未绑定",-403),
    LOGIN_DISABLE("帐号被禁用",-404),
    LOGIN_NO("未登录",-405),
    LOGIN_TOKEN_VALI_FAIL("token验证失败",-410),
    LOGIN_T0KEN_OVERDUE("token过期",-411),
    /**
     * 访问错误返回码
     */
    //签名错误
    SIGN_ERROR("请求拒绝",-500),
    /**
     * 表单验证失败枚举-10000到-19999
     */
    PHONE_FORMAT_ERROR("手机号格式不正确",-10000),
    SMS_FORMAT_ERROR("短信验证码格式错误",-10010),
    LONGIN_PWD_ERROR("用户密码错误",-10020),


    /**
     * 参数类
     */
    PARAM_FORMAT_ERROR("参数缺失",-11000),
            ;

    @Getter
    @Setter
    private String msg ;
    @Getter
    @Setter
    private Integer code ;
    ResultEnum(String msg, Integer code) {
        this.msg = msg ;
        this.code = code ;
    }
}
