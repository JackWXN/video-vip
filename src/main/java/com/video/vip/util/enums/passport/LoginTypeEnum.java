package com.video.vip.util.enums.passport;

import lombok.Getter;
import lombok.Setter;

/**
 * 登录类型枚举
 * @author 何智琦
 */
public enum LoginTypeEnum {
    /**
     * 登录类型枚举
     */
    TYPE_LOGIN("登录",1),
    TYPE_LOGOUT("登出",2)
    ;

    @Getter
    @Setter
    private String message ;
    @Getter
    @Setter
    private int code ;
    LoginTypeEnum(String message, int code) {
        this.message = message ;
        this.code = code ;
    }
}
