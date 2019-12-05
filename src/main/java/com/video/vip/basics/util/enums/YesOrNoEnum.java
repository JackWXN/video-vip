package com.video.vip.basics.util.enums;

import lombok.Getter;
import lombok.Setter;

/**
 * 是或否枚举
 * @author wxn
 */
public enum YesOrNoEnum {
    /**
     * 是或否枚举
     */
    YES("是",1),
    NO("否",0)
    ;

    @Getter
    @Setter
    private String msg ;
    @Getter
    @Setter
    private int code ;

    YesOrNoEnum(String msg, int code) {
        this.msg = msg ;
        this.code = code ;
    }

}

