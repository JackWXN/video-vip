package com.video.vip.basics.exception.base;

import lombok.Data;

@Data
public class VideoRuntimeException extends RuntimeException {
    private static final long serialVersionUID = -3821331516089780637L;

    /**
     * 异常编码
     */
    private String exptCode;

    /**
     * 异常消息
     */
    private String exptMsg;
}
