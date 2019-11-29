package com.video.vip.controller.passport;

import com.alibaba.fastjson.JSONObject;
import com.video.vip.basics.constant.CommonConstant;
import com.video.vip.basics.dto.Result;
import com.video.vip.basics.dto.UserTokenDTO;
import com.video.vip.basics.util.enums.ResultEnum;
import com.video.vip.entity.dto.PassportPwdLoginDTO;
import com.video.vip.entity.dto.passport.PassportDTO;
import com.video.vip.service.ImgCodeService;
import com.video.vip.service.LoginService;
import com.video.vip.util.enums.passport.PassportOperationTypeEnum;
import com.video.vip.util.login.ApiUloginUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by wxn on 2019/11/28
 */
@CrossOrigin()
@RestController
@RequestMapping(value="/login")
@Slf4j
@Api(value = "LoginController", tags = "注册登录控制类")
public class LoginController {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private LoginService loginService;

    @Autowired
    private ImgCodeService imgCodeService;

    @ApiOperation(value="登出", response = Result.class)
    @RequestMapping(value="/logout", method= RequestMethod.POST)
    public Result logout() {
        log.info("登出开始");
        Result result;
        try{
            //获取解析后的token
            UserTokenDTO tUserTokenDTO = (UserTokenDTO) request.getAttribute(CommonConstant.USER_TOKEN_KEY);
            log.debug("用户登录信息：tUserTokenDTO:{}", JSONObject.toJSONString(tUserTokenDTO));
            result = loginService.logoutByToken(tUserTokenDTO.getToken());
        }catch (Exception e){
            log.error("登出失败。",e);
            result = Result.newResult(ResultEnum.EXCEPTION,ResultEnum.EXCEPTION.getMsg());
        }
        log.info("登出结束：result:{}",result.toJSONString());
        return result;
    }

    @ApiOperation(value="账号+密码登录",response = Result.class)
    @RequestMapping(value="/pwd/login", method=RequestMethod.POST)
    public Result<JSONObject> login(@RequestBody PassportPwdLoginDTO passportPwdLoginDTO) {
        log.info("账号+密码登录开始：passportPwdLoginDTO:{}",JSONObject.toJSONString(passportPwdLoginDTO));
        Result<JSONObject> result = Result.newResult(ResultEnum.FAIL,"");
        if(StringUtils.isEmpty(passportPwdLoginDTO.getImgVcode())){
            log.warn("图片验证码不能为空:passportPwdLoginDTO:{}", JSONObject.toJSONString(passportPwdLoginDTO));
            result = Result.newResult(ResultEnum.IMGVCODE_LOGIN_PUT,"");
        }else if(StringUtils.isEmpty(passportPwdLoginDTO.getAccount())||StringUtils.isEmpty(passportPwdLoginDTO.getPwdAes())){
            log.warn("账号|图片验证码|密码不能为空:clcPassportPwdLoginPDTO:{}", JSONObject.toJSONString(passportPwdLoginDTO));
            result = Result.newResult(ResultEnum.FAIL,"账号|密码不能为空");
        }else{
            String imgKey = getPwdKey(passportPwdLoginDTO);
            Result imgCodeCheckResult = imgCodeService.verifyImgCode(imgKey,passportPwdLoginDTO.getImgVcode());
            if(imgCodeCheckResult.isSuccess()) {
                log.debug("图片验证码校验成功:clcPassportPwdLoginPDTO:{}", JSONObject.toJSONString(passportPwdLoginDTO));
                PassportOperationTypeEnum passportOperationTypeEnum = ApiUloginUtil.chackPassportType(passportPwdLoginDTO.getAccount());
                Result<PassportDTO> loginResult = loginService.login(passportOperationTypeEnum,passportPwdLoginDTO.getAccount(),passportPwdLoginDTO.getPwdAes(),ApiUloginUtil.TOKEN_OVERTIME_MS);
                if(loginResult.isSuccess()){
                    JSONObject joData = new JSONObject();
                    joData.put("token",loginResult.getData().getToken());
                    result = Result.newSuccess(joData);
                }else{
                    result.setCode(loginResult.getCode());
                    result.setMessage(loginResult.getMessage());
                    result.setData(null);
                }
            }
        }
        log.info("账号+密码登录结束：result:{}",result.toJSONString());
        return result;
    }

    private String getPwdKey(PassportPwdLoginDTO passportPwdLoginDTO) {
        String imgKeyForRedis = passportPwdLoginDTO.getAccount();
        if (!StringUtils.isEmpty(passportPwdLoginDTO.getRandomKey())) {
            imgKeyForRedis = passportPwdLoginDTO.getRandomKey();
        }
        return  imgKeyForRedis;
    }
}
