package com.video.vip.controller.c;

import com.alibaba.fastjson.JSON;
import com.video.vip.basics.constant.CommonConstant;
import com.video.vip.basics.dto.Result;
import com.video.vip.basics.dto.UserTokenDTO;
import com.video.vip.basics.util.enums.ResultEnum;
import com.video.vip.entity.dto.user.SaveUserInfoDTO;
import com.video.vip.service.UserCenterService;
import com.video.vip.util.enums.UserSourceEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by wxn on 2020/1/14
 */
@CrossOrigin()
@RestController
@RequestMapping(value="/c/user/center")
@Slf4j
@Api(value = "CUserCenterController", tags = "C端用户中心控制类")
public class CUserCenterController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserCenterService userCenterService;

    @ApiOperation(value="新增用户信息",response = Result.class)
    @RequestMapping(value="/saveUserInfo", method= RequestMethod.POST)
    public Result saveUserInfo(@RequestBody SaveUserInfoDTO saveUserInfoDTO) {
        String logStr = "新增用户信息";
        log.info("{}，开始。入参：saveUserInfoDTO={}", logStr, JSON.toJSONString(saveUserInfoDTO));
        Result result;
        if(null==saveUserInfoDTO || null==saveUserInfoDTO.getSource() || null==saveUserInfoDTO.getUserPlatform()){
            result = Result.newResult(ResultEnum.PARAM_FORMAT_ERROR, ResultEnum.PARAM_FORMAT_ERROR.getMsg());
        }else if(UserSourceEnum.USER_SOURCE2.getCode() == saveUserInfoDTO.getSource() && StringUtils.isEmpty(saveUserInfoDTO.getReferrerPid())){
            result = Result.newResult(ResultEnum.PARAM_FORMAT_ERROR, ResultEnum.PARAM_FORMAT_ERROR.getMsg());
        }else {
            UserTokenDTO userTokenDTO = (UserTokenDTO) request.getAttribute(CommonConstant.USER_TOKEN_KEY);
            result = userCenterService.saveUserInfo(logStr, userTokenDTO.getPid(), saveUserInfoDTO);
        }
        log.info("{}，结束。出参：result={}", logStr, JSON.toJSONString(result));
        return result;
    }
}
