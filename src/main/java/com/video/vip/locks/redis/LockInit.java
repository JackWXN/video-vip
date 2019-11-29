package com.video.vip.locks.redis;

import com.video.vip.basics.constant.CommonConstant;
import com.video.vip.basics.exception.SysErrorExeception;
import com.video.vip.basics.util.CoreAchieveConstants;
import com.video.vip.basics.util.basics.DateUtil;
import com.video.vip.locks.CoreLocksUtil;
import com.video.vip.locks.JedisPoolClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;

import java.net.InetAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * redis锁初始化
 * @author wxn
 */
@Slf4j
public class LockInit {

    public static boolean initRedsMac() {
        Jedis jedis = null;
        try{
            jedis = JedisPoolClient.getJedis();
            jedis.hget(CoreLocksUtil.getRedisKey(), "START_LOCAL_PATH");
        } catch (Exception e) {
            log.error("初始化锁信息失败", e);
            JedisPoolClient.close(jedis);
            jedis = null;
        }
        if(jedis==null){
            return false;
        }
        try {
            log.info("当前项目注册key：" + CoreLocksUtil.getRedisKey());
            // 获得redis中注册的服务启动本地路径
            String strOP = jedis.hget(CoreLocksUtil.getRedisKey(), "START_LOCAL_PATH");
            // 将系统锁定记录计入到LockConstants.LOCK_SERVER_KEY中
            jedis.hset(CoreAchieveConstants.LOCK_SERVER_KEY, CoreLocksUtil.getRedisKey(), CoreAchieveConstants.SERVER_START_CODE);
            Map<String, String> map = new HashMap<String, String>();
            map.put("START_LOCAL_PATH", CommonConstant.PROJECT_PATCH);
            map.put("PROJECT_NAME", CommonConstant.PROJECT_NAME);
            map.put("PROJECT_SYS_CODE", CommonConstant.PROJECT_NAME);
            map.put("SERVER_START_CODE", CoreAchieveConstants.SERVER_START_CODE);
            map.put("IP", InetAddress.getLocalHost().getHostAddress().toString());
            map.put("REGISTER_DATE", DateUtil.getDateTime(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));
            jedis.hmset(CoreLocksUtil.getRedisKey(), map);
            if (StringUtils.isBlank(jedis.hget(CoreAchieveConstants.LOCK_SERVER_KEY, CoreLocksUtil.getRedisKey()))) {
                log.error("严重错误：初始化时向redis初始化本次服务的唯一标识出错!");
                throw new SysErrorExeception();
            }
        } catch (Exception e) {
            log.error("严重错误：初始化时向redis初始化本次服务的唯一标识出错", e);
            throw new SysErrorExeception();
        } finally {
            JedisPoolClient.close(jedis);
        }
        return true;
    }
}
