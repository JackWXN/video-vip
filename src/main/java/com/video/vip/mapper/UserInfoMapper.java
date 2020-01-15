package com.video.vip.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.video.vip.entity.bo.UserAllInfoBO;
import com.video.vip.entity.dto.user.QueryUserInfoDTO;
import com.video.vip.entity.po.UserInfo;

import java.util.List;

/**
 * Created by wxn on 2020/1/14
 */
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    List<UserAllInfoBO> queryUserPageList(Page page, QueryUserInfoDTO queryUserInfoDTO);
}
