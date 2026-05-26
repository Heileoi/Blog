<!--
  归档页面
  功能：按时间线展示所有文章
-->
<template>
  <div class="archive-page">
    <h2 class="page-title">文章归档</h2>
    <div class="timeline">
      <div class="timeline-item" v-for="item in archives" :key="item.year + '-' + item.month">
        <div class="timeline-date">
          <span class="year">{{ item.year }}年</span>
          <span class="month">{{ item.month }}月</span>
          <span class="count">({{ item.count }}篇)</span>
        </div>
      </div>
    </div>
    <el-empty v-if="archives.length === 0" description="暂无归档数据" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getArchives } from '@/api/article'

const archives = ref([])

onMounted(async () => {
  const res = await getArchives()
  archives.value = res.data
})
</script>

<style lang="scss" scoped>
.page-title {
  font-size: 24px;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 2px solid var(--primary-color);
}

.timeline-item {
  padding: 15px 0;
  border-left: 3px solid var(--primary-color);
  margin-left: 20px;
  padding-left: 20px;
  position: relative;

  &::before {
    content: '';
    width: 12px;
    height: 12px;
    background: var(--primary-color);
    border-radius: 50%;
    position: absolute;
    left: -7px;
    top: 20px;
  }

  .timeline-date {
    .year {
      font-size: 20px;
      font-weight: bold;
      margin-right: 10px;
    }

    .month {
      font-size: 16px;
      margin-right: 8px;
    }

    .count {
      color: var(--text-secondary);
    }
  }
}
</style>
