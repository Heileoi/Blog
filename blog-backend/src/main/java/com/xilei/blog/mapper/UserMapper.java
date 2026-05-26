package com.xilei.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xilei.blog.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper接口
 * 功能：用户表的数据访问层，继承BaseMapper提供基础CRUD操作
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
