<!--
  公告管理页面
-->
<template>
  <div class="notice-list-page">
    <div class="card">
      <div class="action-bar">
        <el-button type="primary" @click="showDialog()">新增公告</el-button>
      </div>
      <el-table :data="notices" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="title" label="标题" />
        <el-table-column prop="type" label="类型" width="100">
          <template #default="{ row }">{{ ['','系统公告','更新日志','活动通知'][row.type] }}</template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'" size="small">{{ row.status === 1 ? '已发布' : '草稿' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="170" />
        <el-table-column label="操作" width="180">
          <template #default="{ row }">
            <el-button size="small" @click="showDialog(row)">编辑</el-button>
            <el-popconfirm title="确定删除吗？" @confirm="handleDelete(row.id)">
              <template #reference><el-button size="small" type="danger">删除</el-button></template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑公告' : '新增公告'" width="600px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="标题" required><el-input v-model="form.title" /></el-form-item>
        <el-form-item label="内容" required><el-input v-model="form.content" type="textarea" :rows="4" /></el-form-item>
        <el-form-item label="类型"><el-select v-model="form.type"><el-option label="系统公告" :value="1" /><el-option label="更新日志" :value="2" /><el-option label="活动通知" :value="3" /></el-select></el-form-item>
        <el-form-item label="状态"><el-radio-group v-model="form.status"><el-radio :value="0">草稿</el-radio><el-radio :value="1">发布</el-radio></el-radio-group></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" @click="handleSubmit">确定</el-button></template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { listAllNotices, createNotice, updateNotice, deleteNotice } from '@/api/notice'

const notices = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const form = reactive({ id: null, title: '', content: '', type: 1, status: 0 })

const showDialog = (row) => {
  isEdit.value = !!row
  Object.assign(form, row || { id: null, title: '', content: '', type: 1, status: 0 })
  dialogVisible.value = true
}
const handleSubmit = async () => {
  if (!form.title || !form.content) return ElMessage.warning('请填写标题和内容')
  isEdit.value ? await updateNotice(form) : await createNotice(form)
  ElMessage.success(isEdit.value ? '更新成功' : '创建成功')
  dialogVisible.value = false
  fetchData()
}
const handleDelete = async (id) => { await deleteNotice(id); ElMessage.success('删除成功'); fetchData() }
const fetchData = async () => { loading.value = true; try { const res = await listAllNotices(); notices.value = res.data } finally { loading.value = false } }
onMounted(fetchData)
</script>

<style lang="scss" scoped>.action-bar { margin-bottom: 15px; }</style>
