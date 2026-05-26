package com.xilei.blog.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * 登录用户信息类
 * 功能：封装当前登录用户的详细信息，扩展Spring Security的User类
 * 用途：在Controller中通过SecurityContextHolder获取当前用户信息
 */
@Getter
public class LoginUser extends User {

    /** 用户ID */
    private final Long userId;

    /** 昵称 */
    private final String nickname;

    /** 头像URL */
    private final String avatar;

    public LoginUser(Long userId, String username, String password, String nickname, String avatar,
                     Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.userId = userId;
        this.nickname = nickname;
        this.avatar = avatar;
    }

    /**
     * 判断是否为管理员
     */
    public boolean isAdmin() {
        return getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}
