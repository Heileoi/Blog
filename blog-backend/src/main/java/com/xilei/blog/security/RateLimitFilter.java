package com.xilei.blog.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xilei.blog.common.Result;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 接口限流过滤器
 * 基于滑动窗口计数器，对敏感接口进行IP级别限流
 */
@Slf4j
@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /** IP -> 请求记录 */
    private final ConcurrentHashMap<String, RequestRecord> requestCounts = new ConcurrentHashMap<>();

    /** 登录接口：每分钟最多10次 */
    private static final int LOGIN_MAX_REQUESTS = 10;
    private static final long LOGIN_WINDOW_MS = 60_000;

    /** 注册接口：每小时最多5次 */
    private static final int REGISTER_MAX_REQUESTS = 5;
    private static final long REGISTER_WINDOW_MS = 3_600_000;

    /** 评论接口：每分钟最多10次 */
    private static final int COMMENT_MAX_REQUESTS = 10;
    private static final long COMMENT_WINDOW_MS = 60_000;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        String clientIp = getClientIp(request);

        if (path.contains("/auth/login")) {
            if (!tryAcquire(clientIp + ":login", LOGIN_MAX_REQUESTS, LOGIN_WINDOW_MS)) {
                sendTooManyRequests(response, "登录尝试过于频繁，请1分钟后再试");
                return;
            }
        } else if (path.contains("/auth/register")) {
            if (!tryAcquire(clientIp + ":register", REGISTER_MAX_REQUESTS, REGISTER_WINDOW_MS)) {
                sendTooManyRequests(response, "注册请求过于频繁，请1小时后再试");
                return;
            }
        } else if (path.contains("/comments") && "POST".equalsIgnoreCase(request.getMethod())) {
            if (!tryAcquire(clientIp + ":comment", COMMENT_MAX_REQUESTS, COMMENT_WINDOW_MS)) {
                sendTooManyRequests(response, "评论过于频繁，请稍后再试");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean tryAcquire(String key, int maxRequests, long windowMs) {
        long now = System.currentTimeMillis();
        RequestRecord record = requestCounts.compute(key, (k, v) -> {
            if (v == null || now - v.windowStart > windowMs) {
                return new RequestRecord(now);
            }
            return v;
        });

        if (record.count.incrementAndGet() > maxRequests) {
            log.warn("限流触发: key={}, count={}", key, record.count.get());
            return false;
        }
        return true;
    }

    private void sendTooManyRequests(HttpServletResponse response, String message) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(429);
        response.getWriter().write(objectMapper.writeValueAsString(Result.error(429, message)));
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip.split(",")[0].trim();
        }
        ip = request.getHeader("X-Real-IP");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }

    private static class RequestRecord {
        final long windowStart;
        final AtomicInteger count;

        RequestRecord(long windowStart) {
            this.windowStart = windowStart;
            this.count = new AtomicInteger(0);
        }
    }
}
