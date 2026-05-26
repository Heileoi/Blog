package com.xilei.blog.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CommentDTO {

    @NotNull(message = "文章ID不能为空")
    private Long articleId;

    @NotBlank(message = "评论内容不能为空")
    @Size(min = 1, max = 2000, message = "评论内容长度1-2000个字符")
    private String content;

    private Long parentId;

    private Long replyUserId;

    @Size(max = 50, message = "昵称最长50个字符")
    private String nickname;

    @Email(message = "邮箱格式不正确")
    @Size(max = 100, message = "邮箱最长100个字符")
    private String email;

    @Size(max = 200, message = "网站地址最长200个字符")
    private String website;
}
