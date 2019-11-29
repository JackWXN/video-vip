package com.video.vip.basics.intercept.exception;

import com.alibaba.fastjson.JSON;
import com.video.vip.basics.exception.CustomException;
import com.video.vip.basics.util.basics.IPUtil;
import com.video.vip.basics.util.enums.ResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 异常捕捉
 * @author wxn
 */
@ControllerAdvice
@Component
@Slf4j
public class MyControllerAdviceIntt {

    /**
     * 全局异常捕捉处理
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Map errorHandler(Exception ex) {
        String requestURL = "";
        Map<String, String[]> parameterMap = null;
        String method = "";
        String ip = "";
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        if(request!=null){
            requestURL = request.getRequestURL().toString();
            parameterMap = request.getParameterMap();
            method = request.getMethod();
            ip = IPUtil.getClientIp(request);
        }
        Map map = new HashMap();
        map.put("code", ResultEnum.EXCEPTION.getCode());
        map.put("message", "服务器开小差~~");
        log.warn("异常拦截：requestURL=={},method=={},ip=={},parameterMap=={}",requestURL,method,ip, parameterMap.isEmpty()?"":JSON.toJSONString(parameterMap),ex);
        return map;
    }

    @ResponseBody
    @ExceptionHandler(value = CustomException.class)
    public Map handle(CustomException e) {
        log.warn("状态码:" + e.getErrorCode(), e);
        Map map = new HashMap();
        map.put("code", ResultEnum.LOGIN_DISABLE.getCode());
        map.put("message", e.getMessage());
        return map;
    }

}
