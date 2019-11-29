package com.video.vip.basics.dto;

import com.alibaba.fastjson.JSONObject;
import com.video.vip.basics.util.enums.ResultEnum;
import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;

/**
 * 接口返回类型
 * @author 何智琦
 */
@Data
public class Result<T> implements Serializable {
    private static final long serialVersionUID = -2622763747476523430L;

    /**
     * ResultEnum枚举
     */
    private Integer code;
    private String message;
    private T data;

    private Result(){}

    /**
     * 判断是不是成功
     * @return
     */
    public static <T> boolean hasSuccess(Result<T> result){
        if(result!=null&&result.getCode()>0&&result.getCode()<=1000){
            return true;
        }else{
            return false;
        }
    }
    /**
     * 接口调用成功，不需要返回对象
     * @param <T>
     * @return
     */
    public static <T> Result<T> newSuccess(){
        Result<T> result = new Result<T>();
        result.setCode(ResultEnum.SUCCESS.getCode());
        result.setMessage(ResultEnum.SUCCESS.getMsg());
        return result;
    }

    /**
     * 接口调用成功，有返回对象
     * @param data 返回的数据
     * @param <T>
     * @return
     */
    public static <T> Result<T> newSuccess(T data) {
        Result<T> result = new Result<T>();
        result.setCode(ResultEnum.SUCCESS.getCode());
        result.setMessage(ResultEnum.SUCCESS.getMsg());
        result.setData(data);
        return result;
    }

    /**
     * 接口调用成功，设置成功编码和信息
     * @param resultEnum 成功枚举
     * @param message 如果不传，默认为枚举对应的msg信息
     * @param <T>
     * @return
     */
    public static <T> Result<T> newResult(@NonNull ResultEnum resultEnum
            , String message){
        Result<T> result = new Result<T>();
        result.setCode(resultEnum.getCode());
        if(null!=message&&!"".equals(message)){
            result.setMessage(message);
        }else{
            result.setMessage(resultEnum.getMsg());
        }
        return result;
    }

    /**
     * @Desc: 接口调用成功，设置成功编码和信息
     * @param: [code, message]
     * @Return: com.labi.itfin.common.constant.dto.Result<T>
     * @Author: lixiping
     * @Date: 2018/8/13
     */
    public static <T> Result<T> newResult(Integer code , String message){
        Result<T> result = new Result<T>();
        result.setCode(code);
        if(null!=message&&!"".equals(message)){
            result.setMessage(message);
        }else{
            result.setMessage(message);
        }
        return result;
    }

    /**
     * 接口调用成功，需要返回对象
     * @param resultEnum 成功枚举
     * @param message 如果不传，默认为枚举对应的msg信息
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Result<T> newResult(@NonNull ResultEnum resultEnum
            , String message, T data){
        Result<T> result = new Result<T>();
        result.setCode(resultEnum.getCode());
        if(null!=message&&!"".equals(message)){
            result.setMessage(message);
        }else{
            result.setMessage(resultEnum.getMsg());
        }
        result.setData(data);
        return result;
    }

    /**
     * 判断返回结果是否成功（大于0为成功）
     * @return
     */
    public boolean isSuccess(){
        if(code==null){
            return false;
        }else if(code>0&&code<1000){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 转换成json格式数据
     * @return
     */
    public String toJSONString(){
        return JSONObject.toJSONString(this);
    }

}
