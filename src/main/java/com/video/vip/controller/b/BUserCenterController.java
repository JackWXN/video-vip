package com.video.vip.controller.b;

import com.alibaba.fastjson.JSON;
import com.video.vip.basics.constant.CommonConstant;
import com.video.vip.basics.dto.Result;
import com.video.vip.basics.dto.UserTokenDTO;
import com.video.vip.basics.util.enums.ResultEnum;
import com.video.vip.entity.common.QueryPageRequest;
import com.video.vip.entity.common.QueryPageResult;
import com.video.vip.entity.dto.user.QueryUserInfoDTO;
import com.video.vip.entity.dto.user.SaveUserInfoDTO;
import com.video.vip.entity.vo.UserInfoVO;
import com.video.vip.service.UserCenterService;
import com.video.vip.util.enums.UserPlatformEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @ApiOperation(value="新增用户信息")
    @RequestMapping(value="/saveUserInfo", method= RequestMethod.POST)
    public Result saveUserInfo(@RequestBody SaveUserInfoDTO saveUserInfoDTO) {
        String logStr = "新增用户信息";
        log.info("{}，开始。入参：saveUserInfoDTO={}", logStr, JSON.toJSONString(saveUserInfoDTO));
        Result result;
        if(null==saveUserInfoDTO || null==saveUserInfoDTO.getSource()
                || null==saveUserInfoDTO.getUserPlatform() || null== UserPlatformEnum.codeOf(saveUserInfoDTO.getUserPlatform())){
            result = Result.newResult(ResultEnum.PARAM_FORMAT_ERROR, ResultEnum.PARAM_FORMAT_ERROR.getMsg());
        }else {
            UserTokenDTO userTokenDTO = (UserTokenDTO) request.getAttribute(CommonConstant.USER_TOKEN_KEY);
            result = userCenterService.saveUserInfo(logStr, userTokenDTO.getPid(), saveUserInfoDTO);
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
