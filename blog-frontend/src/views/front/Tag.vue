<!--
  标签页面
  功能：展示所有标签云，点击标签查看对应文章
-->
<template>
  <div class="tag-page">
    <h2 class="page-title">文章标签</h2>
    <div class="card tag-cloud-card">
      <div class="tag-cloud">
        <router-link v-for="tag in tags" :key="tag.id"
                     :to="{ path: '/', query: { tagId: tag.id } }">
          <el-tag :size="tag.articleCount > 5 ? 'large' : 'default'"
                  :style="{ backgroundColor: tag.color || '#409eff', color: '#fff', border: 'none' }"
                  class="tag-item" effect="dark">
            {{ tag.name }} ({{ tag.articleCount || 0 }})
          </el-tag>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getHotTags } from '@/api/tag'

const tags = ref([])

onMounted(async () => {
  const res = await getHotTags(50)
  tags.value = res.data
})
</script>

<style lang="scss" scoped>
.page-title {
  font-size: 24px;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 2px solid var(--primary-color);
}

.tag-cloud-card {
  padding: 30px;
}

.tag-cloud {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  justify-content: center;

  .tag-item {
    cursor: pointer;
    transition: transform 0.3s;

    &:hover {
      transform: scale(1.1);
    }
  }
}
</style>
