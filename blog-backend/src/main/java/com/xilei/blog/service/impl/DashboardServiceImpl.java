package com.xilei.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xilei.blog.entity.*;
import com.xilei.blog.enums.ArticleStatus;
import com.xilei.blog.mapper.*;
import com.xilei.blog.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 仪表盘Service实现类
 * 功能：统计博客各项数据指标，用于后台首页展示
 */
@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final ArticleMapper articleMapper;
    private final CommentMapper commentMapper;
    private final UserMapper userMapper;

    /**
     * 获取仪表盘统计数据
     */
    @Override
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        // 文章总数（已发布）
        Long articleCount = articleMapper.selectCount(
                new LambdaQueryWrapper<Article>()
                        .eq(Article::getStatus, ArticleStatus.PUBLISHED.getCode())
        );
        stats.put("articleCount", articleCount);

        // 草稿数
        Long draftCount = articleMapper.selectCount(
                new LambdaQueryWrapper<Article>()
                        .eq(Article::getStatus, ArticleStatus.DRAFT.getCode())
        );
        stats.put("draftCount", draftCount);

        // 评论总数
        Long commentCount = commentMapper.selectCount(null);
        stats.put("commentCount", commentCount);

        // 用户总数
        Long userCount = userMapper.selectCount(null);
        stats.put("userCount", userCount);

        // 今日新增文章
        Long todayArticles = articleMapper.selectCount(
                new LambdaQueryWrapper<Article>()
                        .ge(Article::getCreateTime, LocalDateTime.of(LocalDate.now(), LocalTime.MIN))
        );
        stats.put("todayArticles", todayArticles);

        // 今日新增评论
        Long todayComments = commentMapper.selectCount(
                new LambdaQueryWrapper<Comment>()
                        .ge(Comment::getCreateTime, LocalDateTime.of(LocalDate.now(), LocalTime.MIN))
        );
        stats.put("todayComments", todayComments);

        // 总浏览量
        List<Article> articles = articleMapper.selectList(
                new LambdaQueryWrapper<Article>().select(Article::getViewCount)
        );
        int totalViews = articles.stream()
                .mapToInt(a -> a.getViewCount() != null ? a.getViewCount() : 0)
                .sum();
        stats.put("totalViews", totalViews);

        return stats;
    }

    /**
     * 获取最近7天的文章发布趋势
     */
    @Override
    public Map<String, Object> getArticleTrend() {
        Map<String, Object> result = new HashMap<>();
        List<String> dates = new ArrayList<>();
        List<Long> counts = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");

        for (int i = 6; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            dates.add(date.format(formatter));

            Long count = articleMapper.selectCount(
                    new LambdaQueryWrapper<Article>()
                            .ge(Article::getCreateTime, LocalDateTime.of(date, LocalTime.MIN))
                            .lt(Article::getCreateTime, LocalDateTime.of(date.plusDays(1), LocalTime.MIN))
            );
            counts.add(count);
        }

        result.put("dates", dates);
        result.put("counts", counts);
        return result;
    }
}
