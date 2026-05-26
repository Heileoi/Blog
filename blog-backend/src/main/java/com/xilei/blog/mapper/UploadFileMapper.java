package com.xilei.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xilei.blog.entity.UploadFile;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文件上传Mapper接口
 * 功能：文件上传表的数据访问层
 */
@Mapper
public interface UploadFileMapper extends BaseMapper<UploadFile> {
}
