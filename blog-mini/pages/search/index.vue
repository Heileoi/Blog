<!--
  搜索页面
  功能：搜索文章，支持分类和标签筛选
-->
<template>
  <view class="search-page">
    <!-- 搜索栏 -->
    <view class="search-bar">
      <input v-model="keyword" placeholder="输入关键词搜索..." class="search-input"
             confirm-type="search" @confirm="doSearch" focus />
      <button class="search-btn" type="primary" size="mini" @click="doSearch">搜索</button>
    </view>

    <!-- 文章列表 -->
    <view class="article-list">
      <view class="article-card" v-for="article in articles" :key="article.id" @click="goArticle(article.id)">
        <text class="article-title">{{ article.title }}</text>
        <text class="article-summary">{{ article.summary }}</text>
        <view class="article-meta">
          <text>{{ formatDate(article.publishTime) }}</text>
          <text>  {{ article.viewCount }}</text>
        </view>
      </view>

      <view class="loading-more" v-if="loading">
        <text>加载中...</text>
      </view>

      <view class="empty" v-if="!loading && searched && articles.length === 0">
        <text>未找到相关文章</text>
      </view>
    </view>
  </view>
</template>

<script>
import { listArticles } from '@/api/article'

export default {
  data() {
    return {
      keyword: '',
      articles: [],
      loading: false,
      searched: false,
      categoryId: null,
      tagId: null
    }
  },

  onLoad(options) {
    if (options.categoryId) {
      this.categoryId = options.categoryId
    }
    if (options.tagId) {
      this.tagId = options.tagId
    }
    if (options.title) {
      uni.setNavigationBarTitle({ title: options.title })
      this.doSearch()
    }
  },

  methods: {
    async doSearch() {
      if (!this.keyword && !this.categoryId && !this.tagId) return
      this.loading = true
      this.searched = true
      try {
        const res = await listArticles({
          pageNum: 1,
          pageSize: 50,
          keyword: this.keyword,
          categoryId: this.categoryId,
          tagId: this.tagId
        })
        this.articles = res.data.list || []
      } catch (e) {} finally {
        this.loading = false
      }
    },

    goArticle(id) {
      uni.navigateTo({ url: `/pages/article/detail?id=${id}` })
    },

    formatDate(date) {
      if (!date) return ''
      return date.substring(0, 10)
    }
  }
}
</script>

<style lang="scss" scoped>
.search-bar {
  display: flex;
  align-items: center;
  padding: 20rpx;
  background: #fff;
  gap: 16rpx;

  .search-input {
    flex: 1;
    height: 72rpx;
    padding: 0 24rpx;
    background: #f5f7fa;
    border-radius: 36rpx;
    font-size: 28rpx;
  }
}

.article-list {
  padding: 20rpx;
}

.article-card {
  background: #fff;
  border-radius: 16rpx;
  padding: 24rpx;
  margin-bottom: 16rpx;

  .article-title {
    font-size: 30rpx;
    font-weight: bold;
    display: block;
    margin-bottom: 10rpx;
  }

  .article-summary {
    font-size: 26rpx;
    color: #909399;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
    margin-bottom: 10rpx;
  }

  .article-meta {
    display: flex;
    gap: 20rpx;
    font-size: 24rpx;
    color: #909399;
  }
}

.loading-more, .empty {
  text-align: center;
  padding: 40rpx;
  color: #909399;
  font-size: 26rpx;
}
</style>
