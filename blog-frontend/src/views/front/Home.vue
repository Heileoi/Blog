<!--
  首页
  功能：展示文章列表、热门文章、标签云、公告等
-->
<template>
  <div class="home-page">
    <!-- 轮播公告 -->
    <div class="notice-bar" v-if="notices.length">
      <el-alert :title="notices[0].title" :description="notices[0].content" type="info" show-icon :closable="false" />
    </div>

    <div class="home-content">
      <!-- 左侧文章列表 -->
      <div class="article-list">
        <div class="card" v-for="article in articles" :key="article.id">
          <div class="article-item">
            <!-- 封面图 -->
            <div class="article-cover" v-if="article.coverImage">
              <img :src="article.coverImage" :alt="article.title" />
            </div>
            <div class="article-info">
              <!-- 标题 -->
              <router-link :to="`/article/${article.id}`" class="article-title">
                <el-tag v-if="article.isTop" type="danger" size="small" class="top-tag">置顶</el-tag>
                {{ article.title }}
              </router-link>
              <!-- 摘要 -->
              <p class="article-summary">{{ article.summary }}</p>
              <!-- 元信息 -->
              <div class="article-meta">
                <span><el-icon><Calendar /></el-icon> {{ formatDate(article.publishTime) }}</span>
                <span><el-icon><View /></el-icon> {{ article.viewCount }} 阅读</span>
                <span><el-icon><ChatDotRound /></el-icon> {{ article.commentCount }} 评论</span>
                <span class="category" v-if="article.categoryName">
                  <el-icon><Folder /></el-icon> {{ article.categoryName }}
                </span>
              </div>
              <!-- 标签 -->
              <div class="article-tags" v-if="article.tags?.length">
                <el-tag v-for="tag in article.tags" :key="tag.id" size="small" type="info" effect="plain">
                  {{ tag.name }}
                </el-tag>
              </div>
            </div>
          </div>
        </div>

        <!-- 分页 -->
        <div class="pagination" v-if="total > 0">
          <el-pagination
            v-model:current-page="pageNum"
            :page-size="pageSize"
            :total="total"
            layout="prev, pager, next"
            @current-change="fetchArticles"
          />
        </div>

        <!-- 空状态 -->
        <el-empty v-if="!loading && articles.length === 0" description="暂无文章" />
      </div>

      <!-- 右侧边栏 -->
      <aside class="sidebar">
        <!-- 博主信息 -->
        <div class="card author-card">
          <el-avatar :size="80" src="/default-avatar.png" />
          <h3>王熙磊</h3>
          <p>资深Java开发工程师</p>
          <div class="stats">
            <div class="stat-item">
              <span class="num">{{ total }}</span>
              <span class="label">文章</span>
            </div>
            <div class="stat-item">
              <span class="num">{{ categoryCount }}</span>
              <span class="label">分类</span>
            </div>
            <div class="stat-item">
              <span class="num">{{ tagCount }}</span>
              <span class="label">标签</span>
            </div>
          </div>
        </div>

        <!-- 热门文章 -->
        <div class="card">
          <h4 class="sidebar-title">热门文章</h4>
          <div class="hot-article" v-for="(item, index) in hotArticles" :key="item.id">
            <span class="rank" :class="{ top: index < 3 }">{{ index + 1 }}</span>
            <router-link :to="`/article/${item.id}`" class="title">{{ item.title }}</router-link>
          </div>
        </div>

        <!-- 标签云 -->
        <div class="card">
          <h4 class="sidebar-title">标签云</h4>
          <div class="tag-cloud">
            <router-link v-for="tag in tags" :key="tag.id" :to="{ path: '/', query: { tagId: tag.id } }">
              <el-tag :style="{ backgroundColor: tag.color || '#409eff', color: '#fff' }" class="tag-item">
                {{ tag.name }} ({{ tag.articleCount || 0 }})
              </el-tag>
            </router-link>
          </div>
        </div>
      </aside>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { listPublishedArticles, getHotArticles } from '@/api/article'
import { listFrontCategories } from '@/api/category'
import { getHotTags } from '@/api/tag'
import { listPublishedNotices } from '@/api/notice'

const route = useRoute()

const articles = ref([])
const hotArticles = ref([])
const tags = ref([])
const notices = ref([])
const loading = ref(false)
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const categoryCount = ref(0)
const tagCount = ref(0)

/** 格式化日期 */
const formatDate = (date) => {
  if (!date) return ''
  return date.substring(0, 10)
}

/** 获取文章列表 */
const fetchArticles = async () => {
  loading.value = true
  try {
    const res = await listPublishedArticles({
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      categoryId: route.query.categoryId,
      tagId: route.query.tagId,
      keyword: route.query.keyword
    })
    articles.value = res.data.list
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

/** 初始化数据 */
onMounted(async () => {
  fetchArticles()

  // 获取热门文章
  const hotRes = await getHotArticles(5)
  hotArticles.value = hotRes.data

  // 获取标签
  const tagRes = await getHotTags(20)
  tags.value = tagRes.data
  tagCount.value = tagRes.data.length

  // 获取分类
  const catRes = await listFrontCategories()
  categoryCount.value = catRes.data.length

  // 获取公告
  const noticeRes = await listPublishedNotices()
  notices.value = noticeRes.data
})
</script>

<style lang="scss" scoped>
.notice-bar {
  margin-bottom: 20px;
}

.home-content {
  display: flex;
  gap: 20px;
}

.article-list {
  flex: 1;
}

.article-item {
  display: flex;
  gap: 15px;

  .article-cover {
    width: 200px;
    min-width: 200px;
    height: 140px;
    border-radius: 8px;
    overflow: hidden;

    img {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }
  }

  .article-info {
    flex: 1;

    .article-title {
      font-size: 18px;
      font-weight: 600;
      color: var(--text-primary);
      margin-bottom: 8px;
      display: block;

      &:hover {
        color: var(--primary-color);
      }

      .top-tag {
        margin-right: 6px;
      }
    }

    .article-summary {
      color: var(--text-secondary);
      font-size: 14px;
      line-height: 1.6;
      margin-bottom: 10px;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
      overflow: hidden;
    }

    .article-meta {
      display: flex;
      gap: 15px;
      color: var(--text-secondary);
      font-size: 13px;
      margin-bottom: 8px;

      span {
        display: flex;
        align-items: center;
        gap: 4px;
      }
    }

    .article-tags {
      display: flex;
      gap: 6px;
      flex-wrap: wrap;
    }
  }
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.sidebar {
  width: 300px;
  min-width: 300px;
}

.author-card {
  text-align: center;

  h3 {
    margin: 10px 0 5px;
    font-size: 18px;
  }

  p {
    color: var(--text-secondary);
    font-size: 14px;
  }

  .stats {
    display: flex;
    justify-content: space-around;
    margin-top: 15px;
    padding-top: 15px;
    border-top: 1px solid var(--border-color);

    .stat-item {
      .num {
        display: block;
        font-size: 20px;
        font-weight: bold;
        color: var(--primary-color);
      }

      .label {
        font-size: 12px;
        color: var(--text-secondary);
      }
    }
  }
}

.sidebar-title {
  font-size: 16px;
  margin-bottom: 15px;
  padding-bottom: 10px;
  border-bottom: 2px solid var(--primary-color);
}

.hot-article {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 10px;

  .rank {
    width: 24px;
    height: 24px;
    border-radius: 50%;
    background: #e0e0e0;
    color: #666;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 12px;
    font-weight: bold;

    &.top {
      background: var(--primary-color);
      color: #fff;
    }
  }

  .title {
    flex: 1;
    color: var(--text-regular);
    font-size: 14px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;

    &:hover {
      color: var(--primary-color);
    }
  }
}

.tag-cloud {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;

  .tag-item {
    cursor: pointer;
    border: none;
  }
}
</style>
