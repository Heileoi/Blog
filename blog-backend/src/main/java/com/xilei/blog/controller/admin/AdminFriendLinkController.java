package com.xilei.blog.controller.admin;

import com.xilei.blog.common.Result;
import com.xilei.blog.entity.FriendLink;
import com.xilei.blog.service.FriendLinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 后台友链管理Controller
 */
@Tag(name = "后台友链管理", description = "管理员友链管理接口")
@RestController
@RequestMapping("/admin/friend-link")
@RequiredArgsConstructor
public class AdminFriendLinkController {

    private final FriendLinkService friendLinkService;

    @Operation(summary = "获取所有友链")
    @GetMapping("/list")
    public Result<List<FriendLink>> listFriendLinks() {
        return Result.success(friendLinkService.listAllFriendLinks());
    }

    @Operation(summary = "审核友链")
    @PutMapping("/audit")
    public Result<Void> auditFriendLink(@RequestParam Long id, @RequestParam Integer status) {
        friendLinkService.auditFriendLink(id, status);
        return Result.success("友链审核成功", null);
    }

    @Operation(summary = "更新友链")
    @PutMapping
    public Result<Void> updateFriendLink(@RequestBody FriendLink friendLink) {
        friendLinkService.updateFriendLink(friendLink);
        return Result.success("友链更新成功", null);
    }

    @Operation(summary = "删除友链")
    @DeleteMapping("/{id}")
    public Result<Void> deleteFriendLink(@PathVariable Long id) {
        friendLinkService.deleteFriendLink(id);
        return Result.success("友链删除成功", null);
    }
}
