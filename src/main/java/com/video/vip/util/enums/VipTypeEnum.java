package com.video.vip.util.enums;

import lombok.Getter;
import lombok.Setter;

public enum VipTypeEnum {

    /**
     * vip类型
     */
    VIP_TYPE1(1,"体验卡"),
    VIP_TYPE2(2,"月卡"),
    VIP_TYPE3(3,"季卡"),
    VIP_TYPE4(4,"半年卡"),
    VIP_TYPE5(5,"年卡"),
    VIP_TYPE6(6,"终身卡"),;

    @Getter
    @Setter
    private int code;

    @Getter
    @Setter
    private String msg;

    VipTypeEnum(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public static VipTypeEnum codeOf(int code){
        for(VipTypeEnum obj : values()){
            if(obj.getCode() == code){
                return obj;
            }
        }
        return null;
    }
}
