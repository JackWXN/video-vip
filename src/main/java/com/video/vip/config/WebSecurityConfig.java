package com.video.vip.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.video.vip.basics.constant.CommonConstant;
import com.video.vip.basics.dto.Result;
import com.video.vip.basics.dto.UserTokenDTO;
import com.video.vip.basics.util.FuncUtil;
import com.video.vip.basics.util.basics.MyApplicationContextUtil;
import com.video.vip.basics.util.enums.ResultEnum;
import com.video.vip.security.enums.VisitVidTypeEnum;
import com.video.vip.security.service.VisitService;
import com.video.vip.security.util.CommonApiSecurityUtils;
import com.video.vip.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户权限拦截
 * @author wxn
 */
@Slf4j
@Configuration
public class WebSecurityConfig extends WebMvcConfigurationSupport {

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Bean
    public SecurityInterceptor getSecurityInterceptor() {
        return new SecurityInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration addInterceptor = registry.addInterceptor(getSecurityInterceptor());
        // 拦截配置
        addInterceptor.addPathPatterns("/**");

        // 排除配置
        addInterceptor.excludePathPatterns("/swagger**"
                ,"/webjars/**"
                ,"/swagger-resources/**"
                ,"/img/vcode/**"
                ,"/login/pwd/register"
                ,"/login/pwd/login"
        );
    }

    private class SecurityInterceptor extends HandlerInterceptorAdapter {

        /**
         * 请求前拦截
         * @param request
         * @param response
         * @param handler
         * @return
         * @throws Exception
         */
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
            Result result;
            String token = request.getHeader(CommonApiSecurityUtils.TOKEN_KEY);
            if(StringUtils.isEmpty(token)||token.trim().equals("null")){
                log.warn("用户未登录:url:{},uri:{}",request.getRequestURL(),request.getRequestURI());
                result = Result.newResult(ResultEnum.LOGIN_NO,"");
            }else{
                LoginService loginService = (LoginService) MyApplicationContextUtil.getContext().getBean("loginService");
                String strJsonToken = loginService.validateToken(token);
                if(StringUtils.isEmpty(strJsonToken)){
                    log.warn("用户token过期或未登录:token:{},strJsonToken:{}"
                            ,token,strJsonToken);
                    result = Result.newResult(ResultEnum.LOGIN_NO,"");
                }else{
                    log.debug("用户已登录:token:{}",token);
                    //开始解析token
                    JSONObject jsonObjToken = JSONObject.parseObject(strJsonToken);
                    UserTokenDTO tUserTokenDTO = FuncUtil.getUserTokenDTOToken(jsonObjToken,token);
                    if(tUserTokenDTO!=null &&tUserTokenDTO.getPid()!=null){
                        log.debug("用户token解析成功，并且存在pid");
                        //开始校验登录用户访问频率
                        VisitService visitService = (VisitService) MyApplicationContextUtil.getContext().getBean("VisitService");
                        result = visitService.userFrequency(tUserTokenDTO.getPid().toString(), VisitVidTypeEnum.LOGIN);
                        if(result.isSuccess()){
                            log.debug("用户访问频率验证通过:{}",result);
                            request.setAttribute(CommonConstant.USER_TOKEN_KEY,tUserTokenDTO);
                        }else{
                            log.warn("请不要频繁访问{}",result);
                            FuncUtil.outPrint(result.toJSONString(),response);
                            return false;
                        }
                    }else{
                        log.warn("用户token解析成功，但pid为空。禁止访问");
                        result = Result.newResult(ResultEnum.LOGIN_NO,"");
                    }
                }
            }
            if(result.isSuccess()){
                log.debug("验证成功：result:{}",result);
                return true;
            }else{
                String strUri = request.getRequestURI();
                strUri = strUri.replaceAll("\\\\","/");
                while(strUri.contains("//")){
                    strUri = strUri.replaceAll("//","/");
                }
                if(strUri.indexOf("/user/info/init")==0){
                    log.debug("是首页，不登陆也可以访问，直接进入访问");
                    return true;
                }else if(strUri.indexOf("/user/info/overdueAndIdcard")==0){
                    log.debug("是我的，不登陆也可以访问，直接进入访问");
                    return true;
                }else{
                    log.debug("验证失败，禁止访问：result:{}",result);
                    FuncUtil.outPrint(result.toJSONString(),response);
                    return false;
                }
            }
        }

        /**
         * 请求后拦截
         * @param request
         * @param response
         * @param handler
         * @param modelAndView
         * @throws Exception
         */
        @Override
        public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
            super.postHandle(request, response, handler, modelAndView);
        }
    }
}
