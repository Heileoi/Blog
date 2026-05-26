package com.xilei.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xilei.blog.entity.Notice;
import com.xilei.blog.mapper.NoticeMapper;
import com.xilei.blog.service.NoticeService;
import com.xilei.blog.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 公告Service实现类
 */
@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final NoticeMapper noticeMapper;

    @Override
    public void createNotice(Notice notice) {
        notice.setUserId(SecurityUtils.getCurrentUserId());
        if (notice.getStatus() == null) notice.setStatus(0);
        if (notice.getIsTop() == null) notice.setIsTop(0);
        noticeMapper.insert(notice);
    }

    @Override
    public void updateNotice(Notice notice) {
        noticeMapper.updateById(notice);
    }

    @Override
    public void deleteNotice(Long noticeId) {
        noticeMapper.deleteById(noticeId);
    }

    @Override
    public List<Notice> listPublishedNotices() {
        return noticeMapper.selectList(
                new LambdaQueryWrapper<Notice>()
                        .eq(Notice::getStatus, 1)
                        .orderByDesc(Notice::getIsTop)
                        .orderByDesc(Notice::getCreateTime)
        );
    }

    @Override
    public List<Notice> listAllNotices() {
        return noticeMapper.selectList(
                new LambdaQueryWrapper<Notice>().orderByDesc(Notice::getCreateTime)
        );
    }
}
