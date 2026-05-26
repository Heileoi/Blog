package com.xilei.blog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 博客系统主启动类
 * 功能：
 * 1. Spring Boot应用入口
 * 2. 启用异步处理（用于日志记录等非阻塞操作）
 * 3. 启用定时任务（用于数据统计等定时操作）
 * 4. 扫描MyBatis Mapper接口
 */
@SpringBootApplication
@MapperScan("com.xilei.blog.mapper")
@EnableAsync
@EnableScheduling
public class BlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class, args);
        System.out.println("============================================");
        System.out.println("   熙磊个人博客系统启动成功！");
        System.out.println("   API文档: http://localhost:8080/api/doc.html");
        System.out.println("============================================");
    }
}
