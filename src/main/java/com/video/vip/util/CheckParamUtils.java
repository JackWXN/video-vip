package com.video.vip.util;

import java.lang.reflect.Field;

/**
 * 验证请求参数是否合法
 */
public class CheckParamUtils {

    public static String checkNotNull(Object obj) {
        try {
            Class<?> clazz = obj.getClass();
            //获得私有的成员属性
            Field[] fields = clazz.getDeclaredFields();
            if(fields.length>0){
                for(Field f : fields){
                    f.setAccessible(true);
                    //判断AllowNull注解是否存在
                    if(!f.isAnnotationPresent(AllowNull.class)){
                        if(f.get(obj)==null || "".equals(f.get(obj))){
                            return f.getName()+":不允许为空！";
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            return "IllegalAccessException";
        }
        return null;
    }
}
