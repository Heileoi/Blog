package com.xilei.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xilei.blog.common.PageResult;
import com.xilei.blog.config.MinioConfig;
import com.xilei.blog.entity.UploadFile;
import com.xilei.blog.exception.BusinessException;
import com.xilei.blog.mapper.UploadFileMapper;
import com.xilei.blog.service.FileService;
import com.xilei.blog.utils.SecurityUtils;
import io.minio.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 文件Service实现类
 * 功能：实现文件上传、删除、查询等业务逻辑
 * 支持本地存储和MinIO对象存储两种方式
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final UploadFileMapper uploadFileMapper;
    private final MinioConfig minioConfig;
    private final MinioClient minioClient;

    @Value("${file.upload-dir:./uploads}")
    private String uploadDir;

    /**
     * 上传文件
     * 流程：1.生成唯一文件名 2.上传到存储服务 3.保存文件记录
     */
    @Override
    public String uploadFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BusinessException("上传文件不能为空");
        }

        // 获取文件信息
        String originalName = file.getOriginalFilename();
        String suffix = originalName != null ? originalName.substring(originalName.lastIndexOf(".")) : "";
        String storageName = generateStorageName(suffix);

        try {
            // 上传到MinIO
            String url = uploadToMinio(file, storageName);

            // 保存文件记录
            UploadFile uploadFile = new UploadFile();
            uploadFile.setOriginalName(originalName);
            uploadFile.setStorageName(storageName);
            uploadFile.setUrl(url);
            uploadFile.setSuffix(suffix);
            uploadFile.setFileSize(file.getSize());
            uploadFile.setMimeType(file.getContentType());
            uploadFile.setStorageType("minio");
            uploadFile.setUserId(SecurityUtils.getCurrentUserId());
            uploadFileMapper.insert(uploadFile);

            return url;
        } catch (Exception e) {
            log.error("文件上传失败", e);
            throw new BusinessException("文件上传失败: " + e.getMessage());
        }
    }

    @Override
    public void deleteFile(Long fileId) {
        uploadFileMapper.deleteById(fileId);
    }

    @Override
    public PageResult<UploadFile> listFiles(Integer pageNum, Integer pageSize, String mimeType) {
        Page<UploadFile> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<UploadFile> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(mimeType)) {
            wrapper.like(UploadFile::getMimeType, mimeType);
        }
        wrapper.orderByDesc(UploadFile::getCreateTime);

        Page<UploadFile> result = uploadFileMapper.selectPage(page, wrapper);
        return PageResult.of(pageNum, pageSize, result.getTotal(), result.getRecords());
    }

    /**
     * 上传文件到MinIO
     */
    private String uploadToMinio(MultipartFile file, String storageName) throws Exception {
        // 按日期组织目录
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String objectName = datePath + "/" + storageName;

        // 确保Bucket存在
        boolean bucketExists = minioClient.bucketExists(
                BucketExistsArgs.builder().bucket(minioConfig.getBucketName()).build()
        );
        if (!bucketExists) {
            minioClient.makeBucket(
                    MakeBucketArgs.builder().bucket(minioConfig.getBucketName()).build()
            );
        }

        // 上传文件
        try (InputStream inputStream = file.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioConfig.getBucketName())
                            .object(objectName)
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
        }

        return minioConfig.getEndpoint() + "/" + minioConfig.getBucketName() + "/" + objectName;
    }

    /**
     * 生成存储文件名（UUID + 后缀）
     */
    private String generateStorageName(String suffix) {
        return UUID.randomUUID().toString().replace("-", "") + suffix;
    }
}
