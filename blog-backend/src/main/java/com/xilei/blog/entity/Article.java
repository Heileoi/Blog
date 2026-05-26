package com.xilei.blog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 文章实体类
 * 对应数据库表：tb_article
 * 功能：存储博客文章的核心信息，支持Markdown编辑和富文本展示
 */
@Data
@TableName("tb_article")
public class Article {

    /** 文章ID，自增主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 文章标题 */
    private String title;

    /** 文章摘要（自动截取或手动填写） */
    private String summary;

    /** 文章正文内容（Markdown格式） */
    private String content;

    /** 封面图片URL */
    private String coverImage;

    /** 分类ID */
    private Long categoryId;

    /** 作者ID */
    private Long userId;

    /** 文章状态：0-草稿，1-已发布，2-已下架 */
    private Integer status;

    /** 是否置顶：0-否，1-是 */
    private Integer isTop;

    /** 是否推荐：0-否，1-是 */
    private Integer isFeatured;

    /** 是否允许评论：0-否，1-是 */
    private Integer isCommentEnabled;

    /** 文章密码（加密文章需要密码访问） */
    private String password;

    /** 访问量 */
    private Integer viewCount;

    /** 点赞数 */
    private Integer likeCount;

    /** 评论数 */
    private Integer commentCount;

    /** 收藏数 */
    private Integer collectCount;

    /** 文章来源（原创/转载） */
    private String source;

    /** 原文链接（转载时使用） */
    private String sourceUrl;

    /** SEO关键词 */
    private String keywords;

    /** 逻辑删除标记 */
    @TableLogic
    private Integer isDeleted;

    /** 发布时间 */
    private LocalDateTime publishTime;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /** 分类名称（非数据库字段，用于关联查询） */
    @TableField(exist = false)
    private String categoryName;

    /** 标签列表（非数据库字段，用于关联查询） */
    @TableField(exist = false)
    private java.util.List<Tag> tags;

    /** 作者昵称（非数据库字段） */
    @TableField(exist = false)
    private String authorName;
}
