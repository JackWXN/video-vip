package com.video.vip.controller.login;

import com.alibaba.fastjson.JSONObject;
import com.video.vip.basics.constant.CommonConstant;
import com.video.vip.basics.dto.Result;
import com.video.vip.basics.dto.UserTokenDTO;
import com.video.vip.basics.util.enums.ResultEnum;
import com.video.vip.basics.util.enums.YesOrNoEnum;
import com.video.vip.entity.dto.PassportPwdLoginDTO;
import com.video.vip.entity.dto.PassportRegisterDTO;
import com.video.vip.entity.dto.PasswordUpdateDTO;
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

    @ApiOperation(value="账号+密码注册",response = Result.class)
    @RequestMapping(value="/pwd/register", method=RequestMethod.POST)
    public Result loginRegister(@RequestBody PassportRegisterDTO passportRegisterDTO) {
        log.info("手机号+验证码注册开始：passportRegisterDTO:{}",JSONObject.toJSONString(passportRegisterDTO));
        Result result;
        if(StringUtils.isEmpty(passportRegisterDTO.getAccount())||StringUtils.isEmpty(passportRegisterDTO.getPwdAes())){
            log.warn("账号|密码不能为空不能为空:passportRegisterDTO:{}", JSONObject.toJSONString(passportRegisterDTO));
            result = Result.newResult(ResultEnum.PARAM_FORMAT_ERROR,ResultEnum.PARAM_FORMAT_ERROR.getMsg());
        }else{
            PassportOperationTypeEnum passportOperationTypeEnum = ApiUloginUtil.chackPassportType(passportRegisterDTO.getAccount());
            result = loginService.register(passportOperationTypeEnum,passportRegisterDTO.getAccount(),passportRegisterDTO.getPwdAes(),ApiUloginUtil.TOKEN_OVERTIME_MS);
        }
        log.info("账号+密码注册注册结束：result:{}",result.toJSONString());
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

    @ApiOperation(value="根据老密码修改自己的密码",response = Result.class)
    @RequestMapping(value="/pwd/oldPassword/edit", method=RequestMethod.POST)
    public Result updateOldPassword(@RequestBody PasswordUpdateDTO passwordUpdateDTO) {
        String logStr = "根据老密码修改自己的密码";
        log.info("{}开始：passwordUpdateDTO:{}", logStr,JSONObject.toJSONString(passwordUpdateDTO));
        Result result;
        if(null==passwordUpdateDTO||StringUtils.isEmpty(passwordUpdateDTO.getOldPasswordAes())||StringUtils.isEmpty(passwordUpdateDTO.getNewPasswordAes())){
            log.warn("老密码|新密码不能为空:passwordUpdateDTO:{}",JSONObject.toJSONString(passwordUpdateDTO));
            result = Result.newResult(ResultEnum.NOT_NULL,"老密码|新密码不能为空");
        }else{
            //获取解析后的token
            UserTokenDTO tUserTokenDTO = (UserTokenDTO) request.getAttribute(CommonConstant.USER_TOKEN_KEY);
            result = loginService.updateOldPassword(logStr, PassportOperationTypeEnum.PID,tUserTokenDTO.getPid().toString(),passwordUpdateDTO.getNewPasswordAes(),passwordUpdateDTO.getOldPasswordAes(), YesOrNoEnum.NO,null);
        }
        log.info("{}结束：result:{}", logStr, result.toJSONString());
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
