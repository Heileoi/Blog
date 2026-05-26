<!--
  分类管理页面
  功能：分类的增删改查
-->
<template>
  <div class="category-list-page">
    <div class="card">
      <div class="action-bar">
        <el-button type="primary" @click="showDialog()">新增分类</el-button>
      </div>

      <el-table :data="categories" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="name" label="分类名称" />
        <el-table-column prop="slug" label="别名" />
        <el-table-column prop="description" label="描述" />
        <el-table-column prop="articleCount" label="文章数" width="80" />
        <el-table-column prop="sortOrder" label="排序" width="80" />
        <el-table-column label="操作" width="180">
          <template #default="{ row }">
            <el-button size="small" @click="showDialog(row)">编辑</el-button>
            <el-popconfirm title="确定删除该分类吗？" @confirm="handleDelete(row.id)">
              <template #reference>
                <el-button size="small" type="danger">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑分类' : '新增分类'" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="分类名称" required>
          <el-input v-model="form.name" placeholder="请输入分类名称" />
        </el-form-item>
        <el-form-item label="别名">
          <el-input v-model="form.slug" placeholder="URL友好的别名" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" placeholder="分类描述" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="form.sortOrder" :min="0" />
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
import { listCategories, createCategory, updateCategory, deleteCategory } from '@/api/category'

const categories = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const submitLoading = ref(false)
const isEdit = ref(false)

const form = reactive({ id: null, name: '', slug: '', description: '', sortOrder: 0 })

const showDialog = (row) => {
  isEdit.value = !!row
  if (row) {
    Object.assign(form, row)
  } else {
    Object.assign(form, { id: null, name: '', slug: '', description: '', sortOrder: 0 })
  }
  dialogVisible.value = true
}

const handleSubmit = async () => {
  if (!form.name) return ElMessage.warning('请输入分类名称')
  submitLoading.value = true
  try {
    if (isEdit.value) {
      await updateCategory(form)
      ElMessage.success('分类更新成功')
    } else {
      await createCategory(form)
      ElMessage.success('分类创建成功')
    }
    dialogVisible.value = false
    fetchData()
  } finally {
    submitLoading.value = false
  }
}

const handleDelete = async (id) => {
  await deleteCategory(id)
  ElMessage.success('删除成功')
  fetchData()
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await listCategories()
    categories.value = res.data
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
</style>
