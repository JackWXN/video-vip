package com.video.vip.security.util;

import com.video.vip.basics.dto.Result;
import com.video.vip.basics.util.enums.ResultEnum;
import com.video.vip.locks.JedisPoolClient;
import com.video.vip.security.enums.VisitVidTypeEnum;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

/**
 * 通用工具包
 * @author 何智琦
 */
@Slf4j
public class CommonApiSecurityUtils {
    /**
     * 访问频率redis中记录的KEY
     */
    private static final String REDIS_FREQUENCY_KEY = "casfre_";
    public static final String TOKEN_KEY = "vip-session";

    /**
     * 设置访问频率
     * @param vid 验证id
     * @param visitVidTypeEnum
     * @return
     */
    public static Result<Long> frequency(String vid, VisitVidTypeEnum visitVidTypeEnum) {
        log.debug("用户访问频率限制设置jedis开始:vid:{},visitVidTypeEnum:{}"
                , vid, visitVidTypeEnum);
        Result<Long> result = Result.newSuccess();
        String redisFkey = CommonApiSecurityUtils.REDIS_FREQUENCY_KEY + visitVidTypeEnum.getCode() + "_" + vid;
        Jedis jedis = null;
        try {
            jedis = JedisPoolClient.getJedis();
            Long count = jedis.incr(redisFkey);
            if (count.intValue() == 1) {
                log.debug("pid:{} 未访问过，允许访问，访问时效(设置有效期)：{}，单位时间内可访问次数{}", vid, visitVidTypeEnum.getOverTimeSecond(), visitVidTypeEnum.getOverTimeCount());
                jedis.expire(redisFkey, visitVidTypeEnum.getOverTimeSecond().intValue());
            } else if (count > visitVidTypeEnum.getOverTimeCount()) {
                log.warn("pid:{}访问次数{},单位时间（{}）访问超过限制{}次!", vid, count, visitVidTypeEnum.getOverTimeSecond(), visitVidTypeEnum.getOverTimeCount());
                result = Result.newResult(ResultEnum.FREQUENT_ACCESS, "");
            } else {
                log.debug("pid:{}访问次数未超过限制，当前次数:{}" , vid, count);
            }
            result.setData(count);
        } catch (Exception ee) {
            log.error("频率验证出现异常：{},{}", ee.getClass(), ee.getMessage(), ee);
        } finally {
            JedisPoolClient.close(jedis);
        }
        log.debug("用户访问频率限制设置结束:{}", result);
        return result;
    }
}
