package com.video.vip.security.enums;

import lombok.Getter;
import lombok.Setter;

/**
 * 访问类型枚举
 * @author wxn
 */
public enum VisitVidTypeEnum {
    /**
     * 访问类型枚举
     */
    LOGIN("登录用户",1,30L,50),
    NOT_LOGIN("未登录用户",2,60L,10000)
            ;
    @Getter
    @Setter
    private String msg ;
    @Getter
    @Setter
    private Integer code ;

    /**
     * 过期时间（秒）
     */
    @Getter
    @Setter
    private Long overTimeSecond ;

    /**
     * 过期次数
     */
    @Getter
    @Setter
    private Integer overTimeCount ;

    VisitVidTypeEnum(String msg, Integer code, Long overTimeSecond, Integer overTimeCount) {
        this.msg = msg ;
        this.code = code ;
        this.overTimeSecond = overTimeSecond ;
        this.overTimeCount = overTimeCount ;
    }
}
