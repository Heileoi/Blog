package com.xilei.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xilei.blog.common.PageResult;
import com.xilei.blog.dto.CommentDTO;
import com.xilei.blog.entity.Article;
import com.xilei.blog.entity.Comment;
import com.xilei.blog.enums.CommentStatus;
import com.xilei.blog.exception.BusinessException;
import com.xilei.blog.mapper.ArticleMapper;
import com.xilei.blog.mapper.CommentMapper;
import com.xilei.blog.mapper.UserMapper;
import com.xilei.blog.service.CommentService;
import com.xilei.blog.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 评论Service实现类
 * 功能：实现评论的提交、审核、查询等业务逻辑
 */
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;
    private final ArticleMapper articleMapper;
    private final UserMapper userMapper;

    /**
     * 提交评论
     * 流程：1.验证文章存在 2.验证评论开启 3.保存评论 4.更新文章评论数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createComment(CommentDTO commentDTO, String ipAddress, String userAgent) {
        Article article = articleMapper.selectById(commentDTO.getArticleId());
        if (article == null) {
            throw new BusinessException("文章不存在");
        }
        if (article.getIsCommentEnabled() != null && article.getIsCommentEnabled() == 0) {
            throw new BusinessException("该文章已关闭评论");
        }

        Comment comment = new Comment();
        comment.setContent(commentDTO.getContent());
        comment.setArticleId(commentDTO.getArticleId());
        comment.setParentId(commentDTO.getParentId() != null ? commentDTO.getParentId() : 0L);
        comment.setReplyUserId(commentDTO.getReplyUserId());
        comment.setIpAddress(ipAddress);
        comment.setUserAgent(userAgent);
        comment.setStatus(CommentStatus.APPROVED.getCode()); // 默认直接通过

        // 如果已登录，关联用户信息
        Long currentUserId = SecurityUtils.getCurrentUserId();
        if (currentUserId != null) {
            comment.setUserId(currentUserId);
        } else {
            // 游客评论
            if (!StringUtils.hasText(commentDTO.getNickname())) {
                throw new BusinessException("请输入昵称");
            }
            comment.setNickname(commentDTO.getNickname());
            comment.setEmail(commentDTO.getEmail());
            comment.setWebsite(commentDTO.getWebsite());
        }

        commentMapper.insert(comment);

        // 更新文章评论数
        article.setCommentCount(article.getCommentCount() + 1);
        articleMapper.updateById(article);
    }

    @Override
    public void deleteComment(Long commentId) {
        commentMapper.deleteById(commentId);
    }

    @Override
    public void auditComment(Long commentId, Integer status) {
        Comment comment = commentMapper.selectById(commentId);
        if (comment == null) {
            throw new BusinessException("评论不存在");
        }
        comment.setStatus(status);
        commentMapper.updateById(comment);
    }

    /**
     * 获取文章的评论列表（树形结构）
     */
    @Override
    public PageResult<Comment> listCommentsByArticle(Long articleId, Integer pageNum, Integer pageSize) {
        // 查询顶级评论
        Page<Comment> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<Comment>()
                .eq(Comment::getArticleId, articleId)
                .eq(Comment::getParentId, 0)
                .eq(Comment::getStatus, CommentStatus.APPROVED.getCode())
                .orderByDesc(Comment::getCreateTime);

        Page<Comment> result = commentMapper.selectPage(page, wrapper);

        // 为每个顶级评论加载子评论
        result.getRecords().forEach(this::fillChildren);

        return PageResult.of(pageNum, pageSize, result.getTotal(), result.getRecords());
    }

    /**
     * 分页查询评论列表（管理员）
     */
    @Override
    public PageResult<Comment> listCommentsForAdmin(Integer pageNum, Integer pageSize,
                                                     String keyword, Integer status) {
        Page<Comment> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(keyword)) {
            wrapper.like(Comment::getContent, keyword);
        }
        if (status != null) {
            wrapper.eq(Comment::getStatus, status);
        }
        wrapper.orderByDesc(Comment::getCreateTime);

        Page<Comment> result = commentMapper.selectPage(page, wrapper);
        return PageResult.of(pageNum, pageSize, result.getTotal(), result.getRecords());
    }

    @Override
    public List<Comment> getLatestComments(Integer limit) {
        return commentMapper.selectList(
                new LambdaQueryWrapper<Comment>()
                        .eq(Comment::getStatus, CommentStatus.APPROVED.getCode())
                        .orderByDesc(Comment::getCreateTime)
                        .last("LIMIT " + limit)
        );
    }

    /**
     * 填充子评论
     */
    private void fillChildren(Comment comment) {
        List<Comment> children = commentMapper.selectList(
                new LambdaQueryWrapper<Comment>()
                        .eq(Comment::getParentId, comment.getId())
                        .eq(Comment::getStatus, CommentStatus.APPROVED.getCode())
                        .orderByAsc(Comment::getCreateTime)
        );
        comment.setChildren(children);
    }
}
