package com.xilei.blog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 标签实体类
 * 对应数据库表：tb_tag
 * 功能：文章标签管理，支持文章与标签的多对多关系
 */
@Data
@TableName("tb_tag")
public class Tag {

    /** 标签ID，自增主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 标签名称 */
    private String name;

    /** 标签别名（用于URL友好化） */
    private String slug;

    /** 标签颜色（用于前端展示） */
    private String color;

    /** 该标签下的文章数量（非数据库字段，动态统计） */
    @TableField(exist = false)
    private Integer articleCount;

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
