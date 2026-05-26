package com.xilei.blog.controller.front;

import com.xilei.blog.common.PageResult;
import com.xilei.blog.common.Result;
import com.xilei.blog.entity.Article;
import com.xilei.blog.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 前台文章Controller
 * 功能：提供前台博客的文章展示接口（无需认证）
 * 路径：/api/front/article/*
 */
@Tag(name = "前台文章", description = "前台文章展示接口")
@RestController
@RequestMapping("/front/article")
@RequiredArgsConstructor
public class FrontArticleController {

    private final ArticleService articleService;

    /**
     * 获取文章详情（增加浏览量）
     */
    @Operation(summary = "获取文章详情")
    @GetMapping("/{id}")
    public Result<Article> getArticle(@PathVariable Long id) {
        return Result.success(articleService.getArticleAndIncreaseView(id));
    }

    /**
     * 分页查询已发布文章
     */
    @Operation(summary = "分页查询文章列表")
    @GetMapping("/list")
    public Result<PageResult<Article>> listArticles(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long tagId,
            @RequestParam(required = false) String keyword) {
        return Result.success(articleService.listPublishedArticles(pageNum, pageSize, categoryId, tagId, keyword));
    }

    /**
     * 获取热门文章
     */
    @Operation(summary = "获取热门文章")
    @GetMapping("/hot")
    public Result<List<Article>> getHotArticles(
            @RequestParam(defaultValue = "10") Integer limit) {
        return Result.success(articleService.getHotArticles(limit));
    }

    /**
     * 获取推荐文章
     */
    @Operation(summary = "获取推荐文章")
    @GetMapping("/featured")
    public Result<List<Article>> getFeaturedArticles(
            @RequestParam(defaultValue = "5") Integer limit) {
        return Result.success(articleService.getFeaturedArticles(limit));
    }

    /**
     * 获取最新文章
     */
    @Operation(summary = "获取最新文章")
    @GetMapping("/latest")
    public Result<List<Article>> getLatestArticles(
            @RequestParam(defaultValue = "5") Integer limit) {
        return Result.success(articleService.getLatestArticles(limit));
    }

    /**
     * 获取文章归档
     */
    @Operation(summary = "获取文章归档")
    @GetMapping("/archives")
    public Result<List<Map<String, Object>>> getArchives() {
        return Result.success(articleService.getArchives());
    }

    /**
     * 点赞文章
     */
    @Operation(summary = "点赞文章")
    @PostMapping("/like/{id}")
    public Result<Void> likeArticle(@PathVariable Long id) {
        articleService.likeArticle(id);
        return Result.success("点赞成功", null);
    }
}
