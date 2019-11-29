package com.video.vip.util.enums.passport;

import lombok.Getter;
import lombok.Setter;

/**
 * 用户状态枚举
 * @author wxn
 */
public enum UserStatusEnum {
    /**
     * 用户状态
     */
    DISABLE("禁用",1),
    ENABLE("启用",2),
    HANDING("冻结",3)
            ;
    @Getter
    @Setter
    private String msg ;
    @Getter
    @Setter
    private int code ;
    UserStatusEnum(String msg, int code) {
        this.msg = msg ;
        this.code = code ;
    }
}
