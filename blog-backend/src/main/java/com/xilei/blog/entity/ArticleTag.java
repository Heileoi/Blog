package com.xilei.blog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * 文章-标签关联实体类
 * 对应数据库表：tb_article_tag
 * 功能：维护文章与标签的多对多关系
 */
@Data
@TableName("tb_article_tag")
public class ArticleTag {

    /** 主键ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 文章ID */
    private Long articleId;

    /** 标签ID */
    private Long tagId;
}
