package com.xilei.blog.controller.front;

import com.xilei.blog.common.Result;
import com.xilei.blog.entity.Notice;
import com.xilei.blog.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 前台公告Controller
 */
@Tag(name = "前台公告", description = "前台公告展示接口")
@RestController
@RequestMapping("/front/notice")
@RequiredArgsConstructor
public class FrontNoticeController {

    private final NoticeService noticeService;

    @Operation(summary = "获取已发布公告")
    @GetMapping("/list")
    public Result<List<Notice>> listNotices() {
        return Result.success(noticeService.listPublishedNotices());
    }
}
