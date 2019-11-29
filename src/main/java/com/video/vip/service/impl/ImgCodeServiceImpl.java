package com.video.vip.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.video.vip.basics.dto.Result;
import com.video.vip.basics.service.RedisService;
import com.video.vip.basics.util.enums.ResultEnum;
import com.video.vip.service.ImgCodeService;
import com.video.vip.util.login.CommonCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * 验证码图片业务操作实现类
 * @author 何智琦
 */
@Slf4j
@Component
@Service("imgCodeService")
public class ImgCodeServiceImpl implements ImgCodeService {

    @Autowired
    RedisService redisService;

    @Override
    public Result verifyImgCode(String onlyKey, String vcode) {
        log.info("图片验证码校验开始：onlyKey:{},vcode:{}",onlyKey,vcode);
        Result result = Result.newSuccess();
        Object obj = redisService.get(CommonCodeUtils.REDIS_IMG_VCODE_KEY+onlyKey);
        if(obj==null){
            log.warn("redis中未找到图片验证码onlyKey:{},vcode:{},obj:{}",onlyKey,vcode, JSONObject.toJSONString(obj));
            result = Result.newResult(ResultEnum.IMGVCODE_VALI_FAIL,"");
        }else{
            log.debug("redis中获取到相应的图片验证码:{}",obj);
            if(vcode.equals(obj.toString())){
                log.debug("图片验证码校验成功");
            }else{
                log.warn("图片验证码校验失败onlyKey:{},vcode:{},obj:{}",onlyKey,vcode, JSONObject.toJSONString(obj));
                result = Result.newResult(ResultEnum.IMGVCODE_VALI_FAIL,"");
            }
        }
        log.info("图片验证码校验结束：result:{}", JSON.toJSONString(result));
        return result;
    }
}
