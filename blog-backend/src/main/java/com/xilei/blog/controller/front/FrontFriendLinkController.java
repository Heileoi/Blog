package com.xilei.blog.controller.front;

import com.xilei.blog.common.Result;
import com.xilei.blog.entity.FriendLink;
import com.xilei.blog.service.FriendLinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 前台友链Controller
 */
@Tag(name = "前台友链", description = "前台友链接口")
@RestController
@RequestMapping("/front/friend-link")
@RequiredArgsConstructor
public class FrontFriendLinkController {

    private final FriendLinkService friendLinkService;

    @Operation(summary = "获取已审核的友链")
    @GetMapping("/list")
    public Result<List<FriendLink>> listFriendLinks() {
        return Result.success(friendLinkService.listApprovedFriendLinks());
    }

    @Operation(summary = "申请友链")
    @PostMapping("/apply")
    public Result<Void> applyFriendLink(@RequestBody FriendLink friendLink) {
        friendLinkService.applyFriendLink(friendLink);
        return Result.success("友链申请已提交，等待审核", null);
    }
}
