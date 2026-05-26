<!--
  标签管理页面
  功能：标签的增删改查
-->
<template>
  <div class="tag-list-page">
    <div class="card">
      <div class="action-bar">
        <el-button type="primary" @click="showDialog()">新增标签</el-button>
      </div>

      <el-table :data="tags" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="name" label="标签名称" />
        <el-table-column prop="slug" label="别名" />
        <el-table-column prop="color" label="颜色" width="100">
          <template #default="{ row }">
            <div class="color-preview" :style="{ backgroundColor: row.color || '#409eff' }"></div>
          </template>
        </el-table-column>
        <el-table-column prop="articleCount" label="文章数" width="80" />
        <el-table-column label="操作" width="180">
          <template #default="{ row }">
            <el-button size="small" @click="showDialog(row)">编辑</el-button>
            <el-popconfirm title="确定删除该标签吗？" @confirm="handleDelete(row.id)">
              <template #reference>
                <el-button size="small" type="danger">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑标签' : '新增标签'" width="400px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="标签名称" required>
          <el-input v-model="form.name" placeholder="请输入标签名称" />
        </el-form-item>
        <el-form-item label="别名">
          <el-input v-model="form.slug" placeholder="URL友好的别名" />
        </el-form-item>
        <el-form-item label="颜色">
          <el-color-picker v-model="form.color" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { listTags, createTag, updateTag, deleteTag } from '@/api/tag'

const tags = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const submitLoading = ref(false)
const isEdit = ref(false)

const form = reactive({ id: null, name: '', slug: '', color: '#409eff' })

const showDialog = (row) => {
  isEdit.value = !!row
  if (row) {
    Object.assign(form, row)
  } else {
    Object.assign(form, { id: null, name: '', slug: '', color: '#409eff' })
  }
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (!form.name) return ElMessage.warning('请输入标签名称')
  submitLoading.value = true
  try {
    if (isEdit.value) {
      await updateTag(form)
      ElMessage.success('标签更新成功')
    } else {
      await createTag(form)
      ElMessage.success('标签创建成功')
    }
    dialogVisible.value = false
    fetchData()
  } finally {
    submitLoading.value = false
  }
}

const handleDelete = async (id) => {
  await deleteTag(id)
  ElMessage.success('删除成功')
  fetchData()
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await listTags()
    tags.value = res.data
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
.color-preview {
  width: 30px;
  height: 20px;
  border-radius: 4px;
}
</style>
