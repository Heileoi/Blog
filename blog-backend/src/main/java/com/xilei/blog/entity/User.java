package com.xilei.blog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户实体类
 * 对应数据库表：tb_user
 * 功能：存储用户基本信息，支持管理员和普通用户两种角色
 */
@Data
@TableName("tb_user")
public class User {

    /** 用户ID，自增主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 用户名，唯一，用于登录 */
    private String username;

    /** 密码，BCrypt加密存储 */
    private String password;

    /** 昵称，用于前台显示 */
    private String nickname;

    /** 邮箱地址 */
    private String email;

    /** 头像URL */
    private String avatar;

    /** 个人简介 */
    private String bio;

    /** 个人网站 */
    private String website;

    /** GitHub地址 */
    private String github;

    /** 角色：0-普通用户，1-管理员 */
    private Integer role;

    /** 状态：0-禁用，1-启用 */
    private Integer status;

    /** 逻辑删除：0-未删除，1-已删除 */
    @TableLogic
    private Integer isDeleted;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
