package com.xilei.blog.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 安全响应头过滤器
 * 为所有响应添加安全相关的HTTP头
 */
@Component
public class SecurityHeaderFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 防止MIME嗅探
        response.setHeader("X-Content-Type-Options", "nosniff");
        // 防止点击劫持
        response.setHeader("X-Frame-Options", "DENY");
        // XSS保护（旧浏览器兼容）
        response.setHeader("X-XSS-Protection", "1; mode=block");
        // 严格传输安全（HTTPS时生效）
        response.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");
        // 缓存控制（API响应不应被缓存）
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        // 控制Referer信息
        response.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");

        filterChain.doFilter(request, response);
    }
}
