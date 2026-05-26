package com.xilei.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xilei.blog.entity.OperationLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 操作日志Mapper接口
 * 功能：操作日志表的数据访问层
 */
@Mapper
public interface OperationLogMapper extends BaseMapper<OperationLog> {
}
