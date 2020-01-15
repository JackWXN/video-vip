package com.video.vip.service.impl;

import com.video.vip.basics.dto.Result;
import com.video.vip.basics.id.IdWork;
import com.video.vip.basics.util.basics.DateUtil;
import com.video.vip.basics.util.enums.ResultEnum;
import com.video.vip.dao.PassportDAO;
import com.video.vip.dao.UserInfoDAO;
import com.video.vip.entity.bo.UserAllInfoBO;
import com.video.vip.entity.common.QueryPageRequest;
import com.video.vip.entity.common.QueryPageResult;
import com.video.vip.entity.dto.user.QueryUserInfoDTO;
import com.video.vip.entity.dto.user.SaveUserInfoDTO;
import com.video.vip.entity.po.Passport;
import com.video.vip.entity.po.UserInfo;
import com.video.vip.entity.vo.UserInfoVO;
import com.video.vip.locks.redis.LockService;
import com.video.vip.service.UserCenterService;
import com.video.vip.util.enums.UserPlatformEnum;
import com.video.vip.util.enums.UserSourceEnum;
import com.video.vip.util.enums.VipStatusEnum;
import com.video.vip.util.enums.VipTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wxn on 2020/1/14
 */
@Slf4j
@Service("userCenterService")
public class UserCenterServiceImpl implements UserCenterService {
    @Autowired
    private UserInfoDAO userInfoDAO;

    @Autowired
    private PassportDAO passportDAO;

    @Override
    public Result saveUserInfo(String logStr, Long pid, SaveUserInfoDTO saveUserInfoDTO){
        final String key = "saveUserInfo_" + pid;
        if (LockService.addTimeLock(key, 60)) {
            try{
                UserInfo userInfo = userInfoDAO.getUserInfoByPid(pid);
                if(null != userInfo){
                    return Result.newSuccess();
                }
                userInfo = new UserInfo();
                userInfo.setPid(pid);
                userInfo.setSource(saveUserInfoDTO.getSource());
                userInfo.setReferrerPid(saveUserInfoDTO.getReferrerPid());
                userInfo.setVipStatus(VipStatusEnum.VIP_STATUS1.getCode());
                userInfo.setUserPlatform(saveUserInfoDTO.getUserPlatform());
                userInfo.setId(IdWork.getId());
                userInfo.setCreateDate(new Date());
                userInfo.setUpdateDate(new Date());
                userInfo.setVersion(0);
                int num = userInfoDAO.insertUserInfo(userInfo);
                if(num < 1){
                    return Result.newResult(ResultEnum.FAIL, "保存数据失败");
                }
                return Result.newSuccess();
            }catch (Exception e){
                log.error("{},接口异常。", logStr, e);
                return Result.newResult(ResultEnum.EXCEPTION, ResultEnum.EXCEPTION.getMsg());
            }finally {
                LockService.delTimeLock(key);
            }
        }else {
            return Result.newResult(ResultEnum.SUCCESS, "操作正在进行，请稍后");
        }
    }

    @Override
    public Result<QueryPageResult<UserInfoVO>> queryUserList(String logStr, QueryPageRequest<QueryUserInfoDTO> pageRequest){
        try{
            QueryPageResult<UserAllInfoBO> queryPageResult = userInfoDAO.queryUserPageList(pageRequest);
            List<UserAllInfoBO> userAllInfoBoList = null==queryPageResult.getList()?new ArrayList<>():queryPageResult.getList();

            QueryPageResult<UserInfoVO> voQueryPageResult = new QueryPageResult<UserInfoVO>();
            voQueryPageResult.setList(getUserInfoVoList(userAllInfoBoList));
            voQueryPageResult.setTotal(queryPageResult.getTotal());
            voQueryPageResult.setTotalPage(queryPageResult.getTotalPage());
            return Result.newSuccess(voQueryPageResult);
        }catch (Exception e){
            log.error("{},接口异常。", logStr, e);
            return Result.newResult(ResultEnum.EXCEPTION, ResultEnum.EXCEPTION.getMsg());
        }
    }

    private List<UserInfoVO> getUserInfoVoList(List<UserAllInfoBO> userAllInfoList){
        List<UserInfoVO> userInfoVoList = new ArrayList<>();
        for (UserAllInfoBO userAllInfoBo:userAllInfoList){
            UserInfoVO userInfoVO = new UserInfoVO();
            userInfoVO.setId(userAllInfoBo.getId().toString());
            userInfoVO.setPid(userAllInfoBo.getPid().toString());
            userInfoVO.setPhone(userAllInfoBo.getPhone());
            userInfoVO.setRegistrationDate(null==userAllInfoBo.getCreateDate()?"":DateUtil.getDateTime(userAllInfoBo.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
            UserSourceEnum userSourceEnum = UserSourceEnum.codeOf(userAllInfoBo.getSource());
            userInfoVO.setSource(null==userSourceEnum?"":userSourceEnum.getMsg());
            String referrerPhone = "";
            if(null!=userAllInfoBo.getReferrerPid()){
                Passport passport = passportDAO.getPassportById(userAllInfoBo.getReferrerPid());
                referrerPhone = null==passport?"":passport.getPhone();
            }
            userInfoVO.setReferrerPhone(referrerPhone);
            VipStatusEnum vipStatusEnum = VipStatusEnum.codeOf(userAllInfoBo.getVipStatus());
            userInfoVO.setVipStatus(null==vipStatusEnum?"":vipStatusEnum.getMsg());
            VipTypeEnum vipTypeEnum = VipTypeEnum.codeOf(userAllInfoBo.getVipType());
            userInfoVO.setVipType(null==vipTypeEnum?"":vipTypeEnum.getMsg());
            userInfoVO.setVipStartDate(null==userAllInfoBo.getVipStartDate()?"":DateUtil.getDateTime(userAllInfoBo.getVipStartDate(), "yyyy-MM-dd HH:mm:ss"));
            userInfoVO.setVipEndDate(null==userAllInfoBo.getVipEndDate()?"":DateUtil.getDateTime(userAllInfoBo.getVipEndDate(), "yyyy-MM-dd HH:mm:ss"));
            UserPlatformEnum userPlatformEnum = UserPlatformEnum.codeOf(userAllInfoBo.getUserPlatform());
            userInfoVO.setUserPlatform(null==userPlatformEnum?"":userPlatformEnum.getMsg());
            userInfoVoList.add(userInfoVO);
        }
        return userInfoVoList;
    }
}
