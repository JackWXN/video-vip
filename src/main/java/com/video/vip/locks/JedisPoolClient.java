package com.video.vip.locks;

import com.video.vip.basics.exception.SysErrorExeception;
import com.video.vip.basics.util.properties.ProperServicesUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * jedis连接池
 * @author 何智琦
 */
@Slf4j
public class JedisPoolClient {

    private static JedisPool pool = null;
    /**
     *
     * 方法描述 构建redis连接池
     */
    static {
        if(pool == null) {
            //连接ip
            String strIp = ProperServicesUtils.getServicePro("locks.redis.jedispool.ip");
            //连接端口
            String strProt = ProperServicesUtils.getServicePro("locks.redis.jedispool.port");
            //连接密码
            String strPwd = ProperServicesUtils.getServicePro("locks.redis.jedispool.pwd");
            //连接库,默认为0库
            String strIndex = ProperServicesUtils.getServicePro("locks.redis.jedispool.index");
            //连接最大可创建实例
            String strMaxTotal = ProperServicesUtils.getServicePro("locks.redis.jedispool.maxTotal");
            //最大空闲实例
            String strMaxIdle = ProperServicesUtils.getServicePro("locks.redis.jedispool.maxIdle");
            //创建实例最大等待时间
            String strMaxWaitMillis = ProperServicesUtils.getServicePro("locks.redis.jedispool.maxWaitMillis");
            //连接超时时间
            String strTimeout = ProperServicesUtils.getServicePro("locks.redis.jedispool.timeOut");
            if(StringUtils.isBlank(strIp)
                    ||StringUtils.isBlank(strProt)){
                throw new SysErrorExeception("锁包加载异常！请在service.properties中配置锁参数" +
                        "【locks.redis.jedispool.timeOut】【locks.redis.jedispool.timeOut】等");
            }
            int intProt =
                    StringUtils.isBlank(strProt)?6379:Integer.parseInt(strProt);
            int intIndex =
                    StringUtils.isBlank(strIndex)?0:Integer.parseInt(strIndex);
            int intMaxTotal =
                    StringUtils.isBlank(strMaxTotal)?50:Integer.parseInt(strMaxTotal);
            int intMaxIdle =
                    StringUtils.isBlank(strMaxIdle)?5:Integer.parseInt(strMaxIdle);
            int intMaxWaitMillis =
                    StringUtils.isBlank(strMaxWaitMillis)?1000*100:Integer.parseInt(strMaxWaitMillis);
            int intTimeout =
                    StringUtils.isBlank(strTimeout)?2000:Integer.parseInt(strTimeout);
            log.info("锁jedis连接池配置：" +
                            "Ip:{}," +
                            "Prot:{}." +
                            "Pwd:***," +
                            "intIndex:{}," +
                            "MaxTotal:{}," +
                            "MaxIdle:{}," +
                            "MaxWaitMillis:{}毫秒," +
                            "Timeout:{}"
                    ,strIp,strProt,intIndex,strMaxTotal
                    ,strMaxIdle,strMaxWaitMillis,strTimeout);
            JedisPoolConfig config = new JedisPoolConfig();
            //控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
            //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
            config.setMaxTotal(intMaxTotal);
            //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
            config.setMaxIdle(intMaxIdle);
            //表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；单位毫秒
            //小于零:阻塞不确定的时间,  默认-1
            config.setMaxWaitMillis(intMaxWaitMillis);
            //在borrow(引入)一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
            config.setTestOnBorrow(true);
            //return 一个jedis实例给pool时，是否检查连接可用性（ping()）
            config.setTestOnReturn(true);
            //connectionTimeout 连接超时（默认2000ms）
            //soTimeout 响应超时（默认2000ms）
            try{
                pool = new JedisPool(config, strIp, intProt, intTimeout, strPwd,intIndex);
            }catch (Exception ee){
                throw new SysErrorExeception("锁包，初始化创建jedis连接池异常");
            }
        }
    }
    /**
     *
     * 方法描述 获取Jedis实例
     * @return
     */
    public static Jedis getJedis() {
        return pool.getResource();
    }
    /**
     * 方法描述 释放jedis连接资源
     * @param jedis
     */
    public static void close(Jedis jedis) {
        if(jedis != null) {
            jedis.close();
            jedis = null;
        }
    }
}
