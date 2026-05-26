package com.xilei.blog.enums;

import lombok.Getter;

/**
 * 评论状态枚举
 * 功能：定义评论的审核状态
 */
@Getter
public enum CommentStatus {

    PENDING(0, "待审核"),
    APPROVED(1, "已通过"),
    REJECTED(2, "已拒绝");

    private final Integer code;
    private final String desc;

    CommentStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
