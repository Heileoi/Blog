-- =============================================
-- 熙磊个人博客系统 - 数据库初始化脚本
-- 数据库: MySQL 8.x
-- 字符集: utf8mb4
-- =============================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS blog_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE blog_db;

-- =============================================
-- 1. 用户表
-- =============================================
CREATE TABLE IF NOT EXISTS `tb_user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(100) NOT NULL COMMENT '密码(BCrypt加密)',
    `nickname` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
    `bio` VARCHAR(500) DEFAULT NULL COMMENT '个人简介',
    `website` VARCHAR(255) DEFAULT NULL COMMENT '个人网站',
    `github` VARCHAR(255) DEFAULT NULL COMMENT 'GitHub地址',
    `role` TINYINT NOT NULL DEFAULT 0 COMMENT '角色: 0-普通用户, 1-管理员',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0-未删除, 1-已删除',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    KEY `idx_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- =============================================
-- 2. 分类表
-- =============================================
CREATE TABLE IF NOT EXISTS `tb_category` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '分类ID',
    `name` VARCHAR(50) NOT NULL COMMENT '分类名称',
    `slug` VARCHAR(50) DEFAULT NULL COMMENT '分类别名(URL友好)',
    `description` VARCHAR(200) DEFAULT NULL COMMENT '分类描述',
    `parent_id` BIGINT NOT NULL DEFAULT 0 COMMENT '父分类ID(0为顶级)',
    `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序值(越小越前)',
    `icon` VARCHAR(50) DEFAULT NULL COMMENT '分类图标',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章分类表';

-- =============================================
-- 3. 标签表
-- =============================================
CREATE TABLE IF NOT EXISTS `tb_tag` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '标签ID',
    `name` VARCHAR(50) NOT NULL COMMENT '标签名称',
    `slug` VARCHAR(50) DEFAULT NULL COMMENT '标签别名(URL友好)',
    `color` VARCHAR(20) DEFAULT NULL COMMENT '标签颜色',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章标签表';

-- =============================================
-- 4. 文章表
-- =============================================
CREATE TABLE IF NOT EXISTS `tb_article` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '文章ID',
    `title` VARCHAR(200) NOT NULL COMMENT '文章标题',
    `summary` VARCHAR(500) DEFAULT NULL COMMENT '文章摘要',
    `content` LONGTEXT COMMENT '文章内容(Markdown)',
    `cover_image` VARCHAR(255) DEFAULT NULL COMMENT '封面图片URL',
    `category_id` BIGINT DEFAULT NULL COMMENT '分类ID',
    `user_id` BIGINT NOT NULL COMMENT '作者ID',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态: 0-草稿, 1-已发布, 2-已下架',
    `is_top` TINYINT NOT NULL DEFAULT 0 COMMENT '是否置顶: 0-否, 1-是',
    `is_featured` TINYINT NOT NULL DEFAULT 0 COMMENT '是否推荐: 0-否, 1-是',
    `is_comment_enabled` TINYINT NOT NULL DEFAULT 1 COMMENT '是否允许评论: 0-否, 1-是',
    `password` VARCHAR(100) DEFAULT NULL COMMENT '文章密码(加密文章)',
    `view_count` INT NOT NULL DEFAULT 0 COMMENT '访问量',
    `like_count` INT NOT NULL DEFAULT 0 COMMENT '点赞数',
    `comment_count` INT NOT NULL DEFAULT 0 COMMENT '评论数',
    `collect_count` INT NOT NULL DEFAULT 0 COMMENT '收藏数',
    `source` VARCHAR(20) DEFAULT '原创' COMMENT '文章来源',
    `source_url` VARCHAR(255) DEFAULT NULL COMMENT '原文链接',
    `keywords` VARCHAR(200) DEFAULT NULL COMMENT 'SEO关键词',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    `publish_time` DATETIME DEFAULT NULL COMMENT '发布时间',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_category` (`category_id`),
    KEY `idx_user` (`user_id`),
    KEY `idx_status` (`status`),
    KEY `idx_publish_time` (`publish_time`),
    FULLTEXT KEY `ft_title_summary` (`title`, `summary`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章表';

-- =============================================
-- 5. 文章-标签关联表
-- =============================================
CREATE TABLE IF NOT EXISTS `tb_article_tag` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `article_id` BIGINT NOT NULL COMMENT '文章ID',
    `tag_id` BIGINT NOT NULL COMMENT '标签ID',
    PRIMARY KEY (`id`),
    KEY `idx_article` (`article_id`),
    KEY `idx_tag` (`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章标签关联表';

-- =============================================
-- 6. 评论表
-- =============================================
CREATE TABLE IF NOT EXISTS `tb_comment` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '评论ID',
    `content` TEXT NOT NULL COMMENT '评论内容',
    `article_id` BIGINT NOT NULL COMMENT '文章ID',
    `user_id` BIGINT DEFAULT NULL COMMENT '评论用户ID',
    `parent_id` BIGINT NOT NULL DEFAULT 0 COMMENT '父评论ID(0为顶级)',
    `reply_user_id` BIGINT DEFAULT NULL COMMENT '回复目标用户ID',
    `nickname` VARCHAR(50) DEFAULT NULL COMMENT '评论者昵称(游客)',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '评论者邮箱(游客)',
    `website` VARCHAR(255) DEFAULT NULL COMMENT '评论者网站',
    `ip_address` VARCHAR(50) DEFAULT NULL COMMENT '评论者IP',
    `user_agent` VARCHAR(500) DEFAULT NULL COMMENT '浏览器信息',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 0-待审核, 1-已通过, 2-已拒绝',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_article` (`article_id`),
    KEY `idx_user` (`user_id`),
    KEY `idx_parent` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

-- =============================================
-- 7. 文件上传表
-- =============================================
CREATE TABLE IF NOT EXISTS `tb_upload_file` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '文件ID',
    `original_name` VARCHAR(255) NOT NULL COMMENT '原始文件名',
    `storage_name` VARCHAR(255) NOT NULL COMMENT '存储文件名',
    `url` VARCHAR(500) NOT NULL COMMENT '文件访问URL',
    `suffix` VARCHAR(20) DEFAULT NULL COMMENT '文件后缀',
    `file_size` BIGINT DEFAULT 0 COMMENT '文件大小(字节)',
    `mime_type` VARCHAR(100) DEFAULT NULL COMMENT 'MIME类型',
    `storage_type` VARCHAR(20) DEFAULT 'minio' COMMENT '存储位置',
    `user_id` BIGINT DEFAULT NULL COMMENT '上传用户ID',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文件上传表';

-- =============================================
-- 8. 系统配置表
-- =============================================
CREATE TABLE IF NOT EXISTS `tb_system_config` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '配置ID',
    `config_key` VARCHAR(100) NOT NULL COMMENT '配置键名',
    `config_value` TEXT COMMENT '配置值',
    `description` VARCHAR(200) DEFAULT NULL COMMENT '配置描述',
    `config_group` VARCHAR(50) DEFAULT 'basic' COMMENT '配置分组',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_config_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统配置表';

-- =============================================
-- 9. 操作日志表
-- =============================================
CREATE TABLE IF NOT EXISTS `tb_operation_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '日志ID',
    `user_id` BIGINT DEFAULT NULL COMMENT '操作用户ID',
    `username` VARCHAR(50) DEFAULT NULL COMMENT '操作用户名',
    `module` VARCHAR(50) DEFAULT NULL COMMENT '操作模块',
    `operation` VARCHAR(50) DEFAULT NULL COMMENT '操作类型',
    `method` VARCHAR(10) DEFAULT NULL COMMENT '请求方法',
    `url` VARCHAR(255) DEFAULT NULL COMMENT '请求URL',
    `params` TEXT COMMENT '请求参数',
    `ip_address` VARCHAR(50) DEFAULT NULL COMMENT 'IP地址',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 0-失败, 1-成功',
    `error_msg` TEXT COMMENT '错误信息',
    `duration` BIGINT DEFAULT 0 COMMENT '操作耗时(ms)',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_user` (`user_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

-- =============================================
-- 10. 公告表
-- =============================================
CREATE TABLE IF NOT EXISTS `tb_notice` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '公告ID',
    `title` VARCHAR(100) NOT NULL COMMENT '公告标题',
    `content` TEXT COMMENT '公告内容',
    `type` TINYINT NOT NULL DEFAULT 1 COMMENT '类型: 1-系统公告, 2-更新日志, 3-活动通知',
    `is_top` TINYINT NOT NULL DEFAULT 0 COMMENT '是否置顶',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态: 0-草稿, 1-已发布',
    `user_id` BIGINT DEFAULT NULL COMMENT '创建者ID',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公告表';

-- =============================================
-- 11. 友情链接表
-- =============================================
CREATE TABLE IF NOT EXISTS `tb_friend_link` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '友链ID',
    `name` VARCHAR(50) NOT NULL COMMENT '网站名称',
    `url` VARCHAR(255) NOT NULL COMMENT '网站链接',
    `logo` VARCHAR(255) DEFAULT NULL COMMENT '网站Logo',
    `description` VARCHAR(200) DEFAULT NULL COMMENT '网站描述',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '联系邮箱',
    `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序值',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态: 0-待审核, 1-已通过, 2-已拒绝',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='友情链接表';

-- =============================================
-- 12. 文章收藏表
-- =============================================
CREATE TABLE IF NOT EXISTS `tb_article_collect` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `article_id` BIGINT NOT NULL COMMENT '文章ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_article_user` (`article_id`, `user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章收藏表';

-- =============================================
-- 初始化数据
-- =============================================

-- 插入管理员用户 (密码: admin123, BCrypt加密)
INSERT INTO `tb_user` (`username`, `password`, `nickname`, `email`, `avatar`, `bio`, `role`, `status`)
VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '王熙磊', 'admin@xilei.com', '/default-avatar.png', '资深Java开发工程师，10年+开发经验', 1, 1);

-- 插入默认分类
INSERT INTO `tb_category` (`name`, `slug`, `description`, `sort_order`) VALUES
('技术笔记', 'tech', '技术学习笔记和总结', 1),
('Java', 'java', 'Java相关技术', 2),
('Spring', 'spring', 'Spring框架相关', 3),
('数据库', 'database', '数据库技术', 4),
('前端开发', 'frontend', '前端技术', 5),
('运维部署', 'devops', '运维和部署相关', 6),
('项目实战', 'project', '项目实战经验', 7),
('生活随笔', 'life', '生活感悟和随笔', 8);

-- 插入默认标签
INSERT INTO `tb_tag` (`name`, `slug`, `color`) VALUES
('Java', 'java', '#f89820'),
('Spring Boot', 'spring-boot', '#6db33f'),
('MySQL', 'mysql', '#4479a1'),
('Redis', 'redis', '#d82c20'),
('Vue', 'vue', '#42b883'),
('Docker', 'docker', '#2496ed'),
('Linux', 'linux', '#fcc624'),
('MyBatis', 'mybatis', '#e35b25'),
('微服务', 'microservice', '#ff6b6b'),
('设计模式', 'design-pattern', '#9b59b6');

-- 插入系统配置
INSERT INTO `tb_system_config` (`config_key`, `config_value`, `description`, `config_group`) VALUES
('blog_name', '熙磊的个人博客', '博客名称', 'basic'),
('blog_url', 'http://localhost:5173', '博客地址', 'basic'),
('blog_description', '一个企业级Java博客系统', '博客描述', 'basic'),
('blog_keywords', 'Java,Spring Boot,博客,技术', 'SEO关键词', 'seo'),
('blog_footer', '© 2024 王熙磊. All Rights Reserved.', '页脚信息', 'basic'),
('comment_audit', 'false', '评论是否需要审核', 'basic'),
('file_max_size', '20', '文件上传最大大小(MB)', 'basic');

-- 插入示例公告
INSERT INTO `tb_notice` (`title`, `content`, `type`, `status`) VALUES
('欢迎访问我的博客', '欢迎来到熙磊的个人博客！这里记录了我的技术学习和项目经验。', 1, 1);
