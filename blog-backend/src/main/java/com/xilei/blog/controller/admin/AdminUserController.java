package com.xilei.blog.controller.admin;

import com.xilei.blog.common.PageResult;
import com.xilei.blog.common.Result;
import com.xilei.blog.entity.User;
import com.xilei.blog.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 后台用户管理Controller
 * 功能：管理员对用户的管理操作
 */
@Tag(name = "后台用户管理", description = "管理员用户管理接口")
@RestController
@RequestMapping("/admin/user")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;

    @Operation(summary = "分页查询用户")
    @GetMapping("/list")
    public Result<PageResult<User>> listUsers(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword) {
        return Result.success(userService.listUsers(pageNum, pageSize, keyword));
    }

    @Operation(summary = "获取用户详情")
    @GetMapping("/{id}")
    public Result<User> getUser(@PathVariable Long id) {
        return Result.success(userService.getUserById(id));
    }

    @Operation(summary = "更新用户状态")
    @PutMapping("/status")
    public Result<Void> updateStatus(@RequestParam Long id, @RequestParam Integer status) {
        userService.updateUserStatus(id, status);
        return Result.success("用户状态更新成功", null);
    }

    @Operation(summary = "更新用户角色")
    @PutMapping("/role")
    public Result<Void> updateRole(@RequestParam Long id, @RequestParam Integer role) {
        userService.updateUserRole(id, role);
        return Result.success("用户角色更新成功", null);
    }
}
