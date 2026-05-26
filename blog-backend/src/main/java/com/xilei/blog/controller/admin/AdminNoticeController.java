package com.xilei.blog.controller.admin;

import com.xilei.blog.common.Result;
import com.xilei.blog.entity.Notice;
import com.xilei.blog.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台公告管理Controller
 */
@Tag(name = "后台公告管理", description = "管理员公告CRUD接口")
@RestController
@RequestMapping("/admin/notice")
@RequiredArgsConstructor
public class AdminNoticeController {

    private final NoticeService noticeService;

    @Operation(summary = "创建公告")
    @PostMapping
    public Result<Void> createNotice(@RequestBody Notice notice) {
        noticeService.createNotice(notice);
        return Result.success("公告创建成功", null);
    }

    @Operation(summary = "更新公告")
    @PutMapping
    public Result<Void> updateNotice(@RequestBody Notice notice) {
        noticeService.updateNotice(notice);
        return Result.success("公告更新成功", null);
    }

    @Operation(summary = "删除公告")
    @DeleteMapping("/{id}")
    public Result<Void> deleteNotice(@PathVariable Long id) {
        noticeService.deleteNotice(id);
        return Result.success("公告删除成功", null);
    }

    @Operation(summary = "获取所有公告")
    @GetMapping("/list")
    public Result<List<Notice>> listNotices() {
        return Result.success(noticeService.listAllNotices());
    }
}
