package com.video.vip.controller.b;

import com.alibaba.fastjson.JSON;
import com.video.vip.basics.dto.Result;
import com.video.vip.basics.util.enums.ResultEnum;
import com.video.vip.entity.common.QueryPageRequest;
import com.video.vip.entity.common.QueryPageResult;
import com.video.vip.entity.dto.passport.PassportDTO;
import com.video.vip.entity.dto.user.QueryUserInfoDTO;
import com.video.vip.entity.dto.user.UserLoginOrRegisterInfoDTO;
import com.video.vip.entity.vo.UserInfoVO;
import com.video.vip.service.LoginService;
import com.video.vip.service.UserCenterService;
import com.video.vip.util.enums.UserPlatformEnum;
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
 * Created by wxn on 2020/1/14
 */
@CrossOrigin()
@RestController
@RequestMapping(value="/b/user/center")
@Slf4j
@Api(value = "BUserCenterController", tags = "B端用户中心控制类")
public class BUserCenterController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserCenterService userCenterService;

    @Autowired
    private LoginService loginService;

    @ApiOperation(value="注册登录（没有注册时先注册）")
    @RequestMapping(value="/saveUserInfo", method= RequestMethod.POST)
    public Result saveUserInfo(@RequestBody UserLoginOrRegisterInfoDTO userLoginOrRegisterInfoDTO) {
        String logStr = "注册登录（没有注册时先注册）";
        log.info("{}，开始。入参：userLoginOrRegisterInfoDTO={}", logStr, JSON.toJSONString(userLoginOrRegisterInfoDTO));
        Result result;
        if(null== userLoginOrRegisterInfoDTO || null== userLoginOrRegisterInfoDTO.getSource()
                || null== userLoginOrRegisterInfoDTO.getUserPlatform()
                || null== UserPlatformEnum.codeOf(userLoginOrRegisterInfoDTO.getUserPlatform())
                ||StringUtils.isEmpty(userLoginOrRegisterInfoDTO.getAccount())
                ||StringUtils.isEmpty(userLoginOrRegisterInfoDTO.getPwdAes())){
            result = Result.newResult(ResultEnum.PARAM_FORMAT_ERROR,ResultEnum.PARAM_FORMAT_ERROR.getMsg());
        }else {
            PassportOperationTypeEnum passportOperationTypeEnum = ApiUloginUtil.chackPassportType(userLoginOrRegisterInfoDTO.getAccount());
            result = loginService.login(passportOperationTypeEnum,userLoginOrRegisterInfoDTO.getAccount(),userLoginOrRegisterInfoDTO.getPwdAes(),ApiUloginUtil.TOKEN_OVERTIME_MS);
            if(!result.isSuccess()&&ResultEnum.REGISTER_NO.getCode().equals(result.getCode())){
                result = loginService.register(passportOperationTypeEnum,userLoginOrRegisterInfoDTO.getAccount(),userLoginOrRegisterInfoDTO.getPwdAes(),ApiUloginUtil.TOKEN_OVERTIME_MS);
                if(result.isSuccess()){
                    result = loginService.login(passportOperationTypeEnum,userLoginOrRegisterInfoDTO.getAccount(),userLoginOrRegisterInfoDTO.getPwdAes(),ApiUloginUtil.TOKEN_OVERTIME_MS);
                }
            }
            if(result.isSuccess()){
                PassportDTO  passportDTO = (PassportDTO) result.getData();
                result = userCenterService.saveUserInfo(logStr, passportDTO.getId(), userLoginOrRegisterInfoDTO);
            }
        }
        log.info("{}，结束。出参：result={}", logStr, JSON.toJSONString(result));
        return result;
    }

    @ApiOperation(value="查询用户信息列表")
    @RequestMapping(value="/queryUserList", method= RequestMethod.POST)
    public Result<QueryPageResult<UserInfoVO>> queryUserList(@RequestBody QueryPageRequest<QueryUserInfoDTO> pageRequest) {
        String logStr = "查询用户信息列表";
        log.info("{}，开始。入参：pageRequest={}", logStr, JSON.toJSONString(pageRequest));
        Result result;
        if(null==pageRequest || null==pageRequest.getData() || null==pageRequest.getData().getUserPlatform()
                || null== UserPlatformEnum.codeOf(pageRequest.getData().getUserPlatform())){
            result = Result.newResult(ResultEnum.PARAM_FORMAT_ERROR, ResultEnum.PARAM_FORMAT_ERROR.getMsg());
        }else {
            result = userCenterService.queryUserList(logStr, pageRequest);
        }
        log.info("{}，结束。出参：result={}", logStr, JSON.toJSONString(result));
        return result;
    }
}
