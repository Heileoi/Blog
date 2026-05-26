package com.xilei.blog.service;

import com.xilei.blog.entity.FriendLink;

import java.util.List;

/**
 * 友情链接Service接口
 * 功能：定义友情链接相关的业务逻辑方法
 */
public interface FriendLinkService {

    /**
     * 申请友链
     * @param friendLink 友链信息
     */
    void applyFriendLink(FriendLink friendLink);

    /**
     * 审核友链
     * @param linkId 友链ID
     * @param status 状态（1-通过，2-拒绝）
     */
    void auditFriendLink(Long linkId, Integer status);

    /**
     * 更新友链
     * @param friendLink 友链信息
     */
    void updateFriendLink(FriendLink friendLink);

    /**
     * 删除友链
     * @param linkId 友链ID
     */
    void deleteFriendLink(Long linkId);

    /**
     * 获取已审核的友链列表
     * @return 友链列表
     */
    List<FriendLink> listApprovedFriendLinks();

    /**
     * 获取所有友链列表（管理员）
     * @return 友链列表
     */
    List<FriendLink> listAllFriendLinks();
}
