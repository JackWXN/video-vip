package com.video.vip.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.video.vip.basics.dto.Result;
import com.video.vip.basics.util.basics.DateUtil;
import com.video.vip.basics.util.enums.ResultEnum;
import com.video.vip.basics.util.enums.YesOrNoEnum;
import com.video.vip.basics.util.pwd.PassportConstantUtil;
import com.video.vip.basics.util.pwd.PhoneUtil;
import com.video.vip.dao.LoginTrailDAO;
import com.video.vip.dao.PassportDAO;
import com.video.vip.entity.dto.passport.PassportDTO;
import com.video.vip.entity.po.LoginTrail;
import com.video.vip.entity.po.Passport;
import com.video.vip.locks.redis.LockService;
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

import java.util.Date;
import java.util.Map;

/**
 * 帐号登录相关操作实现
 *
 * @author wxn
 */
@Slf4j
@Service("loginService")
public class LoginServiceImpl implements LoginService {
    @Autowired
    private PassportDAO passportDAO;

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
    public Result register(@NonNull PassportOperationTypeEnum passportOperationTypeEnum, @NonNull String account, @NonNull String pwdAes, @NonNull Long tokenExpiresMs){
        log.info("注册开始：passportOperationTypeEnum:{},account：{}，pwdAes：{},tokenExpiresMs:{}",passportOperationTypeEnum,account,pwdAes,tokenExpiresMs);
        Result<PassportDTO> result = Result.newSuccess();
        final String lockKey = "register-pp-RgC9sGRf9Rjfs0FCRHw-" + account.trim();
        if (LockService.addTimeLock(lockKey, PassportConstantUtil.LOCK_REDIS_REGISTER_SECOND)) {
            try {
                String pwdSalt = PasswordUtil.generatePwdSalt();
                Result<String> passwordCheckResult = PasswordUtil.encryption(pwdAes,pwdSalt,true);
                if(!passwordCheckResult.isSuccess()){
                    result = Result.newResult(ResultEnum.FAIL,"密码不符合要求");
                }else {
                    Passport passport = new Passport();
                    passport.setPasswordSalt(pwdSalt);
                    passport.setPassword(passwordCheckResult.getData());

                    Result<Passport> passportResult = getPassportByAccount(passportOperationTypeEnum,account);
                    if(!passportResult.isSuccess()){
                        result = Result.newResult(passportResult.getCode(),passportResult.getMessage());
                    } else {
                        if(null==passportResult.getData()){
                            passport.setAccount(account.trim());
                            if(passportOperationTypeEnum.equals(PassportOperationTypeEnum.MAIL)){
                                passport.setMail(account.trim());
                            }else if(passportOperationTypeEnum.equals(PassportOperationTypeEnum.PHONE)){
                                passport.setPhone(account.trim());
                            }else {
                                log.warn("目前不支持{}+密码类型注册：passportOperationTypeEnum：{}",passportOperationTypeEnum.getMsg(),passportOperationTypeEnum);
                                result = Result.newResult(ResultEnum.FAIL,"目前不支持"+passportOperationTypeEnum.getMsg()+"+密码类型注册");
                            }
                            passport.setStatus(UserStatusEnum.ENABLE.getCode());
                            int intAdd = passportDAO.savePassport(passport);
                            if(intAdd==0){
                                result = Result.newResult(ResultEnum.REGISTER_ERROR,"注册账号失败");
                                log.error("根据密码注册错误，数据库影响条数为0：intAdd:{},passport:{}",intAdd, JSONObject.toJSONString(passport));
                            }
                        } else if (passportResult.getData().getStatus()==UserStatusEnum.DISABLE.getCode()) {
                            result = Result.newResult(ResultEnum.LOGIN_DISABLE,"账号被禁用,请联系管理员");
                        } else if (passportResult.getData().getStatus()==UserStatusEnum.HANDING.getCode()) {
                            result = Result.newResult(ResultEnum.LOGIN_DISABLE,"账号被冻结,请联系管理员");
                        }else {
                            result = Result.newResult(ResultEnum.FAIL,"账号已存在");
                        }
                    }
                }
            }catch (Exception e){
                log.error("注册异常。",e);
                result = Result.newResult(ResultEnum.FAIL,ResultEnum.FAIL.getMsg());
            }finally {
                LockService.delTimeLock(lockKey);
            }
        } else {
            result = Result.newResult(ResultEnum.FAIL,"请不要频繁操作");
        }
        log.info("注册结束：返回：{}", result.toJSONString());
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
                    BeanUtils.copyProperties(passport,passportDTO);
                    result.setData(passportDTO);
                    //校验密码
                    Result<String> passwordCheckResult = PasswordUtil.encryption(pwdAes,passportDTO.getPasswordSalt(),true);
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
            passportInfo = passportDAO.getPassportByPhone(account.trim());
        }else if(passportOperationTypeEnum.equals(PassportOperationTypeEnum.MAIL)){
            passportInfo = passportDAO.getPassportByMail(account.trim());
        }else if(passportOperationTypeEnum.equals(PassportOperationTypeEnum.PID)){
            passportInfo = passportDAO.getPassportById(Long.parseLong(account));
        }else{
            log.warn("目前不支持{}类型：passportOperationTypeEnum：{}",passportOperationTypeEnum.getMsg(),passportOperationTypeEnum);
            result = Result.newResult(ResultEnum.FAIL,"账号不符合要求");
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
        if(!StringUtils.isEmpty(phone)){
            phone = PhoneUtil.encryption(phone);
        }
        String token = JavaWebTokenUtil.createJWT(pId, roles == null ? null : roles.toJSONString(), phone, tokenExpiresSecond);
        jsonObject.put("pId", pId);
        jsonObject.put("phone", phone);
        jsonObject.put("token", token);
        log.info("生成token结束{}", jsonObject.toJSONString());
        return jsonObject.toJSONString();
    }

    @Override
    public Result updateOldPassword(String logStr, PassportOperationTypeEnum passportOperationTypeEnum, String account, String newPasswordAes, String oldPasswordAes, YesOrNoEnum isReadyPwd, Map<String,Object> params){
        log.info("{}。业务处理开始:passportOperationTypeEnum:{},account:{},newPasswordAes:{},oldPasswordAes:{},isReadyPwd:{}",logStr,passportOperationTypeEnum,account,newPasswordAes,oldPasswordAes,isReadyPwd);
        try{
            Result<Passport> passportResult = getPassportByAccount(passportOperationTypeEnum,account);
            if(!passportResult.isSuccess()||passportResult.getData()==null){
                return Result.newResult(ResultEnum.FAIL,"修改失败，对应的账号不存在");
            }
            Passport passport = passportResult.getData();
            //验证密码是否可用
            Result<String> newPasswordCheckResult = PasswordUtil.encryption(newPasswordAes,null,true);
            if(!newPasswordCheckResult.isSuccess()){
                return Result.newResult(ResultEnum.FAIL,"密码不符合要求");
            }
            log.debug("传递了老密码，校验老密码是否正确:oldPasswordAes:{}",oldPasswordAes);
            Result<String> oldPasswordCheckResult = PasswordUtil.encryption(oldPasswordAes,passport.getPasswordSalt(),false);
            if(!oldPasswordCheckResult.isSuccess()){
                return Result.newResult(ResultEnum.FAIL,"旧密码不正确");
            }
            String oldPassword = oldPasswordCheckResult.getData();
            if(!passport.getPassword().equals(oldPassword)){
                log.warn("密码错误，不允许修改：passport:{},oldPassword:{}",JSONObject.toJSONString(passport),oldPassword);
                return Result.newResult(ResultEnum.FAIL,"账号或密码错误");
            }
            Result updatePasswordResult = updatePasswordByPassport(passport,newPasswordAes,isReadyPwd);
            if(!updatePasswordResult.isSuccess()){
                return Result.newResult(ResultEnum.FAIL,"密码修改失败");
            }
            return Result.newSuccess();
        }catch (Exception e){
            log.error("{}，异常。",logStr,e);
            return Result.newResult(ResultEnum.EXCEPTION,"接口异常");
        }
    }


    /**
     * 根据pid修改密码
     *
     * @param : passport   查询出来的最新的账号信息
     * @param : password   要修改的密码
     * @param : isReadyPwd 是否进行预处理密码，如果是是，则不直接修改主密码
     * @author : wxn
     * @date : 2019/12/4 17:37
     */
    private Result updatePasswordByPassport(@NonNull Passport passport,@NonNull String password,@NonNull YesOrNoEnum isReadyPwd) {
        log.info("根据pid修改密码开始:passport:{},password:{}",JSONObject.toJSONString(passport),password);
        Result result = verifyEditPassword(passport);
        if(result.isSuccess()){
            String pwdSalt = PasswordUtil.generatePwdSalt();
            Result<String> passwordCheckResult = PasswordUtil.encryption(password,pwdSalt,true);
            if(passwordCheckResult.isSuccess()){
                int intEdit = 0;
                if(isReadyPwd.equals(YesOrNoEnum.YES)){
                    log.debug("执行预处理保存密码");
                    Passport editPassport = new Passport();
                    editPassport.setId(passport.getId());
                    editPassport.setPpReadyPwd(passwordCheckResult.getData());
                    editPassport.setPpReadyPwdSalt(pwdSalt);
                    intEdit = passportDAO.updateById(editPassport);
                }else{
                    log.debug("执行保存主密码");
                    Passport editPassport = new Passport();
                    editPassport.setId(passport.getId());
                    editPassport.setPassword(passwordCheckResult.getData());
                    editPassport.setPasswordSalt(pwdSalt);
                    intEdit = passportDAO.updateById(editPassport);
                }
                if(intEdit<1){
                    log.warn("修改密码错误，数据库影响条数为0：intEdit:{},passport:{}",intEdit,JSONObject.toJSONString(passport));
                    result = Result.newResult(ResultEnum.FAIL,"修改密码失败");
                }
            }
        }
        log.info("根据pid修改密码结束:result:{}",result.toJSONString());
        return result;
    }

    /**
     * 验证是否可以修改密码
     *
     * @param : passport 查询出来的最新的账号信息
     * @author : wxn
     * @date : 2019/12/4 17:24
     */
    private Result verifyEditPassword(@NonNull Passport passport) {
        log.info("验证是否可以修改密码开始:passport:{}",JSON.toJSONString(passport));
        Result result = Result.newSuccess();
        //当天的时间
        String strNowDate = DateUtil.getDateTime(new Date(),"yyyyMMdd");
        Passport editPassport = new Passport();
        editPassport.setId(passport.getId());
        //修改值
        int intEdit = 0;
        //判断当天是否尝试过修改密码
        if(passport.getPasswordEditDate()==null||!passport.getPasswordEditDate().equals(strNowDate)){
            editPassport.setPasswordEditDate(strNowDate);
            editPassport.setPasswordEditDateCount(1);
            editPassport.setPasswordEditResetCount(0);
            intEdit = passportDAO.updateById(editPassport);
        }else if(passport.getPasswordEditDateCount()>=PassportConstantUtil.EDIT_TODAY_PWD_MAX_COUNT){
            log.warn("当天尝试修改密码次数超限:passwordEditDateCount:{},passport:{}",PassportConstantUtil.EDIT_TODAY_PWD_MAX_COUNT, JSONObject.toJSONString(passport));
            result = Result.newResult(ResultEnum.FAIL,"请明天再修改密码，或联系管理员");
        }else{
            log.debug("验证通过，尝试修改密码次数+1");
            editPassport.setPasswordEditDateCount(passport.getPasswordEditDateCount()==null?0:(passport.getPasswordEditDateCount()+1));
            intEdit = passportDAO.updateById(editPassport);
        }
        if(result.isSuccess()&&intEdit==0){
            log.warn("验证是否可以修改密码初始化时间和次数失败,数据库影响条数为0:intEdit:{},passport:{}",intEdit, JSONObject.toJSONString(passport));
        }
        return result;
    }

    @Override
    public Result resetTodayEditPwdCount(String logStr, @NonNull PassportOperationTypeEnum passportOperationTypeEnum, @NonNull String account){
        log.info("业务处理{}开始:passportOperationTypeEnum:{},account:{}",logStr,passportOperationTypeEnum,account);
        try{
            Result<Passport> passportResult = getPassportByAccount(passportOperationTypeEnum,account);
            if(!passportResult.isSuccess()||passportResult.getData()==null){
                return Result.newResult(ResultEnum.FAIL,"重置失败，对应的账号不存在");
            }
            //当天的时间
            String strNowDate = DateUtil.getDateTime(new Date(),"yyyyMMdd");
            Passport editPassport = new Passport();
            int intEdit = 0;
            if(passportResult.getData().getPasswordEditDate()==null||!passportResult.getData().getPasswordEditDate().equals(strNowDate)){
                editPassport.setId(passportResult.getData().getId());
                editPassport.setPasswordEditDate(strNowDate);
                editPassport.setPasswordEditDateCount(0);
                editPassport.setPasswordEditResetCount(0);
                intEdit = passportDAO.updateById(editPassport);
            }else if(passportResult.getData().getPasswordEditDateCount()!=null&&passportResult.getData().getPasswordEditDateCount()<PassportConstantUtil.EDIT_TODAY_PWD_MAX_COUNT){
                log.warn("当日尝试修改密码次数未使用完成，不允许重置：passport:{}",JSONObject.toJSONString(passportResult.getData()));
                return Result.newResult(ResultEnum.FAIL,"重置失败，当日修改密码次数未用尽。当天剩余尝试修改密码次数"+(PassportConstantUtil.EDIT_TODAY_PWD_MAX_COUNT-passportResult.getData().getPasswordEditDateCount()));
            }else if(passportResult.getData().getPasswordEditResetCount()!=null&&passportResult.getData().getPasswordEditResetCount()>=PassportConstantUtil.EDIT_PWD_MAX_COUNT_RESET_MAX_COUNT){
                return Result.newResult(ResultEnum.FAIL,"重置失败，当日重置次数用尽");
            }else{
                editPassport.setId(passportResult.getData().getId());
                editPassport.setPasswordEditDateCount(0);
                editPassport.setPasswordEditResetCount(passportResult.getData().getPasswordEditResetCount()==null? 1 :(passportResult.getData().getPasswordEditResetCount()+1));
                intEdit = passportDAO.updateById(editPassport);
            }
            if(intEdit<1){
                return Result.newResult(ResultEnum.FAIL,"重置失败");
            }
            return Result.newSuccess("重置成功，当天剩余重置次数"+(PassportConstantUtil.EDIT_PWD_MAX_COUNT_RESET_MAX_COUNT-editPassport.getPasswordEditResetCount()));
        }catch (Exception e){
            log.error("{},接口异常。",logStr,e);
            return Result.newResult(ResultEnum.FAIL,ResultEnum.FAIL.getMsg());
        }
    }
}
