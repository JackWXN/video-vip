package com.video.vip.service;

import com.video.vip.basics.dto.Result;

/**
 * 验证码图片操作类
 * @author wxn
 */
public interface ImgCodeService {

    /**
     * 根据redis key+图片验证码登录
     * @param onlyKey 唯一key
     * @param vcode 图形验证码
     * @return
     */
    Result verifyImgCode(String onlyKey, String vcode);
}
