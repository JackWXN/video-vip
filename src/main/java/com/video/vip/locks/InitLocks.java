package com.video.vip.locks;

import com.video.vip.basics.exception.SysErrorExeception;
import com.video.vip.locks.redis.LockInit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 锁工具类初始化
 * @author wxn
 */
@Component
@Slf4j
@Order(2)
public class InitLocks {

    static{
        log.info("===============开始初始化锁===============");
        try {
            log.info("===============初始化redis===============");
            LockInit.initRedsMac();
            log.info("===============初始化redis结束===============");
        } catch (Exception e) {
            log.error("锁始化异常: {}", e.getMessage(),e);
            throw new SysErrorExeception();
        }
        log.info("=====================初始化锁结束=====================");
    }
}
