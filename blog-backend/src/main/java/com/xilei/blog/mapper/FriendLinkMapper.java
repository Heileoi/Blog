package com.xilei.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xilei.blog.entity.FriendLink;
import org.apache.ibatis.annotations.Mapper;

/**
 * 友情链接Mapper接口
 * 功能：友情链接表的数据访问层
 */
@Mapper
public interface FriendLinkMapper extends BaseMapper<FriendLink> {
}
