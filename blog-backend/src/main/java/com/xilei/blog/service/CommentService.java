package com.xilei.blog.service;

import com.xilei.blog.common.PageResult;
import com.xilei.blog.dto.CommentDTO;
import com.xilei.blog.entity.Comment;

import java.util.List;

/**
 * 评论Service接口
 * 功能：定义评论相关的业务逻辑方法
 */
public interface CommentService {

    /**
     * 提交评论
     * @param commentDTO 评论DTO
     * @param ipAddress 评论者IP
     * @param userAgent 评论者浏览器信息
     */
    void createComment(CommentDTO commentDTO, String ipAddress, String userAgent);

    /**
     * 删除评论
     * @param commentId 评论ID
     */
    void deleteComment(Long commentId);

    /**
     * 审核评论
     * @param commentId 评论ID
     * @param status 状态（1-通过，2-拒绝）
     */
    void auditComment(Long commentId, Integer status);

    /**
     * 获取文章的评论列表（树形结构）
     * @param articleId 文章ID
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 评论分页结果
     */
    PageResult<Comment> listCommentsByArticle(Long articleId, Integer pageNum, Integer pageSize);

    /**
     * 分页查询评论列表（管理员）
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param keyword 搜索关键词
     * @param status 评论状态
     * @return 分页结果
     */
    PageResult<Comment> listCommentsForAdmin(Integer pageNum, Integer pageSize,
                                              String keyword, Integer status);

    /**
     * 获取最新评论
     * @param limit 数量
     * @return 最新评论列表
     */
    List<Comment> getLatestComments(Integer limit);
}
