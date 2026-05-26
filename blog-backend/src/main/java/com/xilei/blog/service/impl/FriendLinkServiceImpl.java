package com.xilei.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xilei.blog.entity.FriendLink;
import com.xilei.blog.exception.BusinessException;
import com.xilei.blog.mapper.FriendLinkMapper;
import com.xilei.blog.service.FriendLinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 友情链接Service实现类
 */
@Service
@RequiredArgsConstructor
public class FriendLinkServiceImpl implements FriendLinkService {

    private final FriendLinkMapper friendLinkMapper;

    @Override
    public void applyFriendLink(FriendLink friendLink) {
        friendLink.setStatus(0); // 待审核
        if (friendLink.getSortOrder() == null) friendLink.setSortOrder(0);
        friendLinkMapper.insert(friendLink);
    }

    @Override
    @CacheEvict(value = "friendLinks", allEntries = true)
    public void auditFriendLink(Long linkId, Integer status) {
        FriendLink link = friendLinkMapper.selectById(linkId);
        if (link == null) {
            throw new BusinessException("友链不存在");
        }
        link.setStatus(status);
        friendLinkMapper.updateById(link);
    }

    @Override
    @CacheEvict(value = "friendLinks", allEntries = true)
    public void updateFriendLink(FriendLink friendLink) {
        friendLinkMapper.updateById(friendLink);
    }

    @Override
    @CacheEvict(value = "friendLinks", allEntries = true)
    public void deleteFriendLink(Long linkId) {
        friendLinkMapper.deleteById(linkId);
    }

    @Override
    @Cacheable(value = "friendLinks", key = "'approved'")
    public List<FriendLink> listApprovedFriendLinks() {
        return friendLinkMapper.selectList(
                new LambdaQueryWrapper<FriendLink>()
                        .eq(FriendLink::getStatus, 1)
                        .orderByAsc(FriendLink::getSortOrder)
        );
    }

    @Override
    public List<FriendLink> listAllFriendLinks() {
        return friendLinkMapper.selectList(
                new LambdaQueryWrapper<FriendLink>().orderByDesc(FriendLink::getCreateTime)
        );
    }
}
