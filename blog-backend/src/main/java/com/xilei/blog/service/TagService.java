package com.xilei.blog.service;

import com.xilei.blog.entity.Tag;

import java.util.List;

/**
 * 标签Service接口
 * 功能：定义标签相关的业务逻辑方法
 */
public interface TagService {

    /**
     * 创建标签
     * @param tag 标签信息
     */
    void createTag(Tag tag);

    /**
     * 更新标签
     * @param tag 标签信息
     */
    void updateTag(Tag tag);

    /**
     * 删除标签
     * @param tagId 标签ID
     */
    void deleteTag(Long tagId);

    /**
     * 获取标签详情
     * @param tagId 标签ID
     * @return 标签信息
     */
    Tag getTagById(Long tagId);

    /**
     * 获取所有标签（含文章数量）
     * @return 标签列表
     */
    List<Tag> listTagsWithCount();

    /**
     * 获取热门标签
     * @param limit 数量
     * @return 热门标签列表
     */
    List<Tag> getHotTags(Integer limit);
}
