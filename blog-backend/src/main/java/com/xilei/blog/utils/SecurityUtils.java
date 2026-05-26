package com.xilei.blog.utils;

import com.xilei.blog.security.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 安全工具类
 * 功能：获取当前登录用户信息
 * 用途：在Service层获取当前操作用户
 */
public class SecurityUtils {

    /**
     * 获取当前登录用户
     * @return LoginUser对象，未登录返回null
     */
    public static LoginUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof LoginUser) {
            return (LoginUser) authentication.getPrincipal();
        }
        return null;
    }

    /**
     * 获取当前用户ID
     * @return 用户ID，未登录返回null
     */
    public static Long getCurrentUserId() {
        LoginUser user = getCurrentUser();
        return user != null ? user.getUserId() : null;
    }

    /**
     * 获取当前用户名
     * @return 用户名，未登录返回null
     */
    public static String getCurrentUsername() {
        LoginUser user = getCurrentUser();
        return user != null ? user.getUsername() : null;
    }

    /**
     * 判断当前用户是否为管理员
     * @return true-是管理员，false-不是
     */
    public static boolean isAdmin() {
        LoginUser user = getCurrentUser();
        return user != null && user.isAdmin();
    }
}
