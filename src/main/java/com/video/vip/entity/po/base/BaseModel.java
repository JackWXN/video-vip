package com.video.vip.entity.po.base;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 公共类
 */
@Data
public class BaseModel implements Serializable {
    protected static final long serialVersionUID = -955709876587L;

    /**
     * 唯一编号
     */
    private Long id;

    /**
     * 创建日期
     */
    private Date createTime;

    /**
     * 修改日期
     */
    private Date updateTime;

    /**
     * 版本号
     */
    private Integer version;
}
