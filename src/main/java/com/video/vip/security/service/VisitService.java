package com.video.vip.security.service;

import com.video.vip.basics.dto.Result;
import com.video.vip.security.enums.VisitVidTypeEnum;

/**
 * 访问相关业务操作类
 * @author wxn
 */
public interface VisitService {

    /**
     * 用户访问频率限制，如果访问被限制，则返回剩余锁定秒数
     * @param vid 访问id
     * @param visitVidTypeEnum 访问id类型,VisitVidTypeEnum
     * @return
     */
    Result<Long> userFrequency(String vid, VisitVidTypeEnum visitVidTypeEnum);
}
