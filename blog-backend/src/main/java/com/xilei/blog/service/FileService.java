package com.xilei.blog.service;

import com.xilei.blog.common.PageResult;
import com.xilei.blog.entity.UploadFile;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件Service接口
 * 功能：定义文件上传和管理的业务逻辑方法
 */
public interface FileService {

    /**
     * 上传文件
     * @param file 文件对象
     * @return 文件访问URL
     */
    String uploadFile(MultipartFile file);

    /**
     * 删除文件
     * @param fileId 文件ID
     */
    void deleteFile(Long fileId);

    /**
     * 分页查询文件列表
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param mimeType MIME类型过滤
     * @return 分页结果
     */
    PageResult<UploadFile> listFiles(Integer pageNum, Integer pageSize, String mimeType);
}
