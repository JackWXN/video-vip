package com.video.vip.controller.c;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wxn on 2020/1/14
 */
@CrossOrigin()
@RestController
@RequestMapping(value="/c/video")
@Slf4j
@Api(value = "VideoController", tags = "C端视频相关控制类")
public class VideoController {
}
