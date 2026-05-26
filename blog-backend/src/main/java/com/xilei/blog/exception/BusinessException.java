package com.xilei.blog.exception;

import lombok.Getter;

/**
 * 业务异常类
 * 功能：封装业务逻辑中的异常，携带错误码和错误消息
 * 使用场景：参数校验失败、业务规则不满足等
 */
@Getter
public class BusinessException extends RuntimeException {

    /** 错误码 */
    private final Integer code;

    public BusinessException(String message) {
        super(message);
        this.code = 500;
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
