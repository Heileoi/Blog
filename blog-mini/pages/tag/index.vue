<!--
  标签页面
  功能：展示标签云
-->
<template>
  <view class="tag-page">
    <view class="card">
      <view class="tag-cloud">
        <view class="tag-item" v-for="tag in tags" :key="tag.id"
              :style="{ backgroundColor: tag.color || '#409eff' }"
              @click="goTag(tag)">
          <text class="tag-name">{{ tag.name }}</text>
          <text class="tag-count">{{ tag.articleCount || 0 }}</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { getHotTags } from '@/api/tag'

export default {
  data() {
    return { tags: [] }
  },

  onLoad() {
    this.fetchTags()
  },

  methods: {
    async fetchTags() {
      try {
        const res = await getHotTags(50)
        this.tags = res.data || []
      } catch (e) {}
    },

    goTag(tag) {
      uni.navigateTo({
        url: `/pages/search/index?tagId=${tag.id}&title=${tag.name}`
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.tag-page {
  padding: 20rpx;
}

.tag-cloud {
  display: flex;
  flex-wrap: wrap;
  gap: 16rpx;
  justify-content: center;
  padding: 20rpx;
}

.tag-item {
  display: flex;
  align-items: center;
  padding: 12rpx 24rpx;
  border-radius: 30rpx;
  color: #fff;

  .tag-name {
    font-size: 26rpx;
    margin-right: 8rpx;
  }

  .tag-count {
    font-size: 22rpx;
    opacity: 0.8;
  }
}
</style>
