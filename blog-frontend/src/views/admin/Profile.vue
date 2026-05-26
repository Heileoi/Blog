<!--
  个人信息页面
  功能：查看和修改个人信息、修改密码
-->
<template>
  <div class="profile-page">
    <div class="card">
      <h3>个人信息</h3>
      <el-form :model="form" label-width="100px" style="max-width: 500px">
        <el-form-item label="用户名">
          <el-input :value="form.username" disabled />
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="form.nickname" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" />
        </el-form-item>
        <el-form-item label="个人简介">
          <el-input v-model="form.bio" type="textarea" :rows="3" />
        </el-form-item>
        <el-form-item label="个人网站">
          <el-input v-model="form.website" />
        </el-form-item>
        <el-form-item label="GitHub">
          <el-input v-model="form.github" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleUpdate">保存修改</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="card">
      <h3>修改密码</h3>
      <el-form :model="pwdForm" label-width="100px" style="max-width: 500px">
        <el-form-item label="原密码" required>
          <el-input v-model="pwdForm.oldPassword" type="password" show-password />
        </el-form-item>
        <el-form-item label="新密码" required>
          <el-input v-model="pwdForm.newPassword" type="password" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleChangePassword">修改密码</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getUserInfo } from '@/api/auth'
import axios from '@/utils/request'

const form = reactive({ username: '', nickname: '', email: '', bio: '', website: '', github: '' })
const pwdForm = reactive({ oldPassword: '', newPassword: '' })

const handleUpdate = async () => {
  await axios.put('/admin/profile', form)
  ElMessage.success('个人信息更新成功')
}

const handleChangePassword = async () => {
  if (!pwdForm.oldPassword || !pwdForm.newPassword) return ElMessage.warning('请填写密码')
  await axios.put('/admin/profile/password', pwdForm)
  ElMessage.success('密码修改成功')
  pwdForm.oldPassword = ''
  pwdForm.newPassword = ''
}

onMounted(async () => {
  const res = await getUserInfo()
  Object.assign(form, res.data)
})
</script>

<style lang="scss" scoped>
.card { margin-bottom: 20px; h3 { margin-bottom: 20px; } }
</style>
