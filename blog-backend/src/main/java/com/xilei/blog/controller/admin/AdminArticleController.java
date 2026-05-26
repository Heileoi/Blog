package com.xilei.blog.controller.admin;

import com.xilei.blog.common.OperationLogAnnotation;
import com.xilei.blog.common.PageResult;
import com.xilei.blog.common.Result;
import com.xilei.blog.dto.ArticleDTO;
import com.xilei.blog.entity.Article;
import com.xilei.blog.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 后台文章管理Controller
 * 功能：管理员对文章的增删改查操作
 * 路径：/api/admin/article/*
 */
@Tag(name = "后台文章管理", description = "管理员文章CRUD接口")
@RestController
@RequestMapping("/admin/article")
@RequiredArgsConstructor
public class AdminArticleController {

    private final ArticleService articleService;

    /**
     * 创建文章
     * POST /api/admin/article
     */
    @Operation(summary = "创建文章")
    @OperationLogAnnotation(module = "文章管理", operation = "创建文章")
    @PostMapping
    public Result<Long> createArticle(@Valid @RequestBody ArticleDTO articleDTO) {
        Long articleId = articleService.createArticle(articleDTO);
        return Result.success("文章创建成功", articleId);
    }

    /**
     * 更新文章
     * PUT /api/admin/article
     */
    @Operation(summary = "更新文章")
    @OperationLogAnnotation(module = "文章管理", operation = "更新文章")
    @PutMapping
    public Result<Void> updateArticle(@Valid @RequestBody ArticleDTO articleDTO) {
        articleService.updateArticle(articleDTO);
        return Result.success("文章更新成功", null);
    }

    /**
     * 删除文章
     * DELETE /api/admin/article/{id}
     */
    @Operation(summary = "删除文章")
    @OperationLogAnnotation(module = "文章管理", operation = "删除文章")
    @DeleteMapping("/{id}")
    public Result<Void> deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return Result.success("文章删除成功", null);
    }

    /**
     * 获取文章详情
     * GET /api/admin/article/{id}
     */
    @Operation(summary = "获取文章详情")
    @GetMapping("/{id}")
    public Result<Article> getArticle(@PathVariable Long id) {
        return Result.success(articleService.getArticleDetail(id));
    }

    /**
     * 分页查询文章列表
     * GET /api/admin/article/list
     */
    @Operation(summary = "分页查询文章列表")
    @GetMapping("/list")
    public Result<PageResult<Article>> listArticles(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Integer status) {
        return Result.success(articleService.listArticlesForAdmin(pageNum, pageSize, keyword, categoryId, status));
    }

    /**
     * 更新文章状态
     * PUT /api/admin/article/status
     */
    @Operation(summary = "更新文章状态")
    @PutMapping("/status")
    public Result<Void> updateStatus(@RequestParam Long id, @RequestParam Integer status) {
        articleService.updateArticleStatus(id, status);
        return Result.success("状态更新成功", null);
    }

    /**
     * 更新文章置顶状态
     * PUT /api/admin/article/top
     */
    @Operation(summary = "更新文章置顶")
    @PutMapping("/top")
    public Result<Void> updateTop(@RequestParam Long id, @RequestParam Integer isTop) {
        articleService.updateArticleTop(id, isTop);
        return Result.success("置顶状态更新成功", null);
    }
}
