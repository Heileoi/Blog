package com.xilei.blog.service;

import java.util.Map;

/**
 * 仪表盘Service接口
 * 功能：定义后台首页仪表盘的数据统计方法
 */
public interface DashboardService {

    /**
     * 获取仪表盘统计数据
     * 包含：文章数、评论数、用户数、浏览量、今日访问等
     * @return 统计数据Map
     */
    Map<String, Object> getDashboardStats();

    /**
     * 获取最近7天的文章发布趋势
     * @return 日期和数量的列表
     */
    Map<String, Object> getArticleTrend();
}
