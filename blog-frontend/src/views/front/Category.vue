<!--
  分类页面
  功能：展示所有分类及文章数量，点击分类查看对应文章
-->
<template>
  <div class="category-page">
    <h2 class="page-title">文章分类</h2>
    <div class="category-grid">
      <div class="card category-card" v-for="cat in categories" :key="cat.id"
           @click="$router.push({ path: '/', query: { categoryId: cat.id } })">
        <div class="cat-icon">
          <el-icon :size="40"><Folder /></el-icon>
        </div>
        <h3>{{ cat.name }}</h3>
        <p>{{ cat.description || '暂无描述' }}</p>
        <span class="count">{{ cat.articleCount || 0 }} 篇文章</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { listFrontCategories } from '@/api/category'

const categories = ref([])

onMounted(async () => {
  const res = await listFrontCategories()
  categories.value = res.data
})
</script>

<style lang="scss" scoped>
.page-title {
  font-size: 24px;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 2px solid var(--primary-color);
}

.category-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 20px;
}

.category-card {
  text-align: center;
  cursor: pointer;
  transition: transform 0.3s;

  &:hover {
    transform: translateY(-5px);
  }

  .cat-icon {
    color: var(--primary-color);
    margin-bottom: 10px;
  }

  h3 {
    margin-bottom: 8px;
  }

  p {
    color: var(--text-secondary);
    font-size: 14px;
    margin-bottom: 10px;
  }

  .count {
    color: var(--primary-color);
    font-weight: bold;
  }
}
</style>
