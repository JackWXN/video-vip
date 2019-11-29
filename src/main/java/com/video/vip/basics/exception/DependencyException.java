package com.video.vip.basics.exception;

/**
 * Created by yanghongtao1 on 2019/1/8.
 */
public class DependencyException extends RuntimeException {

    private static final long serialVersionUID = 936915964862903634L;

    private Type type;

    private Throwable causeThrowable;

    public DependencyException(Type type, String message, Throwable e) {
        super(message);
        this.type = type;
        this.causeThrowable = e;
    }

    public DependencyException(Type type, String message) {
        this(type, message, null);
    }


    public Type type() {
        return type;
    }


    public Throwable causeThrowable() {
        return causeThrowable;
    }


    public enum Type {
        DEPEND_REQUEST_TYPE, DEPEND_ERROR_CODE_TYPE, NULL_RESULT_ERROR;
    }
}