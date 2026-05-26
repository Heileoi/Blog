package com.xilei.blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 评论DTO
 * 功能：封装评论提交的数据
 */
@Data
public class CommentDTO {

    /** 文章ID */
    @NotNull(message = "文章ID不能为空")
    private Long articleId;

    /** 评论内容 */
    @NotBlank(message = "评论内容不能为空")
    private String content;

    /** 父评论ID（回复时使用） */
    private Long parentId;

    /** 回复目标用户ID */
    private Long replyUserId;

    /** 游客昵称（未登录时使用） */
    private String nickname;

    /** 游客邮箱 */
    private String email;

    /** 游客网站 */
    private String website;
}
