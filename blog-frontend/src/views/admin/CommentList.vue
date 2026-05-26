<!--
  评论管理页面
  功能：评论列表、审核、删除
-->
<template>
  <div class="comment-list-page">
    <div class="card">
      <div class="action-bar">
        <el-input v-model="keyword" placeholder="搜索评论内容..." style="width: 250px" @keyup.enter="fetchData" clearable />
        <el-select v-model="statusFilter" placeholder="状态筛选" clearable @change="fetchData">
          <el-option label="待审核" :value="0" />
          <el-option label="已通过" :value="1" />
          <el-option label="已拒绝" :value="2" />
        </el-select>
      </div>

      <el-table :data="comments" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="content" label="评论内容" min-width="200" show-overflow-tooltip />
        <el-table-column prop="nickname" label="评论者" width="120">
          <template #default="{ row }">
            {{ row.nickname || row.user?.nickname || '匿名' }}
          </template>
        </el-table-column>
        <el-table-column prop="articleId" label="文章ID" width="80" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : row.status === 2 ? 'danger' : 'warning'" size="small">
              {{ row.status === 1 ? '已通过' : row.status === 2 ? '已拒绝' : '待审核' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="评论时间" width="170" />
        <el-table-column label="操作" width="220">
          <template #default="{ row }">
            <el-button size="small" type="success" @click="handleAudit(row.id, 1)" v-if="row.status !== 1">通过</el-button>
            <el-button size="small" type="warning" @click="handleAudit(row.id, 2)" v-if="row.status !== 2">拒绝</el-button>
            <el-popconfirm title="确定删除该评论吗？" @confirm="handleDelete(row.id)">
              <template #reference>
                <el-button size="small" type="danger">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination v-model:current-page="pageNum" :page-size="pageSize" :total="total"
                       layout="total, prev, pager, next" @current-change="fetchData" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { listCommentsForAdmin, auditComment, deleteComment } from '@/api/comment'

const comments = ref([])
const loading = ref(false)
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const keyword = ref('')
const statusFilter = ref(null)

const fetchData = async () => {
  loading.value = true
  try {
    const res = await listCommentsForAdmin({ pageNum: pageNum.value, pageSize: pageSize.value, keyword: keyword.value, status: statusFilter.value })
    comments.value = res.data.list
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

const handleAudit = async (id, status) => {
  await auditComment(id, status)
  ElMessage.success('审核成功')
  fetchData()
}

const handleDelete = async (id) => {
  await deleteComment(id)
  ElMessage.success('删除成功')
  fetchData()
}

onMounted(fetchData)
</script>

<style lang="scss" scoped>
.action-bar {
  display: flex;
  gap: 10px;
  margin-bottom: 15px;
}
.pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 15px;
}
</style>
