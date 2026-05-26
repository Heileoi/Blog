<!--
  友链管理页面
-->
<template>
  <div class="friend-link-page">
    <div class="card">
      <el-table :data="links" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="name" label="网站名称" />
        <el-table-column prop="url" label="链接" show-overflow-tooltip />
        <el-table-column prop="description" label="描述" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : row.status === 2 ? 'danger' : 'warning'" size="small">
              {{ row.status === 1 ? '已通过' : row.status === 2 ? '已拒绝' : '待审核' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220">
          <template #default="{ row }">
            <el-button size="small" type="success" @click="handleAudit(row.id, 1)" v-if="row.status !== 1">通过</el-button>
            <el-button size="small" type="warning" @click="handleAudit(row.id, 2)" v-if="row.status !== 2">拒绝</el-button>
            <el-popconfirm title="确定删除吗？" @confirm="handleDelete(row.id)">
              <template #reference><el-button size="small" type="danger">删除</el-button></template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { listAllFriendLinks, auditFriendLink, deleteFriendLink } from '@/api/friendLink'

const links = ref([])
const loading = ref(false)

const fetchData = async () => {
  loading.value = true
  try { const res = await listAllFriendLinks(); links.value = res.data } finally { loading.value = false }
}
const handleAudit = async (id, status) => { await auditFriendLink(id, status); ElMessage.success('审核成功'); fetchData() }
const handleDelete = async (id) => { await deleteFriendLink(id); ElMessage.success('删除成功'); fetchData() }
onMounted(fetchData)
</script>
