<!--
  文章详情页
  功能：展示文章完整内容、评论区、点赞、收藏等
-->
<template>
  <div class="article-detail" v-if="article">
    <div class="article-main">
      <div class="card">
        <!-- 文章头部 -->
        <div class="article-header">
          <h1 class="title">{{ article.title }}</h1>
          <div class="meta">
            <span><el-icon><Calendar /></el-icon> {{ article.publishTime?.substring(0, 10) }}</span>
            <span><el-icon><View /></el-icon> {{ article.viewCount }} 阅读</span>
            <span><el-icon><Folder /></el-icon> {{ article.categoryName }}</span>
            <span><el-icon><User /></el-icon> {{ article.authorName || '王熙磊' }}</span>
          </div>
          <div class="tags" v-if="article.tags?.length">
            <el-tag v-for="tag in article.tags" :key="tag.id" size="small">{{ tag.name }}</el-tag>
          </div>
        </div>

        <!-- 文章内容 -->
        <div class="article-content markdown-body" v-html="renderedContent"></div>

        <!-- 文章底部操作 -->
        <div class="article-actions">
          <el-button :type="isLiked ? 'primary' : 'default'" @click="handleLike">
            <el-icon><Pointer /></el-icon> {{ isLiked ? '已点赞' : '点赞' }} ({{ article.likeCount }})
          </el-button>
        </div>
      </div>

      <!-- 评论区 -->
      <div class="card comment-section">
        <h3>评论 ({{ article.commentCount }})</h3>

        <!-- 评论表单 -->
        <div class="comment-form">
          <el-input v-model="commentContent" type="textarea" :rows="3" placeholder="写下你的评论..." />
          <el-button type="primary" @click="submitComment" :loading="commentLoading" style="margin-top: 10px">
            提交评论
          </el-button>
        </div>

        <!-- 评论列表 -->
        <div class="comment-list">
          <div class="comment-item" v-for="comment in comments" :key="comment.id">
            <el-avatar :size="40" :src="comment.user?.avatar || '/default-avatar.png'" />
            <div class="comment-body">
              <div class="comment-header">
                <span class="nickname">{{ comment.nickname || comment.user?.nickname || '匿名用户' }}</span>
                <span class="time">{{ comment.createTime?.substring(0, 16) }}</span>
              </div>
              <div class="comment-content">{{ comment.content }}</div>
              <!-- 子评论 -->
              <div class="sub-comment" v-for="child in comment.children" :key="child.id">
                <span class="nickname">{{ child.nickname || child.user?.nickname }}</span>
                <span v-if="child.replyUserId"> 回复 </span>
                <span class="nickname" v-if="child.replyUserId">{{ child.replyUserName }}</span>
                <span>: {{ child.content }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getFrontArticleDetail, likeArticle } from '@/api/article'
import { listCommentsByArticle, createComment } from '@/api/comment'

const route = useRoute()
const article = ref(null)
const comments = ref([])
const commentContent = ref('')
const commentLoading = ref(false)
const isLiked = ref(false)

/** 简单Markdown渲染 */
const renderedContent = computed(() => {
  if (!article.value?.content) return ''
  // 基础Markdown转换（实际项目建议使用markdown-it等库）
  let html = article.value.content
    .replace(/^### (.*$)/gim, '<h3>$1</h3>')
    .replace(/^## (.*$)/gim, '<h2>$1</h2>')
    .replace(/^# (.*$)/gim, '<h1>$1</h1>')
    .replace(/\*\*(.*?)\*\*/gim, '<strong>$1</strong>')
    .replace(/\*(.*?)\*/gim, '<em>$1</em>')
    .replace(/`(.*?)`/gim, '<code>$1</code>')
    .replace(/\n/gim, '<br>')
  return html
})

/** 获取文章详情 */
const fetchArticle = async () => {
  const res = await getFrontArticleDetail(route.params.id)
  article.value = res.data
}

/** 获取评论 */
const fetchComments = async () => {
  const res = await listCommentsByArticle(route.params.id, { pageNum: 1, pageSize: 50 })
  comments.value = res.data.list || []
}

/** 点赞 */
const handleLike = async () => {
  if (isLiked.value) return
  await likeArticle(route.params.id)
  article.value.likeCount++
  isLiked.value = true
  ElMessage.success('点赞成功')
}

/** 提交评论 */
const submitComment = async () => {
  if (!commentContent.value.trim()) {
    ElMessage.warning('请输入评论内容')
    return
  }
  commentLoading.value = true
  try {
    await createComment({
      articleId: parseInt(route.params.id),
      content: commentContent.value
    })
    ElMessage.success('评论提交成功')
    commentContent.value = ''
    fetchComments()
  } finally {
    commentLoading.value = false
  }
}

onMounted(() => {
  fetchArticle()
  fetchComments()
})
</script>

<style lang="scss" scoped>
.article-main {
  max-width: 800px;
  margin: 0 auto;
}

.article-header {
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid var(--border-color);

  .title {
    font-size: 28px;
    margin-bottom: 15px;
    line-height: 1.4;
  }

  .meta {
    display: flex;
    gap: 20px;
    color: var(--text-secondary);
    font-size: 14px;
    margin-bottom: 10px;

    span {
      display: flex;
      align-items: center;
      gap: 4px;
    }
  }

  .tags {
    display: flex;
    gap: 6px;
  }
}

.article-content {
  line-height: 1.8;
  font-size: 16px;

  :deep(h1), :deep(h2), :deep(h3) {
    margin: 20px 0 10px;
  }

  :deep(code) {
    background: #f5f5f5;
    padding: 2px 6px;
    border-radius: 4px;
    font-size: 14px;
  }
}

.article-actions {
  margin-top: 30px;
  padding-top: 20px;
  border-top: 1px solid var(--border-color);
  text-align: center;
}

.comment-section {
  h3 {
    margin-bottom: 20px;
  }
}

.comment-form {
  margin-bottom: 30px;
}

.comment-item {
  display: flex;
  gap: 12px;
  padding: 15px 0;
  border-bottom: 1px solid #f0f0f0;

  .comment-body {
    flex: 1;

    .comment-header {
      margin-bottom: 6px;

      .nickname {
        font-weight: bold;
        color: var(--primary-color);
      }

      .time {
        margin-left: 10px;
        font-size: 12px;
        color: var(--text-secondary);
      }
    }

    .comment-content {
      line-height: 1.6;
    }

    .sub-comment {
      margin-top: 8px;
      padding: 8px 12px;
      background: #f9f9f9;
      border-radius: 6px;
      font-size: 13px;

      .nickname {
        color: var(--primary-color);
      }
    }
  }
}
</style>
