package com.xilei.blog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 文章收藏实体类
 * 对应数据库表：tb_article_collect
 * 功能：记录用户收藏的文章
 */
@Data
@TableName("tb_article_collect")
public class ArticleCollect {

    /** 主键ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 文章ID */
    private Long articleId;

    /** 用户ID */
    private Long userId;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
