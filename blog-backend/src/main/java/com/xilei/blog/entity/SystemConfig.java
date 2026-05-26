package com.xilei.blog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 系统配置实体类
 * 对应数据库表：tb_system_config
 * 功能：存储博客系统的可配置项，支持动态修改无需重启
 */
@Data
@TableName("tb_system_config")
public class SystemConfig {

    /** 配置ID，自增主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 配置键名（唯一） */
    private String configKey;

    /** 配置值 */
    private String configValue;

    /** 配置描述 */
    private String description;

    /** 配置分组：basic-基本, seo-SEO, email-邮件, other-其他 */
    private String configGroup;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
