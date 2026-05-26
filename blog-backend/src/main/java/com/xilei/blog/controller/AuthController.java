package com.xilei.blog.controller;

import com.xilei.blog.common.OperationLogAnnotation;
import com.xilei.blog.common.Result;
import com.xilei.blog.dto.LoginRequest;
import com.xilei.blog.dto.RegisterRequest;
import com.xilei.blog.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 认证Controller
 * 功能：处理用户登录、注册等认证相关请求
 * 路径：/api/auth/*
 */
@Tag(name = "认证管理", description = "用户登录、注册接口")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    /**
     * 用户登录
     * POST /api/auth/login
     */
    @Operation(summary = "用户登录")
    @OperationLogAnnotation(module = "认证管理", operation = "用户登录")
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@Valid @RequestBody LoginRequest request) {
        Map<String, Object> result = userService.login(request);
        return Result.success("登录成功", result);
    }

    /**
     * 用户注册
     * POST /api/auth/register
     */
    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody RegisterRequest request) {
        userService.register(request);
        return Result.success("注册成功", null);
    }
}
