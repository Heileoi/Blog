<!--
  归档页面
  功能：按时间线展示文章归档
-->
<template>
  <view class="archive-page">
    <view class="timeline">
      <view class="timeline-item" v-for="(item, index) in archives" :key="index">
        <view class="timeline-dot" :class="{ active: index === 0 }"></view>
        <view class="timeline-content card">
          <text class="date">{{ item.year }}年{{ item.month }}月</text>
          <text class="count">{{ item.count }} 篇文章</text>
        </view>
      </view>
    </view>

    <view class="empty" v-if="archives.length === 0">
      <text>暂无归档数据</text>
    </view>
  </view>
</template>

<script>
import { getArchives } from '@/api/article'

export default {
  data() {
    return { archives: [] }
  },

  onLoad() {
    this.fetchArchives()
  },

  methods: {
    async fetchArchives() {
      try {
        const res = await getArchives()
        this.archives = res.data || []
      } catch (e) {}
    }
  }
}
</script>

<style lang="scss" scoped>
.archive-page {
  padding: 20rpx 30rpx;
}

.timeline {
  position: relative;
  padding-left: 40rpx;

  &::before {
    content: '';
    position: absolute;
    left: 12rpx;
    top: 0;
    bottom: 0;
    width: 4rpx;
    background: #409eff;
  }
}

.timeline-item {
  position: relative;
  margin-bottom: 24rpx;

  .timeline-dot {
    position: absolute;
    left: -34rpx;
    top: 24rpx;
    width: 20rpx;
    height: 20rpx;
    background: #409eff;
    border-radius: 50%;
    border: 4rpx solid #fff;
    box-shadow: 0 0 0 4rpx #409eff;

    &.active {
      background: #f56c6c;
      box-shadow: 0 0 0 4rpx #f56c6c;
    }
  }

  .timeline-content {
    display: flex;
    justify-content: space-between;
    align-items: center;

    .date {
      font-size: 30rpx;
      font-weight: bold;
    }

    .count {
      font-size: 26rpx;
      color: #409eff;
    }
  }
}

.empty {
  text-align: center;
  padding: 100rpx;
  color: #909399;
}
</style>
