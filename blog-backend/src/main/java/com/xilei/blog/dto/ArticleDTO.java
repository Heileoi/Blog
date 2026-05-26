package com.xilei.blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

/**
 * 文章DTO
 * 功能：封装文章的创建和更新请求数据
 */
@Data
public class ArticleDTO {

    /** 文章ID（更新时必填） */
    private Long id;

    /** 文章标题 */
    @NotBlank(message = "文章标题不能为空")
    private String title;

    /** 文章摘要 */
    private String summary;

    /** 文章内容（Markdown格式） */
    @NotBlank(message = "文章内容不能为空")
    private String content;

    /** 封面图片URL */
    private String coverImage;

    /** 分类ID */
    @NotNull(message = "请选择文章分类")
    private Long categoryId;

    /** 标签ID列表 */
    private List<Long> tagIds;

    /** 文章状态：0-草稿，1-已发布 */
    private Integer status;

    /** 是否置顶 */
    private Integer isTop;

    /** 是否推荐 */
    private Integer isFeatured;

    /** 是否允许评论 */
    private Integer isCommentEnabled;

    /** 文章密码 */
    private String password;

    /** 文章来源 */
    private String source;

    /** 原文链接 */
    private String sourceUrl;

    /** SEO关键词 */
    private String keywords;
}
