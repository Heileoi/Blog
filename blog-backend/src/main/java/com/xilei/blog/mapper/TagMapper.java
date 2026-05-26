package com.xilei.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xilei.blog.entity.Tag;
import org.apache.ibatis.annotations.Mapper;

/**
 * 标签Mapper接口
 * 功能：标签表的数据访问层
 */
@Mapper
public interface TagMapper extends BaseMapper<Tag> {
}
