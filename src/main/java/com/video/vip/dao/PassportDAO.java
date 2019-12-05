package com.video.vip.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.video.vip.entity.po.Passport;
import com.video.vip.mapper.PassportMapper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


/**
 * Created by wxn on 2019/11/28
 */
@Slf4j
@Service
public class PassportDAO {
    @Autowired
    private PassportMapper passportMapper;

    public Passport getPassportByPhone(@NonNull String phone){
        QueryWrapper<Passport> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone",phone);
        return passportMapper.selectOne(queryWrapper);
    }

    public Passport getPassportByMail(@NonNull String mail){
        return passportMapper.selectOne(new QueryWrapper<Passport>().eq("mail",mail));
    }

    public Passport getPassportById(@NonNull Long pid){
        return passportMapper.selectOne(new QueryWrapper<Passport>().eq("id",pid));
    }

    public int savePassport(@NonNull Passport passport){
        return passportMapper.insert(passport);
    }

    public int updateById(@NonNull Passport passport){
        passport.setUpdateDate(new Date());
        return passportMapper.updateById(passport);
    }

}
