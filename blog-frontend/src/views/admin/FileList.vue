<!--
  文件管理页面
  功能：文件上传、文件列表、删除
-->
<template>
  <div class="file-list-page">
    <div class="card">
      <div class="action-bar">
        <el-upload action="/api/admin/file/upload" :headers="uploadHeaders" :on-success="handleUploadSuccess" :show-file-list="false">
          <el-button type="primary"><el-icon><Upload /></el-icon> 上传文件</el-button>
        </el-upload>
      </div>

      <el-table :data="files" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="originalName" label="文件名" min-width="200" show-overflow-tooltip />
        <el-table-column prop="mimeType" label="类型" width="120" />
        <el-table-column label="大小" width="100">
          <template #default="{ row }">
            {{ formatSize(row.fileSize) }}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="上传时间" width="170" />
        <el-table-column label="操作" width="150">
          <template #default="{ row }">
            <el-button size="small" @click="handleCopy(row.url)">复制链接</el-button>
            <el-popconfirm title="确定删除该文件吗？" @confirm="handleDelete(row.id)">
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
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { listFiles, deleteFile } from '@/api/file'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const files = ref([])
const loading = ref(false)
const pageNum = ref(1)
const pageSize = ref(20)
const total = ref(0)

const uploadHeaders = computed(() => ({
  Authorization: `Bearer ${userStore.token}`
}))

const formatSize = (bytes) => {
  if (!bytes) return '0 B'
  const units = ['B', 'KB', 'MB', 'GB']
  let i = 0
  while (bytes >= 1024 && i < units.length - 1) { bytes /= 1024; i++ }
  return bytes.toFixed(1) + ' ' + units[i]
}

const handleUploadSuccess = (res) => {
  ElMessage.success('上传成功')
  fetchData()
}

const handleCopy = (url) => {
  navigator.clipboard.writeText(url)
  ElMessage.success('链接已复制')
}

const handleDelete = async (id) => {
  await deleteFile(id)
  ElMessage.success('删除成功')
  fetchData()
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await listFiles({ pageNum: pageNum.value, pageSize: pageSize.value })
    files.value = res.data.list
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

onMounted(fetchData)
</script>

<style lang="scss" scoped>
.action-bar {
  margin-bottom: 15px;
}
.pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 15px;
}
</style>
