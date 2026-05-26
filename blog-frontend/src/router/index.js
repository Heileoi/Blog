/**
 * 路由配置
 * 功能：定义前台和后台的所有路由规则
 * 包含路由守卫，控制登录和权限验证
 */
import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'

const routes = [
  // ========== 前台路由 ==========
  {
    path: '/',
    component: () => import('@/layout/FrontLayout.vue'),
    children: [
      { path: '', name: 'Home', component: () => import('@/views/front/Home.vue'), meta: { title: '首页' } },
      { path: 'article/:id', name: 'ArticleDetail', component: () => import('@/views/front/ArticleDetail.vue'), meta: { title: '文章详情' } },
      { path: 'category', name: 'Category', component: () => import('@/views/front/Category.vue'), meta: { title: '分类' } },
      { path: 'tag', name: 'Tag', component: () => import('@/views/front/Tag.vue'), meta: { title: '标签' } },
      { path: 'archive', name: 'Archive', component: () => import('@/views/front/Archive.vue'), meta: { title: '归档' } },
      { path: 'about', name: 'About', component: () => import('@/views/front/About.vue'), meta: { title: '关于' } },
      { path: 'friend-link', name: 'FriendLink', component: () => import('@/views/front/FriendLink.vue'), meta: { title: '友链' } }
    ]
  },

  // 登录页
  { path: '/login', name: 'Login', component: () => import('@/views/Login.vue'), meta: { title: '登录' } },

  // ========== 后台路由 ==========
  {
    path: '/admin',
    component: () => import('@/layout/AdminLayout.vue'),
    meta: { requireAuth: true, requireAdmin: true },
    redirect: '/admin/dashboard',
    children: [
      { path: 'dashboard', name: 'Dashboard', component: () => import('@/views/admin/Dashboard.vue'), meta: { title: '仪表盘' } },
      { path: 'article', name: 'AdminArticle', component: () => import('@/views/admin/ArticleList.vue'), meta: { title: '文章管理' } },
      { path: 'article/edit/:id?', name: 'ArticleEdit', component: () => import('@/views/admin/ArticleEdit.vue'), meta: { title: '编辑文章' } },
      { path: 'category', name: 'AdminCategory', component: () => import('@/views/admin/CategoryList.vue'), meta: { title: '分类管理' } },
      { path: 'tag', name: 'AdminTag', component: () => import('@/views/admin/TagList.vue'), meta: { title: '标签管理' } },
      { path: 'comment', name: 'AdminComment', component: () => import('@/views/admin/CommentList.vue'), meta: { title: '评论管理' } },
      { path: 'file', name: 'AdminFile', component: () => import('@/views/admin/FileList.vue'), meta: { title: '文件管理' } },
      { path: 'user', name: 'AdminUser', component: () => import('@/views/admin/UserList.vue'), meta: { title: '用户管理' } },
      { path: 'notice', name: 'AdminNotice', component: () => import('@/views/admin/NoticeList.vue'), meta: { title: '公告管理' } },
      { path: 'friend-link', name: 'AdminFriendLink', component: () => import('@/views/admin/FriendLinkList.vue'), meta: { title: '友链管理' } },
      { path: 'profile', name: 'AdminProfile', component: () => import('@/views/admin/Profile.vue'), meta: { title: '个人信息' } }
    ]
  },

  // 404页面
  { path: '/:pathMatch(.*)*', name: 'NotFound', component: () => import('@/views/404.vue') }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

/**
 * 路由前置守卫
 * 功能：检查登录状态和管理员权限
 */
router.beforeEach((to, from, next) => {
  // 设置页面标题
  document.title = to.meta.title ? `${to.meta.title} - 熙磊的个人博客` : '熙磊的个人博客'

  const userStore = useUserStore()

  // 需要登录的页面
  if (to.meta.requireAuth && !userStore.isLoggedIn) {
    next({ name: 'Login', query: { redirect: to.fullPath } })
    return
  }

  // 需要管理员权限的页面
  if (to.meta.requireAdmin && !userStore.isAdmin) {
    next({ name: 'Home' })
    return
  }

  next()
})

export default router
