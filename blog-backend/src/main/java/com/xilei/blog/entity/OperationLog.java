package com.xilei.blog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 操作日志实体类
 * 对应数据库表：tb_operation_log
 * 功能：记录管理员的所有操作行为，用于安全审计和问题追溯
 */
@Data
@TableName("tb_operation_log")
public class OperationLog {

    /** 日志ID，自增主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 操作用户ID */
    private Long userId;

    /** 操作用户名 */
    private String username;

    /** 操作模块（如：文章管理、用户管理） */
    private String module;

    /** 操作类型（如：新增、修改、删除） */
    private String operation;

    /** 操作方法 */
    private String method;

    /** 请求URL */
    private String url;

    /** 请求参数 */
    private String params;

    /** 请求IP地址 */
    private String ipAddress;

    /** 操作状态：0-失败，1-成功 */
    private Integer status;

    /** 错误信息 */
    private String errorMsg;

    /** 操作耗时（毫秒） */
    private Long duration;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
