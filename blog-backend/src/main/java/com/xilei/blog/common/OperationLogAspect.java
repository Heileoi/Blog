package com.xilei.blog.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xilei.blog.entity.OperationLog;
import com.xilei.blog.mapper.OperationLogMapper;
import com.xilei.blog.utils.IpUtils;
import com.xilei.blog.utils.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 操作日志切面
 * 拦截带有@OperationLogAnnotation注解的方法，自动记录操作日志
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class OperationLogAspect {

    private final OperationLogMapper operationLogMapper;
    private final ObjectMapper objectMapper;

    @Around("@annotation(operationLog)")
    public Object around(ProceedingJoinPoint joinPoint, OperationLogAnnotation operationLog) throws Throwable {
        long startTime = System.currentTimeMillis();
        OperationLog logEntity = new OperationLog();

        try {
            // 基本信息
            logEntity.setModule(operationLog.module());
            logEntity.setOperation(operationLog.operation());
            logEntity.setMethod(joinPoint.getSignature().toShortString());

            // 请求信息
            ServletRequestAttributes attributes =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                logEntity.setUrl(request.getRequestURI());
                logEntity.setIpAddress(IpUtils.getClientIp(request));
            }

            // 参数（截断过长的参数）
            try {
                String params = objectMapper.writeValueAsString(joinPoint.getArgs());
                logEntity.setParams(params.length() > 2000 ? params.substring(0, 2000) : params);
            } catch (Exception e) {
                logEntity.setParams("参数序列化失败");
            }

            // 用户信息
            try {
                logEntity.setUserId(SecurityUtils.getCurrentUserId());
                logEntity.setUsername(SecurityUtils.getCurrentUsername());
            } catch (Exception e) {
                // 未登录状态
            }

            // 执行方法
            Object result = joinPoint.proceed();
            logEntity.setStatus(1);
            logEntity.setDuration(System.currentTimeMillis() - startTime);

            // 异步保存日志
            saveLog(logEntity);
            return result;

        } catch (Throwable e) {
            logEntity.setStatus(0);
            logEntity.setErrorMsg(e.getMessage() != null ?
                    (e.getMessage().length() > 500 ? e.getMessage().substring(0, 500) : e.getMessage()) :
                    "未知错误");
            logEntity.setDuration(System.currentTimeMillis() - startTime);
            saveLog(logEntity);
            throw e;
        }
    }

    private void saveLog(OperationLog logEntity) {
        try {
            operationLogMapper.insert(logEntity);
        } catch (Exception e) {
            log.error("保存操作日志失败: {}", e.getMessage());
        }
    }
}
