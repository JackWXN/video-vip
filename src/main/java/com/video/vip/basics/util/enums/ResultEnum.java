package com.video.vip.basics.util.enums;

import lombok.Getter;
import lombok.Setter;

/**
 * 接口返回对象枚举
 * @author 何智琦
 */
public enum ResultEnum {
    //成功类枚举，区间1-1000
    SUCCESS_REGISTER("注册成功",100),
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
     * 注册错误返回码
     */
    REGISTER_ESISTS_YES("注册用户已经存在",-350),
    REGISTER_PHONE_ESISTS_YES("注册手机号已经存在",-351),
    REGISTER_PP_ESISTS_YES("注册帐号已经存在",-352),
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
     * 文件操作相关错误-20000到-29999
     */
    SFILE_BUSINESS_CODE_NULL_ERROR("静态文件服务-业务校验必须传递对应业务编码",-20000),
    SFILE_CHECK_TYPE_NULL_ERROR("静态文件服务-传递的校验类型不存在",-20001),
    FILE_PATCH_ERROR("没有取到对应的文件目录",-20003),
    /**
     * 数据库操作相关错误-30000到-39999
     */
    DB_NO_INFLUENCE_ERROR("数据库影响条数为0",-30000),
    /**
     * api返回提示语-40000到-49999
     */
    API_ERROR("网页跑丢啦",-40000),
    /**
     * 人脸识别相关-50000到-59999
     */
    FACE_VAGUE("人脸模糊",-50000),
    FACE_EXISTS("人脸已经存在",-50001),
    FACE_NOT_FOUND("未匹配到用户",-50002),
    FACE_NO("图片中未找到人脸",-50003),
    FACE_QPS_NO("QPS超限",-50004),

    /**
     * 短信相关-60000到-69999
     */
    SMS_OVER_MAX("短信次数超限",-60000),
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
