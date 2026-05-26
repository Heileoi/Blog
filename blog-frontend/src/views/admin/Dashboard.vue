<!--
  仪表盘页面
  功能：展示博客核心数据统计、文章发布趋势图表
-->
<template>
  <div class="dashboard-page">
    <!-- 统计卡片 -->
    <div class="stat-cards">
      <div class="stat-card" v-for="stat in statCards" :key="stat.label">
        <div class="stat-icon" :style="{ backgroundColor: stat.color }">
          <el-icon :size="24"><component :is="stat.icon" /></el-icon>
        </div>
        <div class="stat-info">
          <span class="stat-num">{{ stat.value }}</span>
          <span class="stat-label">{{ stat.label }}</span>
        </div>
      </div>
    </div>

    <div class="dashboard-content">
      <!-- 文章趋势图 -->
      <div class="card chart-card">
        <h3>最近7天文章发布趋势</h3>
        <div ref="trendChart" class="chart"></div>
      </div>

      <!-- 最新评论 -->
      <div class="card">
        <h3>最新评论</h3>
        <div class="latest-comments">
          <div class="comment-item" v-for="comment in latestComments" :key="comment.id">
            <p class="comment-content">{{ comment.content }}</p>
            <span class="comment-time">{{ comment.createTime?.substring(0, 16) }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import { getDashboardStats, getArticleTrend } from '@/api/dashboard'
import { getLatestComments } from '@/api/comment'

const trendChart = ref(null)
const latestComments = ref([])

const statCards = reactive([
  { label: '已发布文章', value: 0, icon: 'Document', color: '#409eff' },
  { label: '草稿数量', value: 0, icon: 'Edit', color: '#e6a23c' },
  { label: '评论总数', value: 0, icon: 'ChatDotRound', color: '#67c23a' },
  { label: '用户总数', value: 0, icon: 'User', color: '#f56c6c' },
  { label: '今日文章', value: 0, icon: 'Calendar', color: '#909399' },
  { label: '总浏览量', value: 0, icon: 'View', color: '#9b59b6' }
])

/** 初始化图表 */
const initChart = async () => {
  const res = await getArticleTrend()
  const { dates, counts } = res.data

  await nextTick()
  const chart = echarts.init(trendChart.value)
  chart.setOption({
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', data: dates },
    yAxis: { type: 'value', minInterval: 1 },
    series: [{
      data: counts,
      type: 'line',
      smooth: true,
      areaStyle: { color: 'rgba(64, 158, 255, 0.3)' },
      itemStyle: { color: '#409eff' }
    }]
  })

  window.addEventListener('resize', () => chart.resize())
}

onMounted(async () => {
  // 获取统计数据
  const statsRes = await getDashboardStats()
  const stats = statsRes.data
  statCards[0].value = stats.articleCount || 0
  statCards[1].value = stats.draftCount || 0
  statCards[2].value = stats.commentCount || 0
  statCards[3].value = stats.userCount || 0
  statCards[4].value = stats.todayArticles || 0
  statCards[5].value = stats.totalViews || 0

  // 获取最新评论
  const commentsRes = await getLatestComments(5)
  latestComments.value = commentsRes.data

  // 初始化图表
  initChart()
})
</script>

<style lang="scss" scoped>
.stat-cards {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 15px;
  margin-bottom: 20px;
}

.stat-card {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 15px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);

  .stat-icon {
    width: 50px;
    height: 50px;
    border-radius: 12px;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #fff;
  }

  .stat-info {
    .stat-num {
      display: block;
      font-size: 24px;
      font-weight: bold;
      color: var(--text-primary);
    }

    .stat-label {
      font-size: 13px;
      color: var(--text-secondary);
    }
  }
}

.dashboard-content {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 20px;
}

.chart-card {
  h3 {
    margin-bottom: 15px;
  }
}

.chart {
  height: 300px;
}

.latest-comments {
  .comment-item {
    padding: 10px 0;
    border-bottom: 1px solid #f0f0f0;

    .comment-content {
      font-size: 14px;
      color: var(--text-regular);
      margin-bottom: 4px;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
      overflow: hidden;
    }

    .comment-time {
      font-size: 12px;
      color: var(--text-secondary);
    }
  }
}
</style>
