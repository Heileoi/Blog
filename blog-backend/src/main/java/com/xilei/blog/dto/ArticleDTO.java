package com.xilei.blog.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.List;

@Data
public class ArticleDTO {

    private Long id;

    @NotBlank(message = "文章标题不能为空")
    @Size(max = 200, message = "标题最长200个字符")
    private String title;

    @Size(max = 500, message = "摘要最长500个字符")
    private String summary;

    @NotBlank(message = "文章内容不能为空")
    @Size(max = 100000, message = "文章内容过长")
    private String content;

    @Size(max = 500, message = "封面图片URL过长")
    private String coverImage;

    @NotNull(message = "请选择文章分类")
    private Long categoryId;

    private List<Long> tagIds;

    private Integer status;

    private Integer isTop;

    private Integer isFeatured;

    private Integer isCommentEnabled;

    @Size(max = 100, message = "密码最长100个字符")
    private String password;

    @Size(max = 100, message = "来源最长100个字符")
    private String source;

    @Size(max = 500, message = "原文链接过长")
    private String sourceUrl;

    @Size(max = 500, message = "关键词过长")
    private String keywords;
}
