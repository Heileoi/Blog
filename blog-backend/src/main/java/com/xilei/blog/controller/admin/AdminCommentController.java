package com.xilei.blog.controller.admin;

import com.xilei.blog.common.OperationLogAnnotation;
import com.xilei.blog.common.PageResult;
import com.xilei.blog.common.Result;
import com.xilei.blog.entity.Comment;
import com.xilei.blog.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 后台评论管理Controller
 * 功能：管理员对评论的审核、删除操作
 */
@Tag(name = "后台评论管理", description = "管理员评论审核接口")
@RestController
@RequestMapping("/admin/comment")
@RequiredArgsConstructor
public class AdminCommentController {

    private final CommentService commentService;

    @Operation(summary = "分页查询评论")
    @GetMapping("/list")
    public Result<PageResult<Comment>> listComments(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status) {
        return Result.success(commentService.listCommentsForAdmin(pageNum, pageSize, keyword, status));
    }

    @Operation(summary = "审核评论")
    @OperationLogAnnotation(module = "评论管理", operation = "审核评论")
    @PutMapping("/audit")
    public Result<Void> auditComment(@RequestParam Long id, @RequestParam Integer status) {
        commentService.auditComment(id, status);
        return Result.success("评论审核成功", null);
    }

    @Operation(summary = "删除评论")
    @OperationLogAnnotation(module = "评论管理", operation = "删除评论")
    @DeleteMapping("/{id}")
    public Result<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return Result.success("评论删除成功", null);
    }
}
