package com.video.vip.util.enums.passport;

import lombok.Getter;
import lombok.Setter;

/**
 * 账号操作类型
 * @author wxn
 */
public enum PassportOperationTypeEnum {
    /**
     * 账号操作类型
     */
    PID("pid",1),
    MAIL("邮箱",2),
    PHONE("手机号",3),
    WXPN("微信公众号",4),
            ;
    @Getter
    @Setter
    private String msg ;
    @Getter
    @Setter
    private int code ;
    PassportOperationTypeEnum(String msg, int code) {
        this.msg = msg ;
        this.code = code ;
    }
}
