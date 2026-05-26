<!--
  友链页面
  功能：展示友情链接、申请友链
-->
<template>
  <div class="friend-link-page">
    <h2 class="page-title">友情链接</h2>

    <div class="link-grid">
      <div class="card link-card" v-for="link in links" :key="link.id">
        <a :href="link.url" target="_blank" rel="noopener noreferrer">
          <h3>{{ link.name }}</h3>
          <p>{{ link.description || '暂无描述' }}</p>
        </a>
      </div>
    </div>

    <div class="card apply-card">
      <h3>申请友链</h3>
      <el-form :model="applyForm" label-width="80px">
        <el-form-item label="网站名称">
          <el-input v-model="applyForm.name" placeholder="请输入网站名称" />
        </el-form-item>
        <el-form-item label="网站链接">
          <el-input v-model="applyForm.url" placeholder="请输入网站链接" />
        </el-form-item>
        <el-form-item label="网站描述">
          <el-input v-model="applyForm.description" type="textarea" placeholder="请输入网站描述" />
        </el-form-item>
        <el-form-item label="联系邮箱">
          <el-input v-model="applyForm.email" placeholder="请输入联系邮箱" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleApply">提交申请</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { listApprovedFriendLinks, applyFriendLink } from '@/api/friendLink'

const links = ref([])
const applyForm = reactive({ name: '', url: '', description: '', email: '' })

const handleApply = async () => {
  if (!applyForm.name || !applyForm.url) {
    ElMessage.warning('请填写网站名称和链接')
    return
  }
  await applyFriendLink(applyForm)
  ElMessage.success('友链申请已提交，等待审核')
  Object.assign(applyForm, { name: '', url: '', description: '', email: '' })
}

onMounted(async () => {
  const res = await listApprovedFriendLinks()
  links.value = res.data
})
</script>

<style lang="scss" scoped>
.page-title {
  font-size: 24px;
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 2px solid var(--primary-color);
}

.link-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 15px;
  margin-bottom: 30px;
}

.link-card {
  cursor: pointer;
  transition: transform 0.3s;

  &:hover {
    transform: translateY(-3px);
  }

  a {
    color: inherit;
    text-decoration: none;
  }

  h3 {
    color: var(--primary-color);
    margin-bottom: 8px;
  }

  p {
    color: var(--text-secondary);
    font-size: 14px;
  }
}

.apply-card {
  max-width: 500px;
  margin: 0 auto;

  h3 {
    margin-bottom: 20px;
  }
}
</style>
