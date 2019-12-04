package com.video.vip.entity.po.base;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 公共实体类
 */
@Data
public class BaseModel implements Serializable {
    protected static final long serialVersionUID = -955709876587L;

    /**
     * 唯一编号
     */
    @TableId
    private Long id;

    /**
     * 创建日期
     */
    private Date createDate;

    /**
     * 修改日期
     */
    private Date updateDate;

    /**
     * 版本号
     */
    private Integer version;
}
