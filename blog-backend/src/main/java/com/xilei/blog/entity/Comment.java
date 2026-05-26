package com.xilei.blog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 评论实体类
 * 对应数据库表：tb_comment
 * 功能：支持文章评论和评论回复（通过parent_id实现层级评论）
 */
@Data
@TableName("tb_comment")
public class Comment {

    /** 评论ID，自增主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 评论内容 */
    private String content;

    /** 文章ID */
    private Long articleId;

    /** 评论用户ID */
    private Long userId;

    /** 父评论ID（0表示顶级评论） */
    private Long parentId;

    /** 回复目标用户ID */
    private Long replyUserId;

    /** 评论者昵称（游客评论时使用） */
    private String nickname;

    /** 评论者邮箱（游客评论时使用） */
    private String email;

    /** 评论者网站 */
    private String website;

    /** 评论者IP地址 */
    private String ipAddress;

    /** 评论者浏览器信息 */
    private String userAgent;

    /** 状态：0-待审核，1-已通过，2-已拒绝 */
    private Integer status;

    /** 逻辑删除标记 */
    @TableLogic
    private Integer isDeleted;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /** 回复用户昵称（非数据库字段） */
    @TableField(exist = false)
    private String replyUserName;

    /** 评论用户信息（非数据库字段） */
    @TableField(exist = false)
    private User user;

    /** 子评论列表（非数据库字段） */
    @TableField(exist = false)
    private java.util.List<Comment> children;
}
