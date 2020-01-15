package com.video.vip.util.enums;

import lombok.Getter;
import lombok.Setter;

public enum UserPlatformEnum {

    /**
     * 用户所属平台
     */
    USER_PLATFORM1(0,"C端普通用户"),
    USER_PLATFORM2(1,"B端运营人员"),;

    @Getter
    @Setter
    private int code;

    @Getter
    @Setter
    private String msg;

    UserPlatformEnum(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public static UserPlatformEnum codeOf(int code){
        for(UserPlatformEnum obj : values()){
            if(obj.getCode() == code){
                return obj;
            }
        }
        return null;
    }
}
