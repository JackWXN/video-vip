package com.video.vip.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.video.vip.entity.po.base.BaseModel;
import lombok.Data;

/**
 * Created by wxn on 2020/1/14
 */
@Data
@TableName(value = "today_recommend_config")
public class TodayRecommendConfig extends BaseModel {
    private static final long serialVersionUID = 1307040357525186329L;

    /**
     * 视频名称
     */
    private String name;

    /**
     * 视频封面url
     */
    private String iconUrl;

    /**
     * 视频简介
     */
    private String describe;
}
