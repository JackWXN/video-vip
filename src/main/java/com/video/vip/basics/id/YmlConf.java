package com.video.vip.basics.id;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Describe:
 * @Author: wxn
 * @Date: 2018/6/11 12:50
 **/
@ConfigurationProperties("business")
@Configuration
@Data
public class YmlConf {
    private long bizNum = 1;
    private long instanceNum=1;
}
