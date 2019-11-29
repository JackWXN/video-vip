package com.video.vip.basics.init;

import com.video.vip.basics.constant.CommonConstant;
import com.video.vip.basics.intercept.sql.monitor.mybatis.PropertiesMonitorMybatisUtil;
import com.video.vip.basics.util.CoreAchieveConstants;
import com.video.vip.basics.util.sql.SqlMonitorConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 核心工具初始化
 * @author wxn
 */
@Component
@Slf4j
@Order(1)
public class InitCore {

    static{
        try {
            log.info("===============加载sql监控本地配置文件开始===============");
            log.info("本次启动服务的唯一标识为：{}", CoreAchieveConstants.SERVER_START_CODE);
            log.info("本次启动服务的ip为：{}", CoreAchieveConstants.LOCAL_IP);
            CommonConstant.SERVER_IP = CoreAchieveConstants.LOCAL_IP;
            PropertiesMonitorMybatisUtil.init();
            log.info("===============加载sql监控本地配置文件结束===============");
        } catch (Exception e) {
            log.error("核心初始化异常: {}", e.getMessage(),e);
        }
        log.info("=====================初始化sql监控常量开始=====================");
        if(StringUtils.isNotBlank(PropertiesMonitorMybatisUtil.getValue("SQL_MONITOR"))){
            SqlMonitorConstant.SQL_MONITOR = PropertiesMonitorMybatisUtil.getValue("SQL_MONITOR");
        }
        log.info("初始化是否开启sql打印SqlMonitorConstant.SQL_MONITOR："+SqlMonitorConstant.SQL_MONITOR);
        if(StringUtils.isNotBlank(PropertiesMonitorMybatisUtil.getValue("SLOW_SQL_MILLISECOND"))){
            SqlMonitorConstant.SLOW_SQL_MILLISECOND = Integer.parseInt(PropertiesMonitorMybatisUtil.getValue("SLOW_SQL_MILLISECOND"));
        }
        log.info("初始化超过多少秒算慢sql（毫秒）SqlMonitorConstant.SLOW_SQL_MILLISECOND："+SqlMonitorConstant.SLOW_SQL_MILLISECOND);
        if(StringUtils.isNotBlank(PropertiesMonitorMybatisUtil.getValue("SELECT_SQL_RETURN_COUNT"))){
            SqlMonitorConstant.SELECT_SQL_RETURN_COUNT = Integer.parseInt(PropertiesMonitorMybatisUtil.getValue("SELECT_SQL_RETURN_COUNT"));
        }
        log.info("初始化查询的返回结果超过多少条进行记录SqlMonitorConstant.SELECT_SQL_RETURN_COUNT："+SqlMonitorConstant.SELECT_SQL_RETURN_COUNT);
        if(StringUtils.isNotBlank(PropertiesMonitorMybatisUtil.getValue("SELECT_SQL_RETURN_OBJ_SIZE"))){
            SqlMonitorConstant.SELECT_SQL_RETURN_OBJ_SIZE = Integer.parseInt(PropertiesMonitorMybatisUtil.getValue("SELECT_SQL_RETURN_OBJ_SIZE"));
        }
        log.info("初始化返回的对象大小超过多少进行记录(字节)SqlMonitorConstant.SELECT_SQL_RETURN_OBJ_SIZE："+SqlMonitorConstant.SELECT_SQL_RETURN_OBJ_SIZE);

        log.info("=====================初始化sql监控常量结束=====================");
        CoreAchieveConstants.CORE_ACHIEVE_INIT = true;
    }
}
