package com.xilei.blog.service;

import com.xilei.blog.common.PageResult;
import com.xilei.blog.dto.ArticleDTO;
import com.xilei.blog.entity.Article;

import java.util.List;
import java.util.Map;

/**
 * 文章Service接口
 * 功能：定义文章相关的业务逻辑方法
 */
public interface ArticleService {

    /**
     * 创建文章
     * @param articleDTO 文章DTO
     * @return 文章ID
     */
    Long createArticle(ArticleDTO articleDTO);

    /**
     * 更新文章
     * @param articleDTO 文章DTO
     */
    void updateArticle(ArticleDTO articleDTO);

    /**
     * 删除文章（逻辑删除）
     * @param articleId 文章ID
     */
    void deleteArticle(Long articleId);

    /**
     * 获取文章详情
     * @param articleId 文章ID
     * @return 文章详情（包含分类和标签）
     */
    Article getArticleDetail(Long articleId);

    /**
     * 获取文章详情（增加浏览量）
     * @param articleId 文章ID
     * @return 文章详情
     */
    Article getArticleAndIncreaseView(Long articleId);

    /**
     * 分页查询文章列表（管理员）
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param keyword 搜索关键词
     * @param categoryId 分类ID
     * @param status 文章状态
     * @return 分页结果
     */
    PageResult<Article> listArticlesForAdmin(Integer pageNum, Integer pageSize,
                                              String keyword, Long categoryId, Integer status);

    /**
     * 分页查询已发布文章列表（前台）
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param categoryId 分类ID
     * @param tagId 标签ID
     * @param keyword 搜索关键词
     * @return 分页结果
     */
    PageResult<Article> listPublishedArticles(Integer pageNum, Integer pageSize,
                                               Long categoryId, Long tagId, String keyword);

    /**
     * 获取热门文章
     * @param limit 数量
     * @return 热门文章列表
     */
    List<Article> getHotArticles(Integer limit);

    /**
     * 获取推荐文章
     * @param limit 数量
     * @return 推荐文章列表
     */
    List<Article> getFeaturedArticles(Integer limit);

    /**
     * 获取最新文章
     * @param limit 数量
     * @return 最新文章列表
     */
    List<Article> getLatestArticles(Integer limit);

    /**
     * 获取文章归档
     * @return 归档列表（按年月分组）
     */
    List<Map<String, Object>> getArchives();

    /**
     * 点赞文章
     * @param articleId 文章ID
     */
    void likeArticle(Long articleId);

    /**
     * 更新文章状态
     * @param articleId 文章ID
     * @param status 状态
     */
    void updateArticleStatus(Long articleId, Integer status);

    /**
     * 更新文章置顶状态
     * @param articleId 文章ID
     * @param isTop 是否置顶
     */
    void updateArticleTop(Long articleId, Integer isTop);
}
