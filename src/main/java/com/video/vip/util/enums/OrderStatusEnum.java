package com.video.vip.util.enums;

import lombok.Getter;
import lombok.Setter;

public enum OrderStatusEnum {

    /**
     * 订单状态
     */
    ORDER_STATUS_FAIL(1,"支付失败"),
    ORDER_STATUS_SUCCESS(2,"支付成功"),;

    @Getter
    @Setter
    private int code;

    @Getter
    @Setter
    private String msg;

    OrderStatusEnum(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public static OrderStatusEnum codeOf(int code){
        for(OrderStatusEnum obj : values()){
            if(obj.getCode() == code){
                return obj;
            }
        }
        return null;
    }
}
