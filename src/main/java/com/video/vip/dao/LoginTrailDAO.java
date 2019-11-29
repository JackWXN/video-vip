package com.video.vip.dao;

import com.video.vip.entity.po.LoginTrail;
import com.video.vip.mapper.LoginTrailMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Created by wxn on 2019/11/28
 */
@Slf4j
public class LoginTrailDAO {
    @Autowired
    private LoginTrailMapper loginTrailMapper;

    public int saveLoginTrail(LoginTrail loginTrail){
        return loginTrailMapper.insert(loginTrail);
    }

}
