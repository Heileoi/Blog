<!--
  分类页面
  功能：展示所有分类及文章数量
-->
<template>
  <view class="category-page">
    <view class="category-grid">
      <view class="category-item" v-for="cat in categories" :key="cat.id" @click="goCategory(cat)">
        <view class="cat-icon">
          <text class="icon-text">{{ cat.name.charAt(0) }}</text>
        </view>
        <text class="cat-name">{{ cat.name }}</text>
        <text class="cat-count">{{ cat.articleCount || 0 }} 篇</text>
      </view>
    </view>

    <view class="empty" v-if="categories.length === 0">
      <text>暂无分类</text>
    </view>
  </view>
</template>

<script>
import { listCategories } from '@/api/category'

export default {
  data() {
    return { categories: [] }
  },

  onLoad() {
    this.fetchCategories()
  },

  methods: {
    async fetchCategories() {
      try {
        const res = await listCategories()
        this.categories = res.data || []
      } catch (e) {}
    },

    goCategory(cat) {
      uni.navigateTo({
        url: `/pages/search/index?categoryId=${cat.id}&title=${cat.name}`
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.category-page {
  padding: 20rpx;
}

.category-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 20rpx;
}

.category-item {
  width: calc(33.33% - 14rpx);
  background: #fff;
  border-radius: 16rpx;
  padding: 30rpx 16rpx;
  text-align: center;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.06);

  .cat-icon {
    width: 80rpx;
    height: 80rpx;
    background: linear-gradient(135deg, #409eff, #66b1ff);
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    margin: 0 auto 12rpx;

    .icon-text {
      color: #fff;
      font-size: 36rpx;
      font-weight: bold;
    }
  }

  .cat-name {
    display: block;
    font-size: 28rpx;
    font-weight: bold;
    margin-bottom: 6rpx;
  }

  .cat-count {
    font-size: 22rpx;
    color: #909399;
  }
}

.empty {
  text-align: center;
  padding: 100rpx;
  color: #909399;
}
</style>
