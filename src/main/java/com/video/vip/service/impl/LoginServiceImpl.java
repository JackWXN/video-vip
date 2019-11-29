package com.video.vip.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.video.vip.basics.dto.Result;
import com.video.vip.basics.util.enums.ResultEnum;
import com.video.vip.dao.LoginDAO;
import com.video.vip.dao.LoginTrailDAO;
import com.video.vip.entity.dto.passport.PassportDTO;
import com.video.vip.entity.po.LoginTrail;
import com.video.vip.entity.po.Passport;
import com.video.vip.service.LoginService;
import com.video.vip.util.enums.passport.LoginTypeEnum;
import com.video.vip.util.enums.passport.PassportOperationTypeEnum;
import com.video.vip.util.enums.passport.UserStatusEnum;
import com.video.vip.util.token.JavaWebTokenUtil;
import com.video.vip.util.token.PasswordUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private LoginDAO loginDAO;

    @Autowired
    private LoginTrailDAO loginTrailDAO;

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
        Result<PassportDTO> result = Result.newSuccess();
        try {
            Result<Passport> passportResult = getPassportByAccount(passportOperationTypeEnum,account);
            if(!passportResult.isSuccess()){
                result = Result.newResult(ResultEnum.LONGIN_PWD_ERROR,"未注册");
            }else {
                Passport passport = passportResult.getData();
                if(null==passport){
                    result = Result.newResult(ResultEnum.LONGIN_PWD_ERROR,"未注册");
                } else if (passport.getStatus()==UserStatusEnum.DISABLE.getCode()) {
                    result = Result.newResult(ResultEnum.LOGIN_DISABLE,"账号被禁用");
                } else if (passport.getStatus()==UserStatusEnum.HANDING.getCode()) {
                    result = Result.newResult(ResultEnum.LOGIN_DISABLE,"账号被冻结");
                }else {
                    PassportDTO passportDTO = new PassportDTO();
                    BeanUtils.copyProperties(passportResult.getData(),passportDTO);
                    result.setData(passportDTO);
                    //校验密码
                    Result<String> passwordCheckResult = PasswordUtil.encryption(pwdAes,passportDTO.getPasswordSalt(),false);
                    if(passwordCheckResult.isSuccess()){
                        if(passportDTO.getPassword().equals(passwordCheckResult.getData())){
                            log.debug("密码比对通过：passwordCheckResult:{}",passwordCheckResult.toJSONString());
                            LoginTrail loginTrail = new LoginTrail();
                            loginTrail.setPid(passport.getId());
                            loginTrail.setOperationType(LoginTypeEnum.TYPE_LOGIN.getCode());
                            loginTrailDAO.saveLoginTrail(loginTrail);
                            //开始生成token
                            String jsonToken = createToken(passportDTO.getId(), null, passportDTO.getPhone(), tokenExpiresMs);
                            if(StringUtils.isEmpty(jsonToken)){
                                log.warn("登录失败：jsonToken:{}",jsonToken);
                                result = Result.newResult(ResultEnum.FAIL,"");
                            }else{
                                JSONObject jsonObject = JSONObject.parseObject(jsonToken);
                                passportDTO.setToken(jsonObject.getString("token"));
                                result.setData(passportDTO);
                            }
                        }else{
                            result = Result.newResult(ResultEnum.LONGIN_PWD_ERROR,"用户名或密码错误");
                            log.warn("用户密码错误:passportDTO:{}", JSONObject.toJSONString(passportDTO));
                        }
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

    private Result<Passport> getPassportByAccount(PassportOperationTypeEnum passportOperationTypeEnum, String account) {
        Result<Passport> result = Result.newSuccess();
        Passport passportInfo = null;
        if(passportOperationTypeEnum.equals(PassportOperationTypeEnum.PHONE)){
            passportInfo = loginDAO.getPassportByPhone(account.trim());
        }else if(passportOperationTypeEnum.equals(PassportOperationTypeEnum.MAIL)){
            passportInfo = loginDAO.getPassportByMail(account.trim());
        }else if(passportOperationTypeEnum.equals(PassportOperationTypeEnum.PID)){
            passportInfo = loginDAO.getPassportById(Long.parseLong(account));
        }else{
            log.warn("目前不支持{}类型：passportOperationTypeEnum：{}",passportOperationTypeEnum.getMsg(),passportOperationTypeEnum);
            result = Result.newResult(ResultEnum.FAIL,"类型错误");
        }
        result.setData(passportInfo);
        log.info("查询的账号信息：result:{}", JSON.toJSONString(result));
        return result;
    }

    private String createToken(@NonNull Long pId, JSONObject roles, String phone, @NonNull Long tokenExpiresSecond) {
        log.info("生成token开始,pId:{},roles:{},phone:{},tokenExpiresSecond:{}", pId, roles, phone, tokenExpiresSecond);
        JSONObject jsonObject = new JSONObject();
        if (roles != null) {
            jsonObject.put("roles", roles.toJSONString());
            roles.remove("realName");
        } else {
            jsonObject.put("roles", null);
        }
        String token = JavaWebTokenUtil.createJWT(pId, roles == null ? null : roles.toJSONString(), phone, tokenExpiresSecond);
        jsonObject.put("pId", pId);
        jsonObject.put("phone", phone);
        jsonObject.put("token", token);
        log.info("生成token结束{}", jsonObject.toJSONString());
        return jsonObject.toJSONString();
    }
}
