package com.video.vip.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.video.vip.entity.po.Passport;
import com.video.vip.mapper.LoginMapper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Created by wxn on 2019/11/28
 */
@Slf4j
public class LoginDAO {
    @Autowired
    private LoginMapper loginMapper;

    public Passport getPassportByPhone(@NonNull String phone){
        return loginMapper.selectOne(new QueryWrapper<Passport>().eq("phone",phone));
    }

    public Passport getPassportByMail(@NonNull String mail){
        return loginMapper.selectOne(new QueryWrapper<Passport>().eq("mail",mail));
    }

    public Passport getPassportById(@NonNull Long pid){
        return loginMapper.selectOne(new QueryWrapper<Passport>().eq("id",pid));
    }
}
