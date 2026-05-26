package com.xilei.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xilei.blog.entity.Notice;
import org.apache.ibatis.annotations.Mapper;

/**
 * 公告Mapper接口
 * 功能：公告表的数据访问层
 */
@Mapper
public interface NoticeMapper extends BaseMapper<Notice> {
}
