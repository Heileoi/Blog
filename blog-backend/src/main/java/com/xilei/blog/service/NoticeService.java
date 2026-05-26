package com.xilei.blog.service;

import com.xilei.blog.entity.Notice;

import java.util.List;

/**
 * 公告Service接口
 * 功能：定义公告相关的业务逻辑方法
 */
public interface NoticeService {

    /**
     * 创建公告
     * @param notice 公告信息
     */
    void createNotice(Notice notice);

    /**
     * 更新公告
     * @param notice 公告信息
     */
    void updateNotice(Notice notice);

    /**
     * 删除公告
     * @param noticeId 公告ID
     */
    void deleteNotice(Long noticeId);

    /**
     * 获取已发布公告列表
     * @return 公告列表
     */
    List<Notice> listPublishedNotices();

    /**
     * 获取所有公告列表（管理员）
     * @return 公告列表
     */
    List<Notice> listAllNotices();
}
