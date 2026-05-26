package com.xilei.blog.enums;

import lombok.Getter;

/**
 * 文章状态枚举
 * 功能：定义文章的生命周期状态
 */
@Getter
public enum ArticleStatus {

    DRAFT(0, "草稿"),
    PUBLISHED(1, "已发布"),
    OFFLINE(2, "已下架");

    private final Integer code;
    private final String desc;

    ArticleStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
