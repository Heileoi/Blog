package com.xilei.blog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 分类实体类
 * 对应数据库表：tb_category
 * 功能：文章分类管理，支持多级分类（parent_id实现树形结构）
 */
@Data
@TableName("tb_category")
public class Category {

    /** 分类ID，自增主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 分类名称 */
    private String name;

    /** 分类别名（用于URL友好化） */
    private String slug;

    /** 分类描述 */
    private String description;

    /** 父分类ID（0表示顶级分类） */
    private Long parentId;

    /** 排序值（越小越靠前） */
    private Integer sortOrder;

    /** 分类图标 */
    private String icon;

    /** 该分类下的文章数量（非数据库字段，动态统计） */
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
