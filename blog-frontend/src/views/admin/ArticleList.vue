<!--
  文章管理页面
  功能：文章列表展示、搜索、状态筛选、删除、置顶等操作
-->
<template>
  <div class="article-list-page">
    <div class="card">
      <!-- 操作栏 -->
      <div class="action-bar">
        <el-button type="primary" @click="$router.push('/admin/article/edit')">
          <el-icon><Plus /></el-icon> 写文章
        </el-button>
        <el-input v-model="keyword" placeholder="搜索文章标题..." style="width: 250px" @keyup.enter="fetchArticles" clearable />
        <el-select v-model="statusFilter" placeholder="状态筛选" clearable style="width: 120px" @change="fetchArticles">
          <el-option label="草稿" :value="0" />
          <el-option label="已发布" :value="1" />
          <el-option label="已下架" :value="2" />
        </el-select>
      </div>

      <!-- 文章表格 -->
      <el-table :data="articles" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="title" label="标题" min-width="200">
          <template #default="{ row }">
            <router-link :to="`/admin/article/edit/${row.id}`" class="article-link">{{ row.title }}</router-link>
          </template>
        </el-table-column>
        <el-table-column prop="categoryName" label="分类" width="100" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : row.status === 2 ? 'danger' : 'warning'" size="small">
              {{ row.status === 1 ? '已发布' : row.status === 2 ? '已下架' : '草稿' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="viewCount" label="浏览" width="80" />
        <el-table-column prop="commentCount" label="评论" width="80" />
        <el-table-column prop="isTop" label="置顶" width="80">
          <template #default="{ row }">
            <el-switch :model-value="row.isTop === 1" @change="(val) => handleTop(row.id, val ? 1 : 0)" />
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="170" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="$router.push(`/admin/article/edit/${row.id}`)">编辑</el-button>
            <el-button size="small" :type="row.status === 1 ? 'warning' : 'success'" @click="handleStatus(row.id, row.status === 1 ? 2 : 1)">
              {{ row.status === 1 ? '下架' : '发布' }}
            </el-button>
            <el-popconfirm title="确定删除该文章吗？" @confirm="handleDelete(row.id)">
              <template #reference>
                <el-button size="small" type="danger">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination v-model:current-page="pageNum" :page-size="pageSize" :total="total"
                       layout="total, prev, pager, next, jumper" @current-change="fetchArticles" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { listArticlesForAdmin, deleteArticle, updateArticleStatus, updateArticleTop } from '@/api/article'

const articles = ref([])
const loading = ref(false)
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const keyword = ref('')
const statusFilter = ref(null)

const fetchArticles = async () => {
  loading.value = true
  try {
    const res = await listArticlesForAdmin({
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      keyword: keyword.value,
      status: statusFilter.value
    })
    articles.value = res.data.list
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

const handleDelete = async (id) => {
  await deleteArticle(id)
  ElMessage.success('删除成功')
  fetchArticles()
}

const handleStatus = async (id, status) => {
  await updateArticleStatus(id, status)
  ElMessage.success('状态更新成功')
  fetchArticles()
}

const handleTop = async (id, isTop) => {
  await updateArticleTop(id, isTop)
  ElMessage.success('置顶状态更新成功')
  fetchArticles()
}

onMounted(fetchArticles)
</script>

<style lang="scss" scoped>
.action-bar {
  display: flex;
  gap: 10px;
  margin-bottom: 15px;
}

.article-link {
  color: var(--primary-color);
  text-decoration: none;
  &:hover {
    text-decoration: underline;
  }
}

.pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 15px;
}
</style>
