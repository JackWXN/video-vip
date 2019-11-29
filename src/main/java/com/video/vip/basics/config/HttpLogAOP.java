package com.video.vip.basics.config;


import com.alibaba.fastjson.JSONObject;
import com.video.vip.basics.constant.CommonConstant;
import com.video.vip.basics.util.basics.DateUtil;
import com.video.vip.basics.util.basics.IPUtil;
import com.video.vip.basics.util.basics.Numbers;
import com.video.vip.basics.util.rest.WebAppTraceFilter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

/**
 * controller AOP,进行controller访问日志记录
 * @author wxn
 */
@Aspect
@Configuration
@Order(500)
@Slf4j
public class HttpLogAOP {

    private static final Logger httplog = LoggerFactory.getLogger("http.log");
    /**
     * http日志最大长度
     */
    private static final int STR_LEN_NUM = 1024;

    /**
     * 定义切点Pointcut
     */
    @Pointcut("execution(* *..controller..*Controller.*(..))")
    public void logPointCut() {
    }


    @Around("logPointCut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String mdcHeadName = request.getHeader(WebAppTraceFilter.HEADER_NAME);
        if (StringUtils.isBlank(mdcHeadName)) {
            if(StringUtils.isBlank(MDC.get(WebAppTraceFilter.HEADER_NAME))){
                MDC.put(WebAppTraceFilter.HEADER_NAME, "hlog-"+ UUID.randomUUID().toString().replaceAll("-",""));
            }
        }else{
            MDC.put(WebAppTraceFilter.HEADER_NAME, mdcHeadName);
        }
        String logUuid = Numbers.uuid();
        request.setAttribute("_HLOG_ID",logUuid);
        long startTime = System.currentTimeMillis();
        Object ob = null;
        try {
            // ob 为方法的返回值
            ob = pjp.proceed();
            request.setAttribute("_HLOG_HS",(System.currentTimeMillis() - startTime));
        }catch (Exception ee){
            JSONObject jobj = new JSONObject();
            jobj.put("logUuid",logUuid);
            jobj.put("mdsid",MDC.get(WebAppTraceFilter.HEADER_NAME));
            //耗时(毫秒)
            jobj.put("ctime",(System.currentTimeMillis() - startTime));
            JSONObject jo = new JSONObject();
            jo.put("exception",ee.getMessage());
            //返回值
            jobj.put("retu",jo.toJSONString());
            httplog.info(jobj.toJSONString());
            throw ee;
        }
        return ob;
    }

    @Before("logPointCut()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        try{
            HttpServletRequest request = attributes.getRequest();
            String logUuid = request.getAttribute("_HLOG_ID")+"";
            String ip = IPUtil.getClientIp(request);
            String strParam = Arrays.toString(joinPoint.getArgs());
//            log.info(
//                    "IP={},"+CommonConstant.LOG_FGH
//                            +"CLASS_METHOD={}," +CommonConstant.LOG_FGH
//                            +"HTTP METHOD={},"+CommonConstant.LOG_FGH
//                            +"请求地址={},"+CommonConstant.LOG_FGH
//                            +"参数={},"
//                    ,ip
//                    ,joinPoint.getSignature().getDeclaringTypeName() + "."
//                            + joinPoint.getSignature().getName()
//                    ,request.getMethod()
//                    ,request.getRequestURL().toString()
//                    ,strParam.length()>500?strParam.substring(0,500):strParam
//            );
            JSONObject jobj = new JSONObject();
            jobj.put("logUuid",logUuid);
            //请求ip
            jobj.put("cip",ip);
            //执行机器ip
            jobj.put("execIp", CommonConstant.SERVER_IP);
            jobj.put("thread",Thread.currentThread().getName());
            //过程唯一编码
            jobj.put("mdsid",MDC.get(WebAppTraceFilter.HEADER_NAME));
            jobj.put("classMethod",joinPoint.getSignature().getDeclaringTypeName() + "."
                    + joinPoint.getSignature().getName());
            jobj.put("method",request.getMethod());
            String rurl = request.getRequestURL().toString();
            jobj.put("rurl",rurl.length()>2000?rurl.substring(0,2000):rurl);
            jobj.put("projectName",CommonConstant.PROJECT_NAME);
            jobj.put("methodParams", strParam.length()>STR_LEN_NUM?strParam.substring(0,STR_LEN_NUM):strParam);
            jobj.put("beginDate", DateUtil.getDateTime(new Date(),CommonConstant.DATE_FORMAT_MS));
            httplog.info(jobj.toJSONString());
        }catch(Exception ee){
            log.error("接口日志拦截出现异常！joinPoint={}",joinPoint,ee);
        }
    }

    // returning的值和doAfterReturning的参数名一致
    @AfterReturning(returning = "ret", pointcut = "logPointCut()")
    public void doAfterReturning(Object ret) throws Throwable {
        try{
            // 接收到请求，记录请求内容
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            String logUuid = request.getAttribute("_HLOG_ID")+"";
            String logHs = request.getAttribute("_HLOG_HS")+"";
            String strRetu = JSONObject.toJSONString(ret);
            JSONObject jobj = new JSONObject();
            jobj.put("logUuid",logUuid);
            jobj.put("mdsid",MDC.get(WebAppTraceFilter.HEADER_NAME));
            //耗时
            jobj.put("ctime",logHs);
            jobj.put("endDate", DateUtil.getDateTime(new Date(),CommonConstant.DATE_FORMAT_MS));
            //返回值
            jobj.put("retu",strRetu.length()>STR_LEN_NUM?strRetu.substring(0,STR_LEN_NUM):strRetu);
            httplog.info(jobj.toJSONString());
            log.debug("在MDC中删除:vip-header-rid = {}",MDC.get(WebAppTraceFilter.HEADER_NAME));
            MDC.remove(WebAppTraceFilter.HEADER_NAME);
        }catch(Exception ee){
            log.error("接口日志拦截出现异常！ret={}",ret,ee);
        }
    }

}
