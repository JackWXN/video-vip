package com.video.vip.basics.util.enums.sql;

import lombok.Getter;
import lombok.Setter;

/**
 * sql语句类型枚举
 * @author wxn
 */
public enum SqlTypeEnum {
    INSERT("新增",1),
    DELETE("删除",2),
    UPDATE("修改",3),
    SELECT("查询",4)
    ;

    @Getter
    @Setter
    private String msg ;
    @Getter
    @Setter
    private int code ;
    SqlTypeEnum(String msg, int code) {
        this.msg = msg ;
        this.code = code ;
    }
}
