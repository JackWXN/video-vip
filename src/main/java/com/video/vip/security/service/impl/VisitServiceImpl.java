package com.video.vip.security.service.impl;

import com.video.vip.basics.dto.Result;
import com.video.vip.security.enums.VisitVidTypeEnum;
import com.video.vip.security.service.VisitService;
import com.video.vip.security.util.CommonApiSecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * 访问相关业务操作类
 * @author wxn
 */

@Slf4j
@Component
@Service("VisitService")
public class VisitServiceImpl implements VisitService {

    @Override
    public Result<Long> userFrequency(String vid, VisitVidTypeEnum visitVidTypeEnum) {
        log.debug("用户访问频率限制开始:vid:{},visitVidTypeEnum:{}"
                , vid, visitVidTypeEnum);
        Result<Long> result = Result.newSuccess();
        if (visitVidTypeEnum.equals(VisitVidTypeEnum.LOGIN)) {
            log.debug("频率验证类型为login，说明已登录");
            result = CommonApiSecurityUtils.frequency(vid, visitVidTypeEnum);
        } else if (visitVidTypeEnum.equals(VisitVidTypeEnum.NOT_LOGIN)) {
            //未登录的都要考虑做缓存
            log.debug("其他验证类型。未登录");
        } else {
            log.warn("用户访问的过滤类型未找到");
        }

        log.debug("用户访问频率限制结束:{}", result);
        return result;
    }

}
