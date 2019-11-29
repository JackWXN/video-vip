package com.video.vip.basics.util.basics;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 反射工具类
 * @author wxn
 */
@Slf4j
public class ReflectUtil {
    /**
     * 按照类名反射出它的一个对象
     *
     * @param classname 包名+类名
     * @return Object
     */
    @SuppressWarnings("rawtypes")
    public static Object getObjByClassName(String classname) {
        Object obj = null;
        if (classname != null) {
            try {
                Class a = Class.forName(classname);
                obj = a.newInstance();
            } catch (InstantiationException e) {
                log.error("反射出现异常：",e);
            } catch (IllegalAccessException e) {
                log.error("反射出现异常：",e);
            } catch (ClassNotFoundException e) {
                log.error("反射出现异常：",e);
            }
        }
        return obj;
    }

    /**
     * 按照类名,参数值反射出它的一个对象
     *
     * @param classname 包名+类名
     * @param parameter 构造函数的参数值
     * @return Object
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Object getObjByClassNameAndParameter(String classname, Object[] parameter) {
        Object obj = null;
        if (classname != null) {
            try {
                Class a = Class.forName(classname);
                // 获取公有的构造函数,指定参数
                Constructor con = a.getConstructor(getParameterClass(parameter));
                obj = con.newInstance(parameter);
            } catch (IllegalArgumentException e) {
                log.error("反射出现异常：",e);
            } catch (InstantiationException e) {
                log.error("反射出现异常：",e);
            } catch (IllegalAccessException e) {
                log.error("反射出现异常：",e);
            } catch (InvocationTargetException e) {
                log.error("反射出现异常：",e);
            } catch (SecurityException e) {
                log.error("反射出现异常：",e);
            } catch (NoSuchMethodException e) {
                log.error("反射出现异常：",e);
            } catch (ClassNotFoundException e) {
                log.error("反射出现异常：",e);
            }
        }
        return obj;
    }

    /**
     * 用类名反射调用它的某个方法(一般针对工具类或者service)(无参数)
     *
     * @param className  类名
     * @param methodName 方法名
     * @return Object
     */
    public static Object invokeMethod(String className, String methodName) {
        return invokeMethodWithObjHasParame(className, getObjByClassName(className), methodName, new Object[0]);
    }

    /**
     * 用类名反射调用它的某个方法(一般针对工具类或者service)(有参数)
     *
     * @param className  类名
     * @param methodName 方法名
     * @param parameter  参数数组
     * @return Object
     */
    public static Object invokeMethodHasParame(String className, String methodName, Object[] parameter) {
        return invokeMethodWithObjHasParame(className, getObjByClassName(className), methodName, parameter);
    }

    /**
     * 用对象反射调用它的某个方法(没有参数的方法)
     *
     * @param className  类名
     * @param obj        对象
     * @param methodName 方法名
     * @return [返回类型说明]
     */
    public static Object invokeMethodWithObj(String className, Object obj, String methodName) {
        return invokeMethodWithObjHasParame(className, obj, methodName, new Object[0]);
    }

    /**
     * 用对象反射调用它的某个方法(有参数的方法)
     *
     * @param className  类名
     * @param obj        对象
     * @param methodName 方法名
     * @param parameter  参数数组
     * @return Object
     */
    public static Object invokeMethodWithObjHasParame(String className, Object obj, String methodName,
                                                      Object[] parameter) {
        return invokeMethodWithObjHasSpecialParame(className, obj, methodName, parameter, getParameterClass(parameter));
    }

    /**
     * 获取参数列表的class对象
     *
     * @param parameter 参数值数组
     * @return Class[]
     */
    @SuppressWarnings("rawtypes")
    private static Class[] getParameterClass(Object[] parameter) {
        Class[] methodParameters = null;
        if (parameter != null && parameter.length > 0) {
            methodParameters = new Class[parameter.length];
            for (int i = 0; i < parameter.length; i++) {
                methodParameters[i] = parameter[i].getClass();
            }
        }
        return methodParameters;
    }

    /**
     * 用对象反射调用它的某个方法(指定参数类型的方法)
     *
     * @param className        类名
     * @param obj              对象
     * @param methodName       方法名
     * @param parameter        参数数组
     * @param methodParameters 参数类型数组
     * @return Object
     */
    @SuppressWarnings("rawtypes")
    public static Object invokeMethodWithObjHasSpecialParame(String className, Object obj, String methodName,
                                                             Object[] parameter, Class[] methodParameters) {
        Object object = null;
        try {
            Method method = Class.forName(className).getMethod(methodName.trim(), methodParameters);
            object = method.invoke(obj, parameter);
        } catch (IllegalArgumentException e) {
            log.error("反射出现异常：",e);
        } catch (IllegalAccessException e) {
            log.error("反射出现异常：",e);
        } catch (InvocationTargetException e) {
            log.error("反射出现异常：",e);
        } catch (SecurityException e) {
            log.error("反射出现异常：",e);
        } catch (NoSuchMethodException e) {
            log.error("反射出现异常：",e);
        } catch (ClassNotFoundException e) {
            log.error("反射出现异常：",e);
        }
        return object;
    }

    // 定义注解并指定java注解保留策略为运行时RUNTIME,运行时注入到JAVA字节码文件里
// 这样才可以在运行时反射并获取它。
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
    @interface MyAnnotation{
        String key() default "";
        int value()  default 0;
    }

    /**
     * 反射获取一个类的方法信息 包括参数,方法名,返回类型
     *
     * @param className 类名
     * @return List<String>
     */
    public static List<String> getMethodMsg(String className) {
        List<String> retValue = new ArrayList<String>();
        try {
            // 通过getMethods得到类中包含的方法
            Class myClass = Class.forName(className);
            Method m[] = myClass.getDeclaredMethods();
            for (int i = 0; i < m.length; i++) {
                String meth = m[i].toString();
                // 截取出所有的参数,参数以,形式分割
                meth = meth.substring(meth.indexOf("(") + 1, meth.indexOf(")"));
                // ret由3部分构成:参数;方法名;返回类型
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("params",meth);
                jsonObject.put("method",m[i].getName());
                jsonObject.put("returnType",m[i].getReturnType());
//                String ret = meth + ";"
//                        + m[i].getName() + ";"
//                        + m[i].getReturnType();
                retValue.add(jsonObject.toJSONString());
            }
            return retValue;
        } catch (ClassNotFoundException e) {
            log.error("反射出现异常：",e);
        }
        return retValue;
    }

    /**
     * 反射获取一个类指定的方法信息 包括参数,方法名,返回类型
     *
     * @param className 类名
     * @param methodName 方法名
     * @return List<String>
     */
    public static List<String> getMethodMsg(String className,String methodName) {
        List<String> retValue = new ArrayList<String>();
        try {
            // 通过getMethods得到类中包含的方法
            Class myClass = Class.forName(className);
            Method m[] = myClass.getDeclaredMethods();
            for (int i = 0; i < m.length; i++) {
                if(!m[i].getName().equals(methodName)){
                    log.debug("方法名与传入的方法名不匹配，跳过：methodName:{},m[i]:{}"
                            ,methodName,m[i].getName());
                    continue;
                }
                String meth = m[i].toString();
                // 截取出所有的参数,参数以,形式分割
                meth = meth.substring(meth.indexOf("(") + 1, meth.indexOf(")"));
                // ret由3部分构成:参数;方法名;返回类型
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("params",meth);
                jsonObject.put("method",m[i].getName());
                jsonObject.put("returnType",m[i].getReturnType());
//                String ret = meth + ";"
//                        + m[i].getName() + ";"
//                        + m[i].getReturnType();
                retValue.add(jsonObject.toJSONString());
            }
            return retValue;
        } catch (ClassNotFoundException e) {
            log.error("反射出现异常：",e);
        }
        return retValue;
    }
}
