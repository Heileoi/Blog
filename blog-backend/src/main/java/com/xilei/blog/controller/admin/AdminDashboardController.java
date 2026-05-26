package com.xilei.blog.controller.admin;

import com.xilei.blog.common.Result;
import com.xilei.blog.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 后台仪表盘Controller
 * 功能：提供后台首页的统计数据
 */
@Tag(name = "后台仪表盘", description = "统计数据接口")
@RestController
@RequestMapping("/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final DashboardService dashboardService;

    @Operation(summary = "获取统计数据")
    @GetMapping("/stats")
    public Result<Map<String, Object>> getStats() {
        return Result.success(dashboardService.getDashboardStats());
    }

    @Operation(summary = "获取文章发布趋势")
    @GetMapping("/trend")
    public Result<Map<String, Object>> getTrend() {
        return Result.success(dashboardService.getArticleTrend());
    }
}
