package com.xilei.blog.controller.front;

import com.xilei.blog.common.PageResult;
import com.xilei.blog.common.Result;
import com.xilei.blog.dto.CommentDTO;
import com.xilei.blog.entity.Comment;
import com.xilei.blog.service.CommentService;
import com.xilei.blog.utils.IpUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 前台评论Controller
 * 功能：提供前台评论提交和查询接口
 */
@Tag(name = "前台评论", description = "前台评论接口")
@RestController
@RequestMapping("/front/comment")
@RequiredArgsConstructor
public class FrontCommentController {

    private final CommentService commentService;

    /**
     * 提交评论
     */
    @Operation(summary = "提交评论")
    @PostMapping
    public Result<Void> createComment(@Valid @RequestBody CommentDTO commentDTO,
                                       HttpServletRequest request) {
        String ipAddress = IpUtils.getClientIp(request);
        String userAgent = request.getHeader("User-Agent");
        commentService.createComment(commentDTO, ipAddress, userAgent);
        return Result.success("评论提交成功", null);
    }

    /**
     * 获取文章评论列表
     */
    @Operation(summary = "获取文章评论列表")
    @GetMapping("/article/{articleId}")
    public Result<PageResult<Comment>> listComments(
            @PathVariable Long articleId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "20") Integer pageSize) {
        return Result.success(commentService.listCommentsByArticle(articleId, pageNum, pageSize));
    }

    /**
     * 获取最新评论
     */
    @Operation(summary = "获取最新评论")
    @GetMapping("/latest")
    public Result<List<Comment>> getLatestComments(
            @RequestParam(defaultValue = "10") Integer limit) {
        return Result.success(commentService.getLatestComments(limit));
    }
}
