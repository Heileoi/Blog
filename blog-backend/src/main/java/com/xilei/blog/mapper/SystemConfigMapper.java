package com.xilei.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xilei.blog.entity.SystemConfig;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统配置Mapper接口
 * 功能：系统配置表的数据访问层
 */
@Mapper
public interface SystemConfigMapper extends BaseMapper<SystemConfig> {
}
