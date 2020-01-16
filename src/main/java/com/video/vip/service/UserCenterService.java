package com.video.vip.service;

import com.video.vip.basics.dto.Result;
import com.video.vip.entity.common.QueryPageRequest;
import com.video.vip.entity.common.QueryPageResult;
import com.video.vip.entity.dto.user.QueryUserInfoDTO;
import com.video.vip.entity.dto.user.UserLoginOrRegisterInfoDTO;
import com.video.vip.entity.vo.UserInfoVO;

/**
 * Created by wxn on 2020/1/14
 */
public interface UserCenterService {

    /**
     * 新增用户信息
     *
     * @param : logStr
     * @param : pid
     * @param : userLoginOrRegisterInfoDTO
     * @author : wxn
     * @date : 2020/1/14 17:23
     */
    Result saveUserInfo(String logStr, Long pid, UserLoginOrRegisterInfoDTO userLoginOrRegisterInfoDTO);

    /**
     * 查询用户信息列表
     *
     * @param : logStr
     * @param : pageRequest
     * @author : wxn
     * @date : 2020/1/14 18:43
     */
    Result<QueryPageResult<UserInfoVO>> queryUserList(String logStr, QueryPageRequest<QueryUserInfoDTO> pageRequest);
}
