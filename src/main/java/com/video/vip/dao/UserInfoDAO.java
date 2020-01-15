package com.video.vip.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.video.vip.entity.bo.UserAllInfoBO;
import com.video.vip.entity.common.QueryPageRequest;
import com.video.vip.entity.common.QueryPageResult;
import com.video.vip.entity.dto.user.QueryUserInfoDTO;
import com.video.vip.entity.po.UserInfo;
import com.video.vip.mapper.UserInfoMapper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by wxn on 2020/1/14
 */
@Slf4j
@Service
public class UserInfoDAO {
    @Autowired
    private UserInfoMapper userInfoMapper;

    /**
     * 根据pid查询用户基本信息
     *
     * @param : pid
     * @author : wxn
     * @date : 2020/1/14 17:34
     */
    public UserInfo getUserInfoByPid(@NonNull Long pid){
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pid",pid);
        return userInfoMapper.selectOne(queryWrapper);
    }

    /**
     * 新增用户信息
     *
     * @param : userInfo
     * @author : wxn
     * @date : 2020/1/14 17:51
     */
    public int insertUserInfo(UserInfo userInfo){
        return userInfoMapper.insert(userInfo);
    }

    /**
     * 查询用户列表（分页）
     *
     * @param : pageRequest
     * @author : wxn
     * @date : 2020/1/14 19:20
     */
    public QueryPageResult<UserAllInfoBO> queryUserPageList(QueryPageRequest<QueryUserInfoDTO> pageRequest){
        QueryPageResult<UserAllInfoBO> queryPageResult = new QueryPageResult<>();
        Page page = new Page();
        page.setCurrent(null==pageRequest.getPage()?1:pageRequest.getPage());
        page.setSize(null==pageRequest.getSize()?10:pageRequest.getSize());
        List<UserAllInfoBO> userInfoList = userInfoMapper.queryUserPageList(page, pageRequest.getData());
        queryPageResult.setTotalPage(page.getPages());
        queryPageResult.setList(userInfoList);
        queryPageResult.setTotal(page.getTotal());
        return queryPageResult;
    }
}
