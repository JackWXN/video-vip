package com.video.vip.locks;

import com.video.vip.basics.constant.CommonConstant;
import com.video.vip.basics.util.CoreAchieveConstants;

public class CoreLocksUtil {

    /**
     * 获取redis
     * @return
     */
    public static String getRedisKey(){
        return CoreAchieveConstants.LOCAL_IP+"-"+ CommonConstant.PROJECT_NAME;
    }
}
