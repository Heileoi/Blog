<!--
  文章详情页
  功能：展示文章内容、评论、点赞
-->
<template>
  <view class="article-detail" v-if="article">
    <!-- 文章头部 -->
    <view class="article-header">
      <text class="article-title">{{ article.title }}</text>
      <view class="article-meta">
        <text>{{ formatDate(article.publishTime) }}</text>
        <text>  {{ article.viewCount }} 阅读</text>
        <text>  {{ article.categoryName }}</text>
      </view>
      <view class="article-tags" v-if="article.tags && article.tags.length">
        <text class="tag" v-for="tag in article.tags" :key="tag.id">{{ tag.name }}</text>
      </view>
    </view>

    <!-- 文章内容 -->
    <view class="article-content card">
      <rich-text :nodes="renderContent(article.content)"></rich-text>
    </view>

    <!-- 点赞按钮 -->
    <view class="action-bar">
      <view class="like-btn" :class="{ liked: isLiked }" @click="handleLike">
        <text>{{ isLiked ? '❤️' : ' '}} {{ article.likeCount }} 点赞</text>
      </view>
    </view>

    <!-- 评论区 -->
    <view class="comment-section card">
      <view class="section-title">评论 ({{ article.commentCount }})</view>

      <!-- 评论表单 -->
      <view class="comment-form">
        <textarea v-model="commentContent" placeholder="写下你的评论..." class="comment-textarea" />
        <button class="submit-btn" type="primary" size="mini" @click="submitComment" :loading="commentLoading">
          提交评论
        </button>
      </view>

      <!-- 评论列表 -->
      <view class="comment-list">
        <view class="comment-item" v-for="comment in comments" :key="comment.id">
          <view class="comment-header">
            <text class="nickname">{{ comment.nickname || '匿名用户' }}</text>
            <text class="time">{{ formatDate(comment.createTime) }}</text>
          </view>
          <text class="comment-content">{{ comment.content }}</text>
          <!-- 子评论 -->
          <view class="sub-comment" v-for="child in comment.children" :key="child.id">
            <text class="sub-text">
              <text class="nickname">{{ child.nickname || '匿名' }}</text>
              <text v-if="child.replyUserId"> 回复 </text>
              <text v-if="child.replyUserId" class="nickname">{{ child.replyUserName }}</text>
              : {{ child.content }}
            </text>
          </view>
        </view>

        <view class="empty" v-if="comments.length === 0">
          <text>暂无评论，快来抢沙发吧~</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { getArticleDetail, likeArticle } from '@/api/article'
import { listComments, createComment } from '@/api/comment'

export default {
  data() {
    return {
      article: null,
      comments: [],
      commentContent: '',
      commentLoading: false,
      isLiked: false,
      articleId: 0
    }
  },

  onLoad(options) {
    this.articleId = options.id
    this.fetchArticle()
    this.fetchComments()
  },

  onShareAppMessage() {
    return {
      title: this.article?.title || '熙磊的博客',
      path: `/pages/article/detail?id=${this.articleId}`
    }
  },

  methods: {
    /** 获取文章详情 */
    async fetchArticle() {
      try {
        const res = await getArticleDetail(this.articleId)
        this.article = res.data
        uni.setNavigationBarTitle({ title: this.article.title })
      } catch (e) {
        uni.showToast({ title: '文章不存在', icon: 'none' })
        setTimeout(() => uni.navigateBack(), 1500)
      }
    },

    /** 获取评论 */
    async fetchComments() {
      try {
        const res = await listComments(this.articleId, { pageNum: 1, pageSize: 50 })
        this.comments = res.data.list || []
      } catch (e) {}
    },

    /** 点赞 */
    async handleLike() {
      if (this.isLiked) return
      try {
        await likeArticle(this.articleId)
        this.article.likeCount++
        this.isLiked = true
        uni.showToast({ title: '点赞成功', icon: 'success' })
      } catch (e) {}
    },

    /** 提交评论 */
    async submitComment() {
      if (!this.commentContent.trim()) {
        return uni.showToast({ title: '请输入评论内容', icon: 'none' })
      }
      this.commentLoading = true
      try {
        await createComment({
          articleId: parseInt(this.articleId),
          content: this.commentContent
        })
        uni.showToast({ title: '评论成功', icon: 'success' })
        this.commentContent = ''
        this.fetchComments()
      } catch (e) {} finally {
        this.commentLoading = false
      }
    },

    /** 简单渲染Markdown */
    renderContent(content) {
      if (!content) return ''
      return content
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/^### (.*$)/gim, '<h3 style="margin:20rpx 0 10rpx;font-size:32rpx;">$1</h3>')
        .replace(/^## (.*$)/gim, '<h2 style="margin:20rpx 0 10rpx;font-size:36rpx;">$1</h2>')
        .replace(/^# (.*$)/gim, '<h1 style="margin:20rpx 0 10rpx;font-size:40rpx;">$1</h1>')
        .replace(/\*\*(.*?)\*\*/gim, '<strong>$1</strong>')
        .replace(/\*(.*?)\*/gim, '<em>$1</em>')
        .replace(/`(.*?)`/gim, '<code style="background:#f5f5f5;padding:2rpx 8rpx;border-radius:4rpx;">$1</code>')
        .replace(/\n/gim, '<br>')
    },

    /** 格式化日期 */
    formatDate(date) {
      if (!date) return ''
      return date.substring(0, 16)
    }
  }
}
</script>

<style lang="scss" scoped>
.article-header {
  padding: 30rpx;
  background: #fff;
  margin-bottom: 20rpx;

  .article-title {
    font-size: 40rpx;
    font-weight: bold;
    line-height: 1.4;
    display: block;
    margin-bottom: 16rpx;
  }

  .article-meta {
    display: flex;
    gap: 20rpx;
    font-size: 24rpx;
    color: #909399;
    margin-bottom: 12rpx;
  }

  .article-tags {
    display: flex;
    flex-wrap: wrap;
    gap: 8rpx;

    .tag {
      font-size: 22rpx;
      color: #409eff;
      background: #ecf5ff;
      padding: 4rpx 16rpx;
      border-radius: 6rpx;
    }
  }
}

.article-content {
  font-size: 30rpx;
  line-height: 1.8;
  color: #303133;
}

.action-bar {
  display: flex;
  justify-content: center;
  padding: 30rpx;

  .like-btn {
    background: #f0f0f0;
    padding: 16rpx 40rpx;
    border-radius: 40rpx;
    font-size: 28rpx;

    &.liked {
      background: #fef0f0;
      color: #f56c6c;
    }
  }
}

.comment-section {
  .section-title {
    font-size: 32rpx;
    font-weight: bold;
    margin-bottom: 20rpx;
    padding-bottom: 16rpx;
    border-bottom: 2rpx solid #eee;
  }
}

.comment-form {
  margin-bottom: 30rpx;

  .comment-textarea {
    width: 100%;
    height: 160rpx;
    padding: 16rpx;
    border: 2rpx solid #dcdfe6;
    border-radius: 12rpx;
    font-size: 28rpx;
    margin-bottom: 16rpx;
  }

  .submit-btn {
    float: right;
  }
}

.comment-item {
  padding: 20rpx 0;
  border-bottom: 2rpx solid #f0f0f0;

  .comment-header {
    display: flex;
    justify-content: space-between;
    margin-bottom: 10rpx;

    .nickname {
      font-weight: bold;
      color: #409eff;
      font-size: 28rpx;
    }

    .time {
      font-size: 24rpx;
      color: #909399;
    }
  }

  .comment-content {
    font-size: 28rpx;
    line-height: 1.6;
  }

  .sub-comment {
    margin-top: 12rpx;
    padding: 12rpx 16rpx;
    background: #f9f9f9;
    border-radius: 8rpx;
    font-size: 26rpx;

    .nickname {
      color: #409eff;
    }
  }
}

.empty {
  text-align: center;
  padding: 40rpx;
  color: #909399;
  font-size: 26rpx;
}
</style>
