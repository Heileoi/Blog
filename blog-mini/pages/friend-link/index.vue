<!--
  友情链接页面
-->
<template>
  <view class="friend-link-page">
    <view class="link-list">
      <view class="link-card card" v-for="link in links" :key="link.id" @click="openLink(link.url)">
        <text class="link-name">{{ link.name }}</text>
        <text class="link-desc">{{ link.description || '暂无描述' }}</text>
      </view>
    </view>

    <view class="empty" v-if="links.length === 0">
      <text>暂无友情链接</text>
    </view>
  </view>
</template>

<script>
import { listFriendLinks } from '@/api/friendLink'

export default {
  data() {
    return { links: [] }
  },

  onLoad() {
    this.fetchLinks()
  },

  methods: {
    async fetchLinks() {
      try {
        const res = await listFriendLinks()
        this.links = res.data || []
      } catch (e) {}
    },

    openLink(url) {
      uni.setClipboardData({
        data: url,
        success: () => {
          uni.showToast({ title: '链接已复制', icon: 'success' })
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.friend-link-page {
  padding: 20rpx;
}

.link-card {
  margin-bottom: 16rpx;

  .link-name {
    font-size: 30rpx;
    font-weight: bold;
    color: #409eff;
    display: block;
    margin-bottom: 8rpx;
  }

  .link-desc {
    font-size: 26rpx;
    color: #909399;
  }
}

.empty {
  text-align: center;
  padding: 100rpx;
  color: #909399;
}
</style>
