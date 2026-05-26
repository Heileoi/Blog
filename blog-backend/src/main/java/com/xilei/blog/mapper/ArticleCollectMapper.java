package com.xilei.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xilei.blog.entity.ArticleCollect;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文章收藏Mapper接口
 * 功能：文章收藏表的数据访问层
 */
@Mapper
public interface ArticleCollectMapper extends BaseMapper<ArticleCollect> {
}
