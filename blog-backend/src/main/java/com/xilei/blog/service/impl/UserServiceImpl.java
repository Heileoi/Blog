package com.xilei.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xilei.blog.common.PageResult;
import com.xilei.blog.dto.LoginRequest;
import com.xilei.blog.dto.RegisterRequest;
import com.xilei.blog.dto.UserDTO;
import com.xilei.blog.entity.User;
import com.xilei.blog.exception.BusinessException;
import com.xilei.blog.mapper.UserMapper;
import com.xilei.blog.security.LoginUser;
import com.xilei.blog.service.UserService;
import com.xilei.blog.utils.JwtUtils;
import com.xilei.blog.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户Service实现类
 * 功能：实现用户相关的业务逻辑，包括登录、注册、个人信息管理等
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    /**
     * 用户登录
     * 流程：1.验证用户名密码 2.生成JWT Token 3.返回用户信息和Token
     */
    @Override
    public Map<String, Object> login(LoginRequest request) {
        // 使用Spring Security进行认证
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        // 获取认证成功的用户信息
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();

        // 生成JWT Token
        String token = jwtUtils.generateToken(loginUser.getUserId(), loginUser.getUsername());

        // 构建返回数据
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userId", loginUser.getUserId());
        result.put("username", loginUser.getUsername());
        result.put("nickname", loginUser.getNickname());
        result.put("avatar", loginUser.getAvatar());
        result.put("role", loginUser.isAdmin() ? "admin" : "user");

        return result;
    }

    /**
     * 用户注册
     * 流程：1.校验用户名唯一 2.加密密码 3.保存用户
     */
    @Override
    public void register(RegisterRequest request) {
        // 检查用户名是否已存在
        Long count = userMapper.selectCount(
                new LambdaQueryWrapper<User>().eq(User::getUsername, request.getUsername())
        );
        if (count > 0) {
            throw new BusinessException("用户名已存在");
        }

        // 检查邮箱是否已存在
        if (StringUtils.hasText(request.getEmail())) {
            Long emailCount = userMapper.selectCount(
                    new LambdaQueryWrapper<User>().eq(User::getEmail, request.getEmail())
            );
            if (emailCount > 0) {
                throw new BusinessException("邮箱已被注册");
            }
        }

        // 创建用户
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNickname(request.getNickname());
        user.setEmail(request.getEmail());
        user.setAvatar("/default-avatar.png");
        user.setRole(0);  // 默认普通用户
        user.setStatus(1); // 默认启用

        userMapper.insert(user);
    }

    /**
     * 获取当前登录用户信息
     */
    @Override
    public User getCurrentUserInfo() {
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            throw new BusinessException(401, "未登录");
        }
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        user.setPassword(null); // 不返回密码
        return user;
    }

    /**
     * 更新用户个人信息
     */
    @Override
    public void updateProfile(UserDTO userDTO) {
        Long userId = SecurityUtils.getCurrentUserId();
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        if (StringUtils.hasText(userDTO.getNickname())) user.setNickname(userDTO.getNickname());
        if (StringUtils.hasText(userDTO.getEmail())) user.setEmail(userDTO.getEmail());
        if (StringUtils.hasText(userDTO.getAvatar())) user.setAvatar(userDTO.getAvatar());
        if (userDTO.getBio() != null) user.setBio(userDTO.getBio());
        if (userDTO.getWebsite() != null) user.setWebsite(userDTO.getWebsite());
        if (userDTO.getGithub() != null) user.setGithub(userDTO.getGithub());

        userMapper.updateById(user);
    }

    /**
     * 修改密码
     */
    @Override
    public void changePassword(String oldPassword, String newPassword) {
        Long userId = SecurityUtils.getCurrentUserId();
        User user = userMapper.selectById(userId);

        // 验证原密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BusinessException("原密码错误");
        }

        // 更新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        userMapper.updateById(user);
    }

    /**
     * 分页查询用户列表
     */
    @Override
    public PageResult<User> listUsers(Integer pageNum, Integer pageSize, String keyword) {
        Page<User> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w
                    .like(User::getUsername, keyword)
                    .or().like(User::getNickname, keyword)
                    .or().like(User::getEmail, keyword)
            );
        }
        wrapper.orderByDesc(User::getCreateTime);

        Page<User> result = userMapper.selectPage(page, wrapper);
        result.getRecords().forEach(u -> u.setPassword(null));

        return PageResult.of(pageNum, pageSize, result.getTotal(), result.getRecords());
    }

    /**
     * 更新用户状态
     */
    @Override
    public void updateUserStatus(Long userId, Integer status) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        user.setStatus(status);
        userMapper.updateById(user);
    }

    /**
     * 更新用户角色
     */
    @Override
    public void updateUserRole(Long userId, Integer role) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        user.setRole(role);
        userMapper.updateById(user);
    }

    /**
     * 根据ID获取用户信息
     */
    @Override
    public User getUserById(Long userId) {
        User user = userMapper.selectById(userId);
        if (user != null) {
            user.setPassword(null);
        }
        return user;
    }
}
