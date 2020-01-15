package com.video.vip.basics.id;

/**
 * id生成
 * @author wxn
 */
public class IdWork {
    public static long getId(){
        return SnowFlake.getInstance().nextId();
    }
}
