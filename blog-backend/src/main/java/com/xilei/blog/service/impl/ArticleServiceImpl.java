package com.xilei.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xilei.blog.common.PageResult;
import com.xilei.blog.dto.ArticleDTO;
import com.xilei.blog.entity.Article;
import com.xilei.blog.entity.ArticleTag;
import com.xilei.blog.entity.Category;
import com.xilei.blog.entity.Tag;
import com.xilei.blog.enums.ArticleStatus;
import com.xilei.blog.exception.BusinessException;
import com.xilei.blog.mapper.ArticleMapper;
import com.xilei.blog.mapper.ArticleTagMapper;
import com.xilei.blog.mapper.CategoryMapper;
import com.xilei.blog.mapper.TagMapper;
import com.xilei.blog.service.ArticleService;
import com.xilei.blog.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 文章Service实现类
 * 功能：实现文章的CRUD、分页查询、归档统计等业务逻辑
 */
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleMapper articleMapper;
    private final ArticleTagMapper articleTagMapper;
    private final CategoryMapper categoryMapper;
    private final TagMapper tagMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 创建文章
     * 流程：1.保存文章基本信息 2.关联标签 3.更新分类文章数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {"hotArticles", "featuredArticles", "latestArticles", "archives"}, allEntries = true)
    public Long createArticle(ArticleDTO articleDTO) {
        Article article = new Article();
        BeanUtils.copyProperties(articleDTO, article);

        // 设置作者
        article.setUserId(SecurityUtils.getCurrentUserId());
        // 设置默认值
        if (article.getViewCount() == null) article.setViewCount(0);
        if (article.getLikeCount() == null) article.setLikeCount(0);
        if (article.getCommentCount() == null) article.setCommentCount(0);
        if (article.getCollectCount() == null) article.setCollectCount(0);
        // 如果是发布状态，设置发布时间
        if (ArticleStatus.PUBLISHED.getCode().equals(article.getStatus())) {
            article.setPublishTime(LocalDateTime.now());
        }

        // 自动生成摘要（如果未填写）
        if (!StringUtils.hasText(article.getSummary()) && StringUtils.hasText(article.getContent())) {
            article.setSummary(generateSummary(article.getContent()));
        }

        articleMapper.insert(article);

        // 保存文章标签关联
        saveArticleTags(article.getId(), articleDTO.getTagIds());

        return article.getId();
    }

    /**
     * 更新文章
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {"hotArticles", "featuredArticles", "latestArticles", "archives"}, allEntries = true)
    public void updateArticle(ArticleDTO articleDTO) {
        if (articleDTO.getId() == null) {
            throw new BusinessException("文章ID不能为空");
        }

        Article article = articleMapper.selectById(articleDTO.getId());
        if (article == null) {
            throw new BusinessException("文章不存在");
        }

        BeanUtils.copyProperties(articleDTO, article, "id", "userId", "viewCount", "likeCount",
                "commentCount", "collectCount", "createTime");

        // 如果状态改为发布，设置发布时间
        if (ArticleStatus.PUBLISHED.getCode().equals(articleDTO.getStatus())
                && article.getPublishTime() == null) {
            article.setPublishTime(LocalDateTime.now());
        }

        // 自动生成摘要
        if (!StringUtils.hasText(article.getSummary()) && StringUtils.hasText(article.getContent())) {
            article.setSummary(generateSummary(article.getContent()));
        }

        articleMapper.updateById(article);

        // 更新标签关联（先删后增）
        articleTagMapper.delete(new LambdaQueryWrapper<ArticleTag>()
                .eq(ArticleTag::getArticleId, article.getId()));
        saveArticleTags(article.getId(), articleDTO.getTagIds());
    }

    /**
     * 删除文章
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {"hotArticles", "featuredArticles", "latestArticles", "archives"}, allEntries = true)
    public void deleteArticle(Long articleId) {
        articleMapper.deleteById(articleId);
        // 删除标签关联
        articleTagMapper.delete(new LambdaQueryWrapper<ArticleTag>()
                .eq(ArticleTag::getArticleId, articleId));
    }

    /**
     * 获取文章详情
     */
    @Override
    public Article getArticleDetail(Long articleId) {
        Article article = articleMapper.selectById(articleId);
        if (article == null) {
            throw new BusinessException("文章不存在");
        }
        fillArticleExtra(article);
        return article;
    }

    /**
     * 获取文章详情并增加浏览量
     */
    @Override
    public Article getArticleAndIncreaseView(Long articleId) {
        Article article = articleMapper.selectById(articleId);
        if (article == null) {
            throw new BusinessException("文章不存在");
        }

        // 使用Redis防重复计数（同一IP 5分钟内不重复计数）
        String viewKey = "article:view:" + articleId + ":" + SecurityUtils.getCurrentUserId();
        Boolean hasViewed = redisTemplate.hasKey(viewKey);
        if (!Boolean.TRUE.equals(hasViewed)) {
            articleMapper.incrementViewCount(articleId);
            article.setViewCount(article.getViewCount() + 1);
            redisTemplate.opsForValue().set(viewKey, "1", 5, TimeUnit.MINUTES);
        }

        fillArticleExtra(article);
        return article;
    }

    /**
     * 分页查询文章列表（管理员）
     */
    @Override
    public PageResult<Article> listArticlesForAdmin(Integer pageNum, Integer pageSize,
                                                     String keyword, Long categoryId, Integer status) {
        Page<Article> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like(Article::getTitle, keyword)
                    .or().like(Article::getSummary, keyword));
        }
        if (categoryId != null) {
            wrapper.eq(Article::getCategoryId, categoryId);
        }
        if (status != null) {
            wrapper.eq(Article::getStatus, status);
        }
        wrapper.orderByDesc(Article::getCreateTime);

        Page<Article> result = articleMapper.selectPage(page, wrapper);
        result.getRecords().forEach(this::fillArticleExtra);

        return PageResult.of(pageNum, pageSize, result.getTotal(), result.getRecords());
    }

    /**
     * 分页查询已发布文章列表（前台）
     */
    @Override
    public PageResult<Article> listPublishedArticles(Integer pageNum, Integer pageSize,
                                                      Long categoryId, Long tagId, String keyword) {
        Page<Article> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(Article::getStatus, ArticleStatus.PUBLISHED.getCode());

        if (categoryId != null) {
            wrapper.eq(Article::getCategoryId, categoryId);
        }
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like(Article::getTitle, keyword)
                    .or().like(Article::getKeywords, keyword));
        }
        // 置顶文章优先，然后按发布时间倒序
        wrapper.orderByDesc(Article::getIsTop)
                .orderByDesc(Article::getPublishTime);

        Page<Article> result = articleMapper.selectPage(page, wrapper);
        result.getRecords().forEach(this::fillArticleExtra);

        return PageResult.of(pageNum, pageSize, result.getTotal(), result.getRecords());
    }

    /**
     * 获取热门文章
     */
    @Override
    @Cacheable(value = "hotArticles", key = "#limit")
    public List<Article> getHotArticles(Integer limit) {
        List<Article> articles = articleMapper.selectList(
                new LambdaQueryWrapper<Article>()
                        .eq(Article::getStatus, ArticleStatus.PUBLISHED.getCode())
                        .orderByDesc(Article::getViewCount)
                        .last("LIMIT " + limit)
        );
        articles.forEach(this::fillArticleExtra);
        return articles;
    }

    /**
     * 获取推荐文章
     */
    @Override
    @Cacheable(value = "featuredArticles", key = "#limit")
    public List<Article> getFeaturedArticles(Integer limit) {
        List<Article> articles = articleMapper.selectList(
                new LambdaQueryWrapper<Article>()
                        .eq(Article::getStatus, ArticleStatus.PUBLISHED.getCode())
                        .eq(Article::getIsFeatured, 1)
                        .orderByDesc(Article::getPublishTime)
                        .last("LIMIT " + limit)
        );
        articles.forEach(this::fillArticleExtra);
        return articles;
    }

    /**
     * 获取最新文章
     */
    @Override
    @Cacheable(value = "latestArticles", key = "#limit")
    public List<Article> getLatestArticles(Integer limit) {
        List<Article> articles = articleMapper.selectList(
                new LambdaQueryWrapper<Article>()
                        .eq(Article::getStatus, ArticleStatus.PUBLISHED.getCode())
                        .orderByDesc(Article::getPublishTime)
                        .last("LIMIT " + limit)
        );
        articles.forEach(this::fillArticleExtra);
        return articles;
    }

    /**
     * 获取文章归档
     */
    @Override
    @Cacheable(value = "archives")
    public List<Map<String, Object>> getArchives() {
        return articleMapper.selectArchives();
    }

    /**
     * 点赞文章（Redis防重复点赞）
     */
    @Override
    public void likeArticle(Long articleId) {
        String likeKey = "article:like:" + articleId;
        Long userId = SecurityUtils.getCurrentUserId();
        String member = userId != null ? String.valueOf(userId) : "anonymous";

        Boolean isMember = redisTemplate.opsForSet().isMember(likeKey, member);
        if (Boolean.TRUE.equals(isMember)) {
            throw new BusinessException("您已经点赞过这篇文章了");
        }

        articleMapper.incrementLikeCount(articleId);
        redisTemplate.opsForSet().add(likeKey, member);
    }

    /**
     * 更新文章状态
     */
    @Override
    @CacheEvict(value = {"hotArticles", "featuredArticles", "latestArticles", "archives"}, allEntries = true)
    public void updateArticleStatus(Long articleId, Integer status) {
        Article article = articleMapper.selectById(articleId);
        if (article == null) {
            throw new BusinessException("文章不存在");
        }
        article.setStatus(status);
        if (ArticleStatus.PUBLISHED.getCode().equals(status) && article.getPublishTime() == null) {
            article.setPublishTime(LocalDateTime.now());
        }
        articleMapper.updateById(article);
    }

    /**
     * 更新文章置顶状态
     */
    @Override
    @CacheEvict(value = {"hotArticles", "featuredArticles", "latestArticles"}, allEntries = true)
    public void updateArticleTop(Long articleId, Integer isTop) {
        Article article = articleMapper.selectById(articleId);
        if (article == null) {
            throw new BusinessException("文章不存在");
        }
        article.setIsTop(isTop);
        articleMapper.updateById(article);
    }

    /**
     * 保存文章标签关联
     */
    private void saveArticleTags(Long articleId, List<Long> tagIds) {
        if (CollectionUtils.isEmpty(tagIds)) return;
        for (Long tagId : tagIds) {
            ArticleTag articleTag = new ArticleTag();
            articleTag.setArticleId(articleId);
            articleTag.setTagId(tagId);
            articleTagMapper.insert(articleTag);
        }
    }

    /**
     * 填充文章额外信息（分类名、标签列表）
     */
    private void fillArticleExtra(Article article) {
        // 填充分类名
        if (article.getCategoryId() != null) {
            Category category = categoryMapper.selectById(article.getCategoryId());
            if (category != null) {
                article.setCategoryName(category.getName());
            }
        }
        // 填充标签列表
        List<ArticleTag> articleTags = articleTagMapper.selectList(
                new LambdaQueryWrapper<ArticleTag>().eq(ArticleTag::getArticleId, article.getId())
        );
        if (!CollectionUtils.isEmpty(articleTags)) {
            List<Long> tagIds = articleTags.stream()
                    .map(ArticleTag::getTagId)
                    .collect(Collectors.toList());
            List<Tag> tags = tagMapper.selectBatchIds(tagIds);
            article.setTags(tags);
        }
    }

    /**
     * 生成文章摘要（截取前200个字符）
     */
    private String generateSummary(String content) {
        if (!StringUtils.hasText(content)) return "";
        // 移除Markdown标记
        String plainText = content.replaceAll("[#*`>\\-\\[\\]()!]", "")
                .replaceAll("\n+", " ")
                .trim();
        return plainText.length() > 200 ? plainText.substring(0, 200) + "..." : plainText;
    }
}
