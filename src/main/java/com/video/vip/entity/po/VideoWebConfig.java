package com.video.vip.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.video.vip.entity.po.base.BaseModel;
import lombok.Data;

/**
 * Created by wxn on 2020/1/14
 */
@Data
@TableName(value = "video_web_config")
public class VideoWebConfig extends BaseModel {
    private static final long serialVersionUID = 5973719047899894262L;

    /**
     * 视频网站名称
     */
    private String name;

    /**
     * 视频网站图标url
     */
    private String iconUrl;

    /**
     * 视频网站跳转url
     */
    private String jumpUrl;

}
