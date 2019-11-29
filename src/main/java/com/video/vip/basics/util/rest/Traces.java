package com.video.vip.basics.util.rest;

import com.alibaba.ttl.TransmittableThreadLocal;

import java.util.concurrent.ThreadLocalRandom;

public class Traces {
  private static final TransmittableThreadLocal<String> TRACE_HOLDER =  new TransmittableThreadLocal<>();
  
  public static final TransmittableThreadLocal<String>  URI_HOLDER   =  new TransmittableThreadLocal<String>();
  
  public static final TransmittableThreadLocal<String>  KEY_HOLDER   =  new TransmittableThreadLocal<String>();
  
  public static final TransmittableThreadLocal<String>  UID_HOLDER   =  new TransmittableThreadLocal<String>();

  public static String getTraceId() {
    String traceId = TRACE_HOLDER.get();
    if (traceId == null) {
      return "";
    }
    return traceId;
  }
  
  public static void setTraceId(String traceId) {
    TRACE_HOLDER.set(traceId);
  }
  
  public static void addKey(String key){
    KEY_HOLDER.set(key);
  }
  
  public static String getKey(){
    return KEY_HOLDER.get();
  }
  
  public static void setUid(String uid){
    UID_HOLDER.set(uid);
  }
  
  public static String getUid(){
    return UID_HOLDER.get();
  }
  
  
  public static void removeUid(){
    UID_HOLDER.remove();
  }
  
  public static String getUri(){
    return URI_HOLDER.get();
  }
  
  public static void addUri(String key){
    URI_HOLDER.set(key);
  }
  
  public static void removeTraceId() {
    TRACE_HOLDER.remove();
  }
  
  public static void clearAttrs(){
    KEY_HOLDER.remove();
    URI_HOLDER.remove();
  }
  
  public static String genTraceId(){
    return Math.abs(ThreadLocalRandom.current().nextLong()) + "";
  }
  
  public static String getWithInsurance() {
    if ("".equals(getTraceId())) {
      TRACE_HOLDER.set(genTraceId());
    }
    return getTraceId();
  }
}
