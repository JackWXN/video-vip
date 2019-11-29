package com.video.vip.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * Created by wxn on 2019/11/29
 */
@Data
@TableName(value = "login_trail")
public class LoginTrail {

    /**
     * 主键自增
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    /**
     * pid
     */
    private Long pid;

    /**
     * 操作类型,LoginTypeEnum:1:登录,2:登出
     */
    private int operationType;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 最后一次修改日期
     */
    private Date updateDate;

    /**
     * 版本号
     */
    private Integer version;
}
