package com.xilei.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xilei.blog.entity.Article;
import com.xilei.blog.entity.ArticleTag;
import com.xilei.blog.entity.Tag;
import com.xilei.blog.enums.ArticleStatus;
import com.xilei.blog.exception.BusinessException;
import com.xilei.blog.mapper.ArticleMapper;
import com.xilei.blog.mapper.ArticleTagMapper;
import com.xilei.blog.mapper.TagMapper;
import com.xilei.blog.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 标签Service实现类
 * 功能：实现标签的CRUD和统计查询
 */
@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagMapper tagMapper;
    private final ArticleTagMapper articleTagMapper;
    private final ArticleMapper articleMapper;

    @Override
    public void createTag(Tag tag) {
        Long count = tagMapper.selectCount(
                new LambdaQueryWrapper<Tag>().eq(Tag::getName, tag.getName())
        );
        if (count > 0) {
            throw new BusinessException("标签名称已存在");
        }
        tagMapper.insert(tag);
    }

    @Override
    public void updateTag(Tag tag) {
        tagMapper.updateById(tag);
    }

    @Override
    public void deleteTag(Long tagId) {
        // 检查标签是否被使用
        Long count = articleTagMapper.selectCount(
                new LambdaQueryWrapper<ArticleTag>().eq(ArticleTag::getTagId, tagId)
        );
        if (count > 0) {
            throw new BusinessException("该标签已被文章使用，无法删除");
        }
        tagMapper.deleteById(tagId);
    }

    @Override
    public Tag getTagById(Long tagId) {
        return tagMapper.selectById(tagId);
    }

    /**
     * 获取所有标签（含文章数量）
     */
    @Override
    public List<Tag> listTagsWithCount() {
        List<Tag> tags = tagMapper.selectList(
                new LambdaQueryWrapper<Tag>().orderByAsc(Tag::getCreateTime)
        );
        // 统计每个标签下的已发布文章数
        tags.forEach(tag -> {
            List<ArticleTag> articleTags = articleTagMapper.selectList(
                    new LambdaQueryWrapper<ArticleTag>().eq(ArticleTag::getTagId, tag.getId())
            );
            if (!articleTags.isEmpty()) {
                List<Long> articleIds = articleTags.stream()
                        .map(ArticleTag::getArticleId)
                        .collect(Collectors.toList());
                Long count = articleMapper.selectCount(
                        new LambdaQueryWrapper<Article>()
                                .in(Article::getId, articleIds)
                                .eq(Article::getStatus, ArticleStatus.PUBLISHED.getCode())
                );
                tag.setArticleCount(count.intValue());
            } else {
                tag.setArticleCount(0);
            }
        });
        return tags;
    }

    /**
     * 获取热门标签
     */
    @Override
    public List<Tag> getHotTags(Integer limit) {
        List<Tag> tags = listTagsWithCount();
        return tags.stream()
                .sorted((a, b) -> b.getArticleCount() - a.getArticleCount())
                .limit(limit)
                .collect(Collectors.toList());
    }
}
