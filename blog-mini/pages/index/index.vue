<!--
  首页
  功能：展示文章列表、搜索、轮播公告
-->
<template>
  <view class="index-page">
    <!-- 搜索栏 -->
    <view class="search-bar" @click="goSearch">
      <view class="search-input">
        <text class="icon"> </text>
        <text class="placeholder">搜索文章...</text>
      </view>
    </view>

    <!-- 公告轮播 -->
    <view class="notice-bar" v-if="notices.length">
      <swiper autoplay circular vertical class="notice-swiper" :interval="3000">
        <swiper-item v-for="notice in notices" :key="notice.id">
          <view class="notice-item">
            <text class="notice-tag">公告</text>
            <text class="notice-text">{{ notice.title }}</text>
          </view>
        </swiper-item>
      </swiper>
    </view>

    <!-- 推荐文章轮播 -->
    <view class="swiper-section" v-if="featuredArticles.length">
      <swiper autoplay circular class="featured-swiper" :interval="4000">
        <swiper-item v-for="article in featuredArticles" :key="article.id" @click="goArticle(article.id)">
          <view class="featured-item">
            <image :src="article.coverImage || '/static/default-cover.png'" mode="aspectFill" class="featured-cover" />
            <view class="featured-info">
              <text class="featured-title">{{ article.title }}</text>
            </view>
          </view>
        </swiper-item>
      </swiper>
    </view>

    <!-- 文章列表 -->
    <view class="section">
      <view class="section-header">
        <text class="section-title">最新文章</text>
      </view>

      <view class="article-card" v-for="article in articles" :key="article.id" @click="goArticle(article.id)">
        <view class="article-content">
          <text class="article-title">{{ article.title }}</text>
          <text class="article-summary">{{ article.summary }}</text>
          <view class="article-meta">
            <text class="meta-item">  {{ formatDate(article.publishTime) }}</text>
            <text class="meta-item">  {{ article.viewCount }}</text>
            <text class="meta-item">  {{ article.commentCount }}</text>
          </view>
          <view class="article-tags" v-if="article.tags && article.tags.length">
            <text class="tag" v-for="tag in article.tags" :key="tag.id">{{ tag.name }}</text>
          </view>
        </view>
        <image v-if="article.coverImage" :src="article.coverImage" mode="aspectFill" class="article-cover" />
      </view>

      <!-- 加载状态 -->
      <view class="loading-more" v-if="loading">
        <text>加载中...</text>
      </view>

      <!-- 没有更多 -->
      <view class="no-more" v-if="!hasMore && articles.length > 0">
        <text>— 没有更多了 —</text>
      </view>

      <!-- 空状态 -->
      <view class="empty" v-if="!loading && articles.length === 0">
        <text class="empty-icon"> </text>
        <text class="empty-text">暂无文章</text>
      </view>
    </view>
  </view>
</template>

<script>
import { listArticles, getFeaturedArticles } from '@/api/article'
import { listNotices } from '@/api/notice'

export default {
  data() {
    return {
      articles: [],
      featuredArticles: [],
      notices: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      loading: false,
      hasMore: true
    }
  },

  onLoad() {
    this.fetchNotices()
    this.fetchFeatured()
    this.fetchArticles()
  },

  onPullDownRefresh() {
    this.pageNum = 1
    this.hasMore = true
    this.articles = []
    this.fetchArticles()
    uni.stopPullDownRefresh()
  },

  onReachBottom() {
    if (this.hasMore && !this.loading) {
      this.pageNum++
      this.fetchArticles()
    }
  },

  methods: {
    /** 获取公告 */
    async fetchNotices() {
      try {
        const res = await listNotices()
        this.notices = res.data || []
      } catch (e) {}
    },

    /** 获取推荐文章 */
    async fetchFeatured() {
      try {
        const res = await getFeaturedArticles(5)
        this.featuredArticles = res.data || []
      } catch (e) {}
    },

    /** 获取文章列表 */
    async fetchArticles() {
      if (this.loading) return
      this.loading = true
      try {
        const res = await listArticles({
          pageNum: this.pageNum,
          pageSize: this.pageSize
        })
        const list = res.data.list || []
        if (this.pageNum === 1) {
          this.articles = list
        } else {
          this.articles = [...this.articles, ...list]
        }
        this.total = res.data.total
        this.hasMore = this.articles.length < this.total
      } catch (e) {} finally {
        this.loading = false
      }
    },

    /** 格式化日期 */
    formatDate(date) {
      if (!date) return ''
      return date.substring(0, 10)
    },

    /** 跳转文章详情 */
    goArticle(id) {
      uni.navigateTo({ url: `/pages/article/detail?id=${id}` })
    },

    /** 跳转搜索 */
    goSearch() {
      uni.navigateTo({ url: '/pages/search/index' })
    }
  }
}
</script>

<style lang="scss" scoped>
.search-bar {
  padding: 20rpx 30rpx;
  background: #409eff;

  .search-input {
    display: flex;
    align-items: center;
    background: rgba(255, 255, 255, 0.9);
    border-radius: 40rpx;
    padding: 16rpx 24rpx;

    .icon {
      margin-right: 10rpx;
    }

    .placeholder {
      color: #999;
      font-size: 28rpx;
    }
  }
}

.notice-bar {
  background: #fdf6ec;
  padding: 16rpx 30rpx;

  .notice-swiper {
    height: 48rpx;
  }

  .notice-item {
    display: flex;
    align-items: center;
    height: 48rpx;

    .notice-tag {
      background: #e6a23c;
      color: #fff;
      font-size: 22rpx;
      padding: 4rpx 12rpx;
      border-radius: 6rpx;
      margin-right: 12rpx;
    }

    .notice-text {
      font-size: 26rpx;
      color: #666;
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
    }
  }
}

.featured-swiper {
  height: 360rpx;
  margin: 20rpx 30rpx;
  border-radius: 16rpx;
  overflow: hidden;

  .featured-item {
    position: relative;
    width: 100%;
    height: 100%;

    .featured-cover {
      width: 100%;
      height: 100%;
    }

    .featured-info {
      position: absolute;
      bottom: 0;
      left: 0;
      right: 0;
      padding: 30rpx;
      background: linear-gradient(transparent, rgba(0, 0, 0, 0.7));

      .featured-title {
        color: #fff;
        font-size: 32rpx;
        font-weight: bold;
      }
    }
  }
}

.section {
  padding: 0 30rpx;

  .section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20rpx;

    .section-title {
      font-size: 32rpx;
      font-weight: bold;
      color: #303133;
    }
  }
}

.article-card {
  display: flex;
  background: #fff;
  border-radius: 16rpx;
  padding: 24rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.06);

  .article-content {
    flex: 1;
    display: flex;
    flex-direction: column;

    .article-title {
      font-size: 30rpx;
      font-weight: bold;
      color: #303133;
      margin-bottom: 12rpx;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
      overflow: hidden;
    }

    .article-summary {
      font-size: 26rpx;
      color: #909399;
      margin-bottom: 12rpx;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
      overflow: hidden;
    }

    .article-meta {
      display: flex;
      gap: 20rpx;
      margin-bottom: 10rpx;

      .meta-item {
        font-size: 24rpx;
        color: #909399;
      }
    }

    .article-tags {
      display: flex;
      flex-wrap: wrap;
      gap: 8rpx;

      .tag {
        font-size: 22rpx;
        color: #409eff;
        background: #ecf5ff;
        padding: 4rpx 12rpx;
        border-radius: 6rpx;
      }
    }
  }

  .article-cover {
    width: 200rpx;
    height: 160rpx;
    border-radius: 12rpx;
    margin-left: 20rpx;
  }
}

.loading-more, .no-more {
  text-align: center;
  padding: 30rpx;
  color: #909399;
  font-size: 26rpx;
}

.empty {
  text-align: center;
  padding: 100rpx 0;

  .empty-icon {
    font-size: 80rpx;
    display: block;
    margin-bottom: 20rpx;
  }

  .empty-text {
    color: #909399;
    font-size: 28rpx;
  }
}
</style>
