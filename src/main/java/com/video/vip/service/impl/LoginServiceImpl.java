package com.video.vip.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.video.vip.basics.dto.Result;
import com.video.vip.basics.util.enums.ResultEnum;
import com.video.vip.entity.dto.passport.PassportDTO;
import com.video.vip.service.LoginService;
import com.video.vip.util.enums.PassportOperationTypeEnum;
import com.video.vip.util.token.JavaWebTokenUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 帐号登录相关操作实现
 *
 * @author wxn
 */
@Slf4j
@Service("loginService")
public class LoginServiceImpl implements LoginService {

    @Override
    public String validateToken(String token) {
        log.info("解析token开始：token:{}", token);
        String jsonToken = null;
        if(StringUtils.isEmpty(token)){
            log.warn("解析token失败，token:{}",token);
        }else{
            try {
                jsonToken = JavaWebTokenUtil.parseJWT(token);
            } catch (Exception e) {
                log.warn("解析token失败！token错误！token:{}", token, e);
            }
        }
        log.info("解析token结束:jsonToken:{}", jsonToken);
        return jsonToken;
    }


    @Override
    public Result logoutByToken(String token) {
        log.info("进行登出业务操作开始:token:{}",token);
        Result result;
        final boolean flag = JavaWebTokenUtil.logoutByToken(token);
        if(!flag){
            log.warn("登出操作无具体的返回结果");
            result = Result.newResult(ResultEnum.FAIL,"登出失败");
        }else{
            result = Result.newSuccess();
        }
        log.info("进行登出业务操作结束：返回：{}", result.toJSONString());
        return result;
    }

    @Override
    public Result<PassportDTO> login(@NonNull PassportOperationTypeEnum passportOperationTypeEnum, @NonNull String account, @NonNull String pwdAes, @NonNull Long tokenExpiresMs) {
        log.info("根据密码登录开始:passportOperationTypeEnum:{},account:{},pwdAes:{},tokenExpiresMs:{}",passportOperationTypeEnum,account,pwdAes,tokenExpiresMs);
        Result result;
        try {
            result = passwordService.getPassportByType(passportOperationTypeEnum,account);
            PassportDTO passportDTO = (PassportDTO)result.getData();
            result.setData(passportDTO);
            result = passport2Service.valiPassportStatus(passportDTO);
            if (result.isSuccess()) {
                //校验密码
                result = PasswordUtil.encryption(pwdAes,passportDTO.getPPwdSalt(),false);
                if(result.isSuccess()){
                    if(passportDTO.getPPwd().equals(result.getData())){
                        log.debug("密码比对通过：result:{}",result.toJSONString());
                        result =  passport2Service.login(passportDTO,platformIdEnum,appId,tokenExpiresMs);
                    }else{
                        result = Result.newResult(ResultEnum.LONGIN_PWD_ERROR,"用户名或密码错误");
                        log.warn("用户密码错误:passportDTO:{}", JSONObject.toJSONString(passportDTO));
                    }
                }
            }
        } catch (Exception e) {
            log.error("发生异常", e);
            result = Result.newResult(ResultEnum.EXCEPTION,"");
        }
        log.info("--> 根据密码登录结束:result:{}",result.toJSONString());
        return result;
    }

    private Result<PassportDTO> getPassportByAccount(PassportOperationTypeEnum passportOperationTypeEnum, String account) {
        Result<PassportDTO> result = Result.newSuccess();
        PassportDTO passportDTO = null;
        if(passportOperationTypeEnum.equals(PassportOperationTypeEnum.PHONE)){
            passportDTO = passportDAO.getByPhone(account.trim());
        }else if(passportOperationTypeEnum.equals(PassportOperationTypeEnum.MAIL)){
            passportDTO = passportDAO.getByMail(account.trim());
        }else if(passportOperationTypeEnum.equals(PassportOperationTypeEnum.PID)){
            passportDTO = passportDAO.getById(Long.parseLong(account));
        }else{
            log.warn("目前不支持{}类型：passportOperationTypeEnum：{}",passportOperationTypeEnum.getMsg(),passportOperationTypeEnum);
            result = Result.newResult(ResultEnum.FAIL,"类型错误");
        }
        result.setData(passportDTO);
        log.info("查询的账号信息：result:{}", JSON.toJSONString(result));
        return result;
    }
}
