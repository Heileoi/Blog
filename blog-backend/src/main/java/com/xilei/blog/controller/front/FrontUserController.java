package com.xilei.blog.controller.front;

import com.xilei.blog.common.Result;
import com.xilei.blog.entity.User;
import com.xilei.blog.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 前台用户Controller
 * 功能：获取博主信息等公开接口
 */
@Tag(name = "前台用户", description = "前台用户信息接口")
@RestController
@RequestMapping("/front/user")
@RequiredArgsConstructor
public class FrontUserController {

    private final UserService userService;

    @Operation(summary = "获取用户公开信息")
    @GetMapping("/{id}")
    public Result<User> getUserInfo(@PathVariable Long id) {
        return Result.success(userService.getUserById(id));
    }
}
