<!--
  用户管理页面
  功能：用户列表、状态管理、角色管理
-->
<template>
  <div class="user-list-page">
    <div class="card">
      <div class="action-bar">
        <el-input v-model="keyword" placeholder="搜索用户名/昵称/邮箱..." style="width: 300px" @keyup.enter="fetchData" clearable />
      </div>

      <el-table :data="users" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column label="用户信息" min-width="200">
          <template #default="{ row }">
            <div class="user-info">
              <el-avatar :src="row.avatar" :size="32" />
              <div>
                <div class="username">{{ row.username }}</div>
                <div class="nickname">{{ row.nickname }}</div>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="email" label="邮箱" />
        <el-table-column prop="role" label="角色" width="100">
          <template #default="{ row }">
            <el-tag :type="row.role === 1 ? 'danger' : 'info'" size="small">
              {{ row.role === 1 ? '管理员' : '普通用户' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-switch :model-value="row.status === 1" @change="(val) => handleStatus(row.id, val ? 1 : 0)" />
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="注册时间" width="170" />
        <el-table-column label="操作" width="150">
          <template #default="{ row }">
            <el-button size="small" @click="handleRole(row.id, row.role === 1 ? 0 : 1)">
              {{ row.role === 1 ? '取消管理员' : '设为管理员' }}
            </el-button>
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
import axios from '@/utils/request'

const users = ref([])
const loading = ref(false)
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const keyword = ref('')

const fetchData = async () => {
  loading.value = true
  try {
    const res = await axios.get('/admin/user/list', { params: { pageNum: pageNum.value, pageSize: pageSize.value, keyword: keyword.value } })
    users.value = res.data.list
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

const handleStatus = async (id, status) => {
  await axios.put('/admin/user/status', null, { params: { id, status } })
  ElMessage.success('状态更新成功')
  fetchData()
}

const handleRole = async (id, role) => {
  await axios.put('/admin/user/role', null, { params: { id, role } })
  ElMessage.success('角色更新成功')
  fetchData()
}

onMounted(fetchData)
</script>

<style lang="scss" scoped>
.action-bar {
  margin-bottom: 15px;
}
.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
  .username { font-weight: bold; }
  .nickname { font-size: 12px; color: var(--text-secondary); }
}
.pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 15px;
}
</style>
