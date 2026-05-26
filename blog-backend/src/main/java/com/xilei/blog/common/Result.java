package com.xilei.blog.common;

import lombok.Data;
import java.io.Serializable;

/**
 * 统一响应结果封装类
 * 功能：封装所有API接口的返回数据，保证响应格式一致性
 * 响应格式：{ code: 200, message: "操作成功", data: {...} }
 */
@Data
public class Result<T> implements Serializable {

    /** 状态码 */
    private Integer code;

    /** 响应消息 */
    private String message;

    /** 响应数据 */
    private T data;

    /** 时间戳 */
    private Long timestamp;

    public Result() {
        this.timestamp = System.currentTimeMillis();
    }

    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    /** 成功响应（无数据） */
    public static <T> Result<T> success() {
        return new Result<>(200, "操作成功", null);
    }

    /** 成功响应（带数据） */
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }

    /** 成功响应（自定义消息和数据） */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(200, message, data);
    }

    /** 失败响应（默认消息） */
    public static <T> Result<T> error() {
        return new Result<>(500, "操作失败", null);
    }

    /** 失败响应（自定义消息） */
    public static <T> Result<T> error(String message) {
        return new Result<>(500, message, null);
    }

    /** 失败响应（自定义状态码和消息） */
    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code, message, null);
    }

    /** 未认证响应 */
    public static <T> Result<T> unauthorized() {
        return new Result<>(401, "未登录或登录已过期", null);
    }

    /** 无权限响应 */
    public static <T> Result<T> forbidden() {
        return new Result<>(403, "没有操作权限", null);
    }

    /** 资源不存在响应 */
    public static <T> Result<T> notFound() {
        return new Result<>(404, "资源不存在", null);
    }
}
