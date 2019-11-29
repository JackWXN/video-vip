package com.video.vip.locks.redis;

import com.video.vip.basics.util.CoreAchieveConstants;
import com.video.vip.basics.util.basics.DateUtil;
import com.video.vip.basics.util.basics.StringUtil;
import com.video.vip.locks.CoreLocksUtil;
import com.video.vip.locks.JedisPoolClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * redis锁定管理
 * @author wxn
 */
@Slf4j
public class LockService  {
    /**
     * 添加时间锁，效率比较高
     * （适合短时间使用，超过60秒要使用短锁，因为此锁如果执行解锁失败，只能等过期后才能结束）
     * @param key 锁的唯一标识（必须全redis唯一，否则会认为锁定）
     * @param expireTime 设置的超时时间(单位：秒)
     * @return 上锁成功返回true，已经被锁定或上锁失败返回false
     */
    public static boolean addTimeLock(String key,Integer expireTime) {
        if(StringUtils.isBlank(key)){
            log.warn("添加锁定错误：key不能为空{}",key);
            return false;
        }
        boolean boolRetu = false;
        try {
            String strKey = "lock-" + StringUtil.getNotNull(key);
            boolRetu = setRedisTimeLock(strKey,expireTime);
        } catch (Exception ee) {
            log.error("添加时间锁出现异常：{},{}", ee.getClass(),ee.getMessage(),ee);
            boolRetu = false;
        }
        return boolRetu;
    }

    /**
     * 添加短锁，慎用，效率没有时间锁高（默认3天才会过期）。要在finally里结束
     * 加锁的服务重启时，当台服务加了没解的锁将失效
     * 短锁的特性是，明确的告知了锁可以解才能解
     * @param key 调用锁的当前应用LockConstants类里的SERVER_START_CODE
     * @return 上锁成功返回true，已经被锁定或上锁失败返回false
     */
    public static boolean addShortLock(String key) {
        boolean bool = false;
        if(StringUtils.isBlank(key)){
            log.warn("添加锁定错误：key不能为空{}",key);
            return false;
        }
        Jedis jedis = null;
        try {
            String strKey = "lock-" + StringUtil.getNotNull(key);
            // 如果没有被锁定
            if (setRedisShortLock(strKey,null)) {
                bool = true;
            } else {
                jedis = JedisPoolClient.getJedis();
                String lockCode = "del_lock_overdue";
                long acquired = jedis.setnx(lockCode, "1");
                // 设置20秒超时
                jedis.expire(lockCode, 20);
                // 对短锁操作过程进行锁定，1秒过期
                if (acquired == 1) {
                    try {
                        //判断是否已经加锁
                        String serverValueLock = jedis.get(strKey);
                        if (StringUtils.isBlank(serverValueLock)) {
                            //未被加锁
                            bool = false;
                        }else{
                            // 下标0代表锁定该值的服务器唯一标识key（ip地址+项目名称），下标1代表上锁时该服务key对应的服务每次启动的唯一标识
                            String[] svls = serverValueLock.split(";");
                            // 获得当前的服务器唯一标识key对应的唯一标识
                            String strServerValue = jedis.hget(CoreAchieveConstants.LOCK_SERVER_KEY, svls[0]);
                            // 根据key查询当前服务器唯一标识
                            // 上锁时对应的当前服务器唯一标识
                            if (StringUtils.isNotBlank(strServerValue)
                                    && StringUtils.isNotBlank(svls[1])) {
                                // 如果当前跟上锁时的服务器唯一标识不相等，则证明该key锁定时的服务器已经重启过了，则可以删除对应的锁
                                if (!strServerValue.equals(svls[1])) {
                                    jedis.del(strKey);
                                    bool = setRedisShortLock(strKey, null);
                                }
                            }
                        }
                    } catch (Exception ee) {
                        log.error("redis加锁出现异常：{},{}",ee.getClass(),ee.getMessage(),ee);
                        bool = false;
                    } finally {
                        jedis.del(lockCode);
                    }
                }
            }
        } catch (Exception ee) {
            log.error("短锁启动出现异常", ee);
            bool = false;
        } finally {
            JedisPoolClient.close(jedis);
        }
        return bool;
    }

    /**
     * 删除时间锁
     * @param key
     * @return
     */
    public static boolean delTimeLock(String key) {
        boolean bool = false;
        if(StringUtils.isBlank(key)){
            log.warn("添加锁定错误：key不能为空{}",key);
            return false;
        }
        Jedis jedis = null;
        String strKey = "lock-" + StringUtil.getNotNull(key);
        try {
            jedis = JedisPoolClient.getJedis();
            jedis.del(strKey);
            bool = true;
        } catch (Exception ee) {
            log.error("删除锁出现异常：{},{}", ee.getClass(),ee.getMessage(),ee);
            bool = false;
        } finally {
            JedisPoolClient.close(jedis);
        }
        return bool;
    }
    /**
     * 删除短锁，和addShortLock配套使用
     * @param key 锁的唯一标识（必须全redis唯一）
     * @return
     */
    public static boolean delShortLock(String key) {
        boolean bool = false;
        if(StringUtils.isBlank(key)){
            log.warn("添加锁定错误：key不能为空{}",key);
            return false;
        }
        Jedis jedis = null;
        String strKey = "lock-" + StringUtil.getNotNull(key);
        try {
            jedis = JedisPoolClient.getJedis();
            jedis.del(strKey);
            // 每台服务器当前存在的锁（目前包括过期删除了的锁）
            jedis.hdel(CoreLocksUtil.getRedisKey() + "_sLock", strKey);
            jedis.del(strKey + "_sLock");
            bool = true;
        } catch (Exception ee) {
            log.error("删除锁出现异常{},{}", ee.getClass(),ee.getMessage(),ee);
            bool = false;
        } finally {
            JedisPoolClient.close(jedis);
        }
        return bool;
    }

    /**
     * 设置锁
     * @param strKey 锁的唯一标识（必须全redis唯一，否则会认为锁定）
     * @param runTime 设置的超时时间(单位：秒)
     * @return 上锁成功返回true，已经被锁定或上锁失败返回false
     */
    private static boolean setRedisTimeLock(String strKey, Integer runTime) throws Exception {
        Date dateNow = new Date();
        boolean bool = false;
        Jedis jedis = null;
        // 默认超时时间
        int intExpireTime = 8;
        if (runTime != null && runTime > 0) {
            intExpireTime = runTime;
        }
        try{
            jedis = JedisPoolClient.getJedis();
            long acquired = jedis.setnx(strKey,intExpireTime+"");
            log.debug("超时间为：" + intExpireTime + "秒");
            // 上锁成功
            if (acquired == 1) {
                //设置超时时间
                jedis.expire(strKey, intExpireTime);
                bool = true;
            }
        } catch (Exception ee) {
            log.error("{},{}", ee.getClass(),ee.getMessage(),ee);
            bool = false;
        } finally {
            JedisPoolClient.close(jedis);
        }
        return bool;
    }

    private static boolean setRedisShortLock(String strKey, Integer runTime) throws Exception {
        Date dateNow = new Date();
        boolean bool = false;
        Jedis jedis = null;
        // 默认超时时间
        int intExpireTime = 3 * 24 * 60 * 60;
        if (runTime != null && runTime > 0) {
            intExpireTime = runTime;
        }
        log.debug("超时间为：" + intExpireTime + "秒");
        try{
            jedis = JedisPoolClient.getJedis();
            long acquired = jedis.setnx(strKey, CoreLocksUtil.getRedisKey() + ";" + CoreAchieveConstants.SERVER_START_CODE);
            // 如果没有被锁定
            if (acquired == 1) {
                // 设置3天超时
                jedis.expire(strKey, intExpireTime);
                Map<String, String> mapSL = new HashMap<String, String>();
                // 锁定时间
                mapSL.put("lockDate", DateUtil.getDateTime(dateNow, "yyyy-MM-dd HH:mm:ss.SSS"));
                // 锁定时的服务器唯一编码
                mapSL.put("serverValue", CoreAchieveConstants.SERVER_START_CODE);
                mapSL.put("expire", intExpireTime + "");
                // 每台服务器当前存在的锁（目前包括过期删除了的锁），设置当前锁备注，值为过期时间
                jedis.hset(CoreLocksUtil.getRedisKey() + "_sLock", strKey, DateUtil.getDateTime(DateUtil.getAddMillisecond(dateNow, intExpireTime * 1000), "yyyyMMddHHmmss"));
                jedis.expire(CoreLocksUtil.getRedisKey() + "_sLock", intExpireTime);
                jedis.hmset(strKey + "_sLock", mapSL);
                jedis.expire(strKey + "_sLock", intExpireTime);
                bool = true;
            }
        } catch (Exception ee) {
            log.error("{},{}", ee.getClass(),ee.getMessage(),ee);
            bool = false;
        } finally {
            JedisPoolClient.close(jedis);
        }
        return bool;
    }
}
