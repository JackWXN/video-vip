package com.video.vip.util.enums;

import lombok.Getter;
import lombok.Setter;

public enum VipStatusEnum {

    /**
     * vip状态
     */
    VIP_STATUS1(0,"未开通"),
    VIP_STATUS2(1,"已开通"),
    VIP_STATUS3(2,"已过期"),;

    @Getter
    @Setter
    private int code;

    @Getter
    @Setter
    private String msg;

    VipStatusEnum(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public static VipStatusEnum codeOf(int code){
        for(VipStatusEnum obj : values()){
            if(obj.getCode() == code){
                return obj;
            }
        }
        return null;
    }
}
