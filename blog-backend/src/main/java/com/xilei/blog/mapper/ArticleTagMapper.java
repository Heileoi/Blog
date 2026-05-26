package com.xilei.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xilei.blog.entity.ArticleTag;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文章-标签关联Mapper接口
 * 功能：文章标签关联表的数据访问层
 */
@Mapper
public interface ArticleTagMapper extends BaseMapper<ArticleTag> {
}
