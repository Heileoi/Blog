<!--
  关于页面
  功能：展示博主信息、技术栈、联系方式
-->
<template>
  <view class="about-page">
    <!-- 头像和简介 -->
    <view class="profile-card card">
      <image class="avatar" src="/static/default-avatar.png" mode="aspectFill" />
      <text class="name">王熙磊</text>
      <text class="title">资深Java开发工程师</text>
      <text class="bio">10年+Java开发经验，精通Spring Boot、微服务架构、数据库优化等技术领域。</text>
    </view>

    <!-- 技术栈 -->
    <view class="card">
      <view class="section-title">技术栈</view>
      <view class="tech-list">
        <text class="tech-item" v-for="tech in techStack" :key="tech">{{ tech }}</text>
      </view>
    </view>

    <!-- 联系方式 -->
    <view class="card">
      <view class="section-title">联系方式</view>
      <view class="contact-item">
        <text class="label">邮箱：</text>
        <text class="value">admin@xilei.com</text>
      </view>
      <view class="contact-item">
        <text class="label">GitHub：</text>
        <text class="value">github.com/xilei</text>
      </view>
    </view>

    <!-- 功能入口 -->
    <view class="card">
      <view class="menu-item" @click="goPage('/pages/friend-link/index')">
        <text>友情链接</text>
        <text class="arrow">›</text>
      </view>
      <view class="menu-item" @click="goPage('/pages/tag/index')">
        <text>标签云</text>
        <text class="arrow">›</text>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      techStack: ['Java', 'Spring Boot', 'MySQL', 'Redis', 'Docker', 'Vue', 'Kafka', 'Elasticsearch', 'Linux', 'Python']
    }
  },

  methods: {
    goPage(url) {
      uni.navigateTo({ url })
    }
  }
}
</script>

<style lang="scss" scoped>
.about-page {
  padding: 20rpx;
}

.profile-card {
  text-align: center;
  padding: 40rpx;

  .avatar {
    width: 160rpx;
    height: 160rpx;
    border-radius: 50%;
    margin-bottom: 20rpx;
  }

  .name {
    display: block;
    font-size: 36rpx;
    font-weight: bold;
    margin-bottom: 8rpx;
  }

  .title {
    display: block;
    font-size: 26rpx;
    color: #909399;
    margin-bottom: 16rpx;
  }

  .bio {
    font-size: 26rpx;
    color: #606266;
    line-height: 1.6;
  }
}

.section-title {
  font-size: 30rpx;
  font-weight: bold;
  margin-bottom: 16rpx;
  padding-bottom: 12rpx;
  border-bottom: 2rpx solid #eee;
}

.tech-list {
  display: flex;
  flex-wrap: wrap;
  gap: 12rpx;

  .tech-item {
    background: #ecf5ff;
    color: #409eff;
    padding: 8rpx 20rpx;
    border-radius: 6rpx;
    font-size: 24rpx;
  }
}

.contact-item {
  display: flex;
  padding: 12rpx 0;

  .label {
    color: #909399;
    font-size: 28rpx;
  }

  .value {
    color: #303133;
    font-size: 28rpx;
  }
}

.menu-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24rpx 0;
  border-bottom: 2rpx solid #f0f0f0;
  font-size: 30rpx;

  &:last-child {
    border-bottom: none;
  }

  .arrow {
    color: #909399;
    font-size: 36rpx;
  }
}
</style>
