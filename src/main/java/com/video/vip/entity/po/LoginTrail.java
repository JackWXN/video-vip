package com.video.vip.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.video.vip.entity.po.base.BaseModel;
import lombok.Data;

/**
 * Created by wxn on 2019/11/29
 */
@Data
@TableName(value = "login_trail")
public class LoginTrail extends BaseModel {
    private static final long serialVersionUID = 7610227496043487863L;

    /**
     * pid
     */
    private Long pid;

    /**
     * 操作类型,LoginTypeEnum:1:登录,2:登出
     */
    private int operationType;

}
