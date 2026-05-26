package com.xilei.blog.controller.admin;

import com.xilei.blog.common.Result;
import com.xilei.blog.dto.PasswordDTO;
import com.xilei.blog.dto.UserDTO;
import com.xilei.blog.entity.User;
import com.xilei.blog.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员个人信息Controller
 * 功能：管理员查看和修改个人信息、修改密码
 */
@Tag(name = "管理员个人信息", description = "个人信息管理接口")
@RestController
@RequestMapping("/admin/profile")
@RequiredArgsConstructor
public class AdminProfileController {

    private final UserService userService;

    @Operation(summary = "获取个人信息")
    @GetMapping
    public Result<User> getProfile() {
        return Result.success(userService.getCurrentUserInfo());
    }

    @Operation(summary = "更新个人信息")
    @PutMapping
    public Result<Void> updateProfile(@Valid @RequestBody UserDTO userDTO) {
        userService.updateProfile(userDTO);
        return Result.success("个人信息更新成功", null);
    }

    @Operation(summary = "修改密码")
    @PutMapping("/password")
    public Result<Void> changePassword(@Valid @RequestBody PasswordDTO passwordDTO) {
        userService.changePassword(passwordDTO.getOldPassword(), passwordDTO.getNewPassword());
        return Result.success("密码修改成功", null);
    }
}
