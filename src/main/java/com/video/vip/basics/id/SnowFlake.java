package com.video.vip.basics.id;

import com.video.vip.basics.util.basics.DateUtil;
import com.video.vip.basics.util.basics.IPUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


/**
 * twitter的snowflake算法 -- java实现
 * 协议格式： 0 - 38位时间戳 - 2位datacenter - 8 位机器标识 - 6位业务标识 - 2位实例序列- 7位时间序列
 */
@Slf4j
@Component
public class SnowFlake {
    @Autowired
    private YmlConf getYmlConf;
    private static YmlConf ymlConf;

    @PostConstruct
    public void init() {
        ymlConf = this.getYmlConf;
    }

    private SnowFlake() {
    }


    private static class SingletonHolder {
        private static final SnowFlake INSTANCE = new SnowFlake(ymlConf.getBizNum(),ymlConf.getInstanceNum());
    }


    public static final SnowFlake getInstance() {
        try {
            return SingletonHolder.INSTANCE;
        } catch (Exception e) {
            log.error("cant create singletonholder of Snowflake:", e);
            return null;
        }
    }

    /**
     * 起始的时间戳
     */
    private final static long START_STMP = DateUtil.paraseDate2("2020-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss").getTime();

    /**
     * 每一部分占用的位数
     * 2 位datacenter - 8 位机器标识 - 7位业务标识 - 8位时间序列
     */

    private final static long DATACENTER_BIT = 2;   //数据中心标识占用的位数
    private final static long MACHINE_BIT = 8;   //机器标识占用的位数
    private final static long BIZTAG_BIT = 6;//业务中心占用的位数
    private final static long INSTANCETAG_BIT = 4;//业务实例占用的位数
    private final static long SEQUENCE_BIT = 7; //序列号占用的位数

    /**
     * 每一部分的最大值
     */

    private final static long MAX_DATACENTER_NUM = -1L ^ (-1L << DATACENTER_BIT);
    private final static long MAX_MACHINE_NUM = -1L ^ (-1L << MACHINE_BIT);
    private final static long MAX_BIZTAG_BIT_NUM = -1L ^ (-1L << BIZTAG_BIT);
    private final static long MAX_INSTANCETAG_BIT_NUM =  -1L ^ (-1L << INSTANCETAG_BIT);
    private final static long MAX_SEQUENCE = -1L ^ (-1L << SEQUENCE_BIT);

    /**
     * 每一部分向左的位移
     */
    private final static long BIZCENTER_LEFT = SEQUENCE_BIT;
    private final static long INSTANCE_LEFT = INSTANCETAG_BIT + BIZCENTER_LEFT;
    private final static long MACHINE_LEFT = BIZTAG_BIT + INSTANCE_LEFT;
    private final static long DATACENTER_LEFT = MACHINE_BIT + MACHINE_LEFT;
    private final static long TIMESTMP_LEFT = DATACENTER_BIT + DATACENTER_LEFT;

    private long datacenterId = 1;  //数据中心
    private long machineId = 1;     //机器标识
    private long bizId;
    private long instanceId;
    private long sequence = 0L; //序列号
    private long lastStmp = -1L;//上一次时间戳

    public SnowFlake(long bizId,long instanceId) {
        long temMachineId = getIp();
        if (-1 != temMachineId) {
            this.machineId = temMachineId;
        }
        log.info("获取配置的业务码：{},实例码：{}" ,bizId,instanceId);
        if (bizId > MAX_BIZTAG_BIT_NUM || bizId < 0) {
            throw new IllegalArgumentException("bizId can't be greater than MAX_BIZTAG_BIT_NUM or less than 0");
        }
        if (instanceId > MAX_INSTANCETAG_BIT_NUM || instanceId < 0) {
            throw new IllegalArgumentException("instanceId can't be greater than MAX_INSTANCETAG_BIT_NUM or less than 0");
        }
        this.bizId = bizId;
        this.instanceId = instanceId;
    }

    public SnowFlake(long datacenterId, long machineId, long bizId) {
        long temMachineId = getIp();
        if (-1 != temMachineId) {
            this.machineId = temMachineId;
        }
        log.info("获取配置的数据中心：{},机器码：{}, 业务码：{}", datacenterId, machineId, bizId);
        if (datacenterId > MAX_DATACENTER_NUM || datacenterId < 0) {
            throw new IllegalArgumentException("datacenterId can't be greater than MAX_DATACENTER_NUM or less than 0");
        }
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException("machineId can't be greater than MAX_MACHINE_NUM or less than 0");
        }
        if (bizId > MAX_BIZTAG_BIT_NUM || bizId < 0) {
            throw new IllegalArgumentException("bizId can't be greater than MAX_BIZTAG_BIT_NUM or less than 0");
        }
        this.datacenterId = datacenterId;
        this.machineId = machineId;
        this.bizId = bizId;
    }

    /**
     * 产生下一个ID
     *
     * @return
     */
    public synchronized long nextId() {
        long currStmp = getNewstmp();
        if (currStmp < lastStmp) {
            throw new RuntimeException("Clock moved backwards.  Refusing to generate id");
        }

        if (currStmp == lastStmp) {
            //相同毫秒内，序列号自增
            sequence = (sequence + 1) & MAX_SEQUENCE;
            //同一毫秒的序列数已经达到最大
            if (sequence == 0L) {
                currStmp = getNextMill();
            }
        } else {
            //不同毫秒内，序列号置为0
            sequence = 0L;
        }

        lastStmp = currStmp;

        return (currStmp - START_STMP) << TIMESTMP_LEFT //时间戳部分
                | datacenterId << DATACENTER_LEFT       //数据中心部分
                | machineId << MACHINE_LEFT             //机器标识部分
                | bizId << BIZCENTER_LEFT               //业务部分
                | instanceId << INSTANCE_LEFT           //实例部分
                | sequence;                             //序列号部分
    }

    private long getNextMill() {
        long mill = getNewstmp();
        while (mill <= lastStmp) {
            mill = getNewstmp();
        }
        return mill;
    }

    private long getNewstmp() {
        return System.currentTimeMillis();
    }

    private long getIp() {
        String localIp = IPUtil.getLocalIP();
        String[] intArr = localIp.split("\\.");
        int[] ipInt = new int[intArr.length];
        for (int i = 0; i < intArr.length; ++i) {
            ipInt[i] = (new Integer(intArr[i])).intValue();
        }
        return ipInt[3];
    }


}
