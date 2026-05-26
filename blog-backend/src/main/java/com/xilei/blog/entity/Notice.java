package com.xilei.blog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 公告实体类
 * 对应数据库表：tb_notice
 * 功能：系统公告管理，支持置顶和定时发布
 */
@Data
@TableName("tb_notice")
public class Notice {

    /** 公告ID，自增主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 公告标题 */
    private String title;

    /** 公告内容 */
    private String content;

    /** 公告类型：1-系统公告，2-更新日志，3-活动通知 */
    private Integer type;

    /** 是否置顶：0-否，1-是 */
    private Integer isTop;

    /** 状态：0-草稿，1-已发布 */
    private Integer status;

    /** 创建者ID */
    private Long userId;

    /** 逻辑删除标记 */
    @TableLogic
    private Integer isDeleted;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
