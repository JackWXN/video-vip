package com.video.vip.basics.util.basics;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.video.vip.basics.util.enums.ResultEnum;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 枚举相关工具类
 * @author wxn
 */
@Slf4j
public class EnumUtils {


    /**
     * 根据枚举指定字段名称获取对应枚举
     * @param vlueFieldName 字段名称
     * @param enumClass 枚举类
     * @param value 要匹配的值
     * @return
     */
    public static Enum getEnumByValue(@NonNull String vlueFieldName
            ,@NonNull Class enumClass,@NonNull Object value){
        log.debug("根据枚举指定字段名称获取对应枚举开始：：vlueFileName{},：enumClass{}" +
                        ",value:{}"
                ,vlueFieldName,enumClass,value);
        Enum objREnum = null;
        try{
            List<ResultEnum> enumList = org.apache.commons.lang3.EnumUtils
                    .getEnumList(enumClass);
            for(Enum objEnum : enumList){
                Method method = objEnum.getClass()
                        .getMethod("get"+(vlueFieldName.substring(0,1).toUpperCase()
                                .concat(vlueFieldName.substring(1))));
                Object obj = (Object)method.invoke(objEnum);
                if(obj.toString().equals(value.toString())){
                    log.debug("匹配到相应的值，跳出循环:obj:{}",obj);
                    objREnum = objEnum;
                    break;
                }
            }
        }catch (Exception e){
            log.error("根据枚举指定字段名称获取对应枚举发生异常:vlueFieldName:{},enumClass:{},value:{}"
                    ,vlueFieldName,enumClass,value,e);
            throw new RuntimeException("未找到对应的枚举");
        }
        log.debug("根据枚举指定字段名称获取对应枚举结束:{}",objREnum);
        return objREnum;
    }


    /**
     * 获取指定枚举以列表形式展示
     * @param enumClasssName 枚举类名称
     * @return
     */
    public static JSONArray listEnum(@NonNull String enumClasssName){
        log.debug("获取指定枚举以列表形式展示开始：enumClasssName：{}"
                ,enumClasssName);
        JSONArray jsonArray = new JSONArray();
        try{
            Class enumClass = Class.forName(enumClasssName);
            List<Enum> enumList = org.apache.commons.lang3.EnumUtils
                    .getEnumList(enumClass);
            for(Enum objEnum : enumList){
                Method[] methods = objEnum.getClass().getMethods();
                JSONObject obj = new JSONObject();
                for(Method method : methods){
                    String methodName = method.getName();
                    if(methodName.equals("getDeclaringClass")
                            ||methodName.equals("getClass")){
                        continue;
                    }
                    if(methodName.indexOf("get")==0){
                        String strFileName = methodName.substring(3);
                        strFileName = strFileName.substring(0,1).toLowerCase()
                                .concat(strFileName.substring(1));
                        obj.put(strFileName,(Object)method.invoke(objEnum));
                    }
                }
                if (obj.size()>0){
                    jsonArray.add(obj);
                }
            }
        }catch (Exception e){
            log.error("获取指定枚举以列表形式展示发生异常",e);
        }
        log.debug("获取指定枚举以列表形式展示结束:{}",jsonArray);
        return jsonArray;
    }
}
