package com.xilei.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xilei.blog.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

/**
 * 评论Mapper接口
 * 功能：评论表的数据访问层
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
}
