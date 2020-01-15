package com.video.vip.util.enums;

import lombok.Getter;
import lombok.Setter;

public enum UserSourceEnum {
    /**
     * 用户来源
     */
    USER_SOURCE1(0,"自己注册"),
    USER_SOURCE2(1,"好友分享"),;

    @Getter
    @Setter
    private int code;

    @Getter
    @Setter
    private String msg;

    UserSourceEnum(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public static UserSourceEnum codeOf(int code){
        for(UserSourceEnum obj : values()){
            if(obj.getCode() == code){
                return obj;
            }
        }
        return null;
    }
}
