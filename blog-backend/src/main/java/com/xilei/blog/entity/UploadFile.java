package com.xilei.blog.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 文件上传实体类
 * 对应数据库表：tb_upload_file
 * 功能：记录所有上传的文件信息，支持图片、文档等多种类型
 */
@Data
@TableName("tb_upload_file")
public class UploadFile {

    /** 文件ID，自增主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 原始文件名 */
    private String originalName;

    /** 存储文件名（UUID生成，避免重名） */
    private String storageName;

    /** 文件访问URL */
    private String url;

    /** 文件后缀名（如 .jpg, .png） */
    private String suffix;

    /** 文件大小（字节） */
    private Long fileSize;

    /** MIME类型（如 image/jpeg） */
    private String mimeType;

    /** 存储位置：local-本地，minio-MinIO，oss-阿里云OSS */
    private String storageType;

    /** 上传用户ID */
    private Long userId;

    /** 逻辑删除标记 */
    @TableLogic
    private Integer isDeleted;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
