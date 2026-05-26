package com.xilei.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xilei.blog.entity.Category;
import org.apache.ibatis.annotations.Mapper;

/**
 * 分类Mapper接口
 * 功能：分类表的数据访问层
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
