package com.video.vip.basics.util.http;

import java.util.HashMap;

/**
 * 扩展hashMap
 * Created by Administrator on 2015/6/8.
 */
public class CommonMap<K,V> extends HashMap<K,V>{

    private static final long serialVersionUID = -5900918612273451817L;

    public CommonMap(){}
    public CommonMap(K key, V value){
        this.put(key,value);
    }
    public CommonMap<K,V> add(K key,V value){
        this.put(key,value);
        return this;
    }
    public CommonMap<K,V> delete(K key){
        this.remove(key);
        return this;
    }
}
