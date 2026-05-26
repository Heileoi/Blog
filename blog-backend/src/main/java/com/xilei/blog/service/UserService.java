package com.xilei.blog.service;

import com.xilei.blog.common.PageResult;
import com.xilei.blog.dto.LoginRequest;
import com.xilei.blog.dto.RegisterRequest;
import com.xilei.blog.dto.UserDTO;
import com.xilei.blog.entity.User;

import java.util.Map;

/**
 * 用户Service接口
 * 功能：定义用户相关的业务逻辑方法
 */
public interface UserService {

    /**
     * 用户登录
     * @param request 登录请求（用户名、密码）
     * @return 包含token和用户信息的Map
     */
    Map<String, Object> login(LoginRequest request);

    /**
     * 用户注册
     * @param request 注册请求
     */
    void register(RegisterRequest request);

    /**
     * 获取当前登录用户信息
     * @return 用户信息
     */
    User getCurrentUserInfo();

    /**
     * 更新用户个人信息
     * @param userDTO 用户信息DTO
     */
    void updateProfile(UserDTO userDTO);

    /**
     * 修改密码
     * @param oldPassword 原密码
     * @param newPassword 新密码
     */
    void changePassword(String oldPassword, String newPassword);

    /**
     * 分页查询用户列表（管理员）
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param keyword 搜索关键词
     * @return 分页结果
     */
    PageResult<User> listUsers(Integer pageNum, Integer pageSize, String keyword);

    /**
     * 更新用户状态（管理员）
     * @param userId 用户ID
     * @param status 状态
     */
    void updateUserStatus(Long userId, Integer status);

    /**
     * 更新用户角色（管理员）
     * @param userId 用户ID
     * @param role 角色
     */
    void updateUserRole(Long userId, Integer role);

    /**
     * 根据ID获取用户信息
     * @param userId 用户ID
     * @return 用户信息
     */
    User getUserById(Long userId);
}
