<!--
  文章编辑页面
  功能：创建和编辑文章，支持Markdown编辑器、分类选择、标签选择等
-->
<template>
  <div class="article-edit-page">
    <div class="card">
      <h3>{{ isEdit ? '编辑文章' : '写文章' }}</h3>

      <el-form :model="form" label-width="100px" class="edit-form">
        <!-- 标题 -->
        <el-form-item label="文章标题" required>
          <el-input v-model="form.title" placeholder="请输入文章标题" maxlength="200" show-word-limit />
        </el-form-item>

        <!-- 分类 -->
        <el-form-item label="文章分类" required>
          <el-select v-model="form.categoryId" placeholder="请选择分类">
            <el-option v-for="cat in categories" :key="cat.id" :label="cat.name" :value="cat.id" />
          </el-select>
        </el-form-item>

        <!-- 标签 -->
        <el-form-item label="文章标签">
          <el-select v-model="form.tagIds" multiple placeholder="请选择标签">
            <el-option v-for="tag in tags" :key="tag.id" :label="tag.name" :value="tag.id" />
          </el-select>
        </el-form-item>

        <!-- 摘要 -->
        <el-form-item label="文章摘要">
          <el-input v-model="form.summary" type="textarea" :rows="2" placeholder="文章摘要（不填则自动截取）" />
        </el-form-item>

        <!-- 封面图 -->
        <el-form-item label="封面图片">
          <el-input v-model="form.coverImage" placeholder="封面图片URL" />
        </el-form-item>

        <!-- Markdown编辑器 -->
        <el-form-item label="文章内容" required>
          <div class="editor-wrapper">
            <textarea v-model="form.content" class="markdown-editor" placeholder="请输入Markdown内容..."></textarea>
          </div>
        </el-form-item>

        <!-- 其他选项 -->
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="文章来源">
              <el-select v-model="form.source">
                <el-option label="原创" value="原创" />
                <el-option label="转载" value="转载" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="允许评论">
              <el-switch v-model="form.isCommentEnabled" :active-value="1" :inactive-value="0" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="推荐文章">
              <el-switch v-model="form.isFeatured" :active-value="1" :inactive-value="0" />
            </el-form-item>
          </el-col>
        </el-row>

        <!-- SEO关键词 -->
        <el-form-item label="SEO关键词">
          <el-input v-model="form.keywords" placeholder="多个关键词用逗号分隔" />
        </el-form-item>

        <!-- 操作按钮 -->
        <el-form-item>
          <el-button type="primary" @click="handleSubmit(1)" :loading="loading">发布文章</el-button>
          <el-button @click="handleSubmit(0)" :loading="loading">保存草稿</el-button>
          <el-button @click="$router.back()">取消</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { createArticle, updateArticle, getArticleDetail } from '@/api/article'
import { listCategories } from '@/api/category'
import { listTags } from '@/api/tag'

const route = useRoute()
const router = useRouter()

const isEdit = computed(() => !!route.params.id)
const loading = ref(false)
const categories = ref([])
const tags = ref([])

const form = reactive({
  title: '',
  content: '',
  summary: '',
  coverImage: '',
  categoryId: null,
  tagIds: [],
  source: '原创',
  isCommentEnabled: 1,
  isFeatured: 0,
  keywords: ''
})

/** 提交文章 */
const handleSubmit = async (status) => {
  if (!form.title) return ElMessage.warning('请输入文章标题')
  if (!form.content) return ElMessage.warning('请输入文章内容')
  if (!form.categoryId) return ElMessage.warning('请选择文章分类')

  loading.value = true
  try {
    const data = { ...form, status }
    if (isEdit.value) {
      data.id = parseInt(route.params.id)
      await updateArticle(data)
      ElMessage.success('文章更新成功')
    } else {
      await createArticle(data)
      ElMessage.success('文章创建成功')
    }
    router.push('/admin/article')
  } finally {
    loading.value = false
  }
}

onMounted(async () => {
  // 加载分类和标签
  const [catRes, tagRes] = await Promise.all([listCategories(), listTags()])
  categories.value = catRes.data
  tags.value = tagRes.data

  // 编辑模式：加载文章数据
  if (isEdit.value) {
    const res = await getArticleDetail(route.params.id)
    const article = res.data
    Object.assign(form, {
      title: article.title,
      content: article.content,
      summary: article.summary,
      coverImage: article.coverImage,
      categoryId: article.categoryId,
      tagIds: article.tags?.map(t => t.id) || [],
      source: article.source || '原创',
      isCommentEnabled: article.isCommentEnabled ?? 1,
      isFeatured: article.isFeatured ?? 0,
      keywords: article.keywords
    })
  }
})
</script>

<style lang="scss" scoped>
.edit-form {
  max-width: 900px;
}

.editor-wrapper {
  width: 100%;
}

.markdown-editor {
  width: 100%;
  min-height: 400px;
  padding: 15px;
  font-family: 'Monaco', 'Menlo', 'Consolas', monospace;
  font-size: 14px;
  line-height: 1.6;
  border: 1px solid var(--border-color);
  border-radius: 4px;
  resize: vertical;
  outline: none;

  &:focus {
    border-color: var(--primary-color);
  }
}
</style>
