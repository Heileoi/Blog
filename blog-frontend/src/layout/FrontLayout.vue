<!--
  前台布局组件
  功能：前台页面的整体布局结构，包含头部导航、主内容区、底部版权信息
-->
<template>
  <div class="front-layout">
    <!-- 顶部导航栏 -->
    <header class="front-header">
      <div class="container header-content">
        <!-- Logo和博客名称 -->
        <router-link to="/" class="logo">
          <span class="logo-text">熙磊的博客</span>
        </router-link>

        <!-- 导航菜单 -->
        <nav class="nav-menu">
          <router-link to="/" class="nav-item">首页</router-link>
          <router-link to="/category" class="nav-item">分类</router-link>
          <router-link to="/tag" class="nav-item">标签</router-link>
          <router-link to="/archive" class="nav-item">归档</router-link>
          <router-link to="/friend-link" class="nav-item">友链</router-link>
          <router-link to="/about" class="nav-item">关于</router-link>
        </nav>

        <!-- 右侧操作区 -->
        <div class="header-right">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索文章..."
            class="search-input"
            @keyup.enter="handleSearch"
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>

          <template v-if="userStore.isLoggedIn">
            <el-dropdown @command="handleCommand">
              <span class="user-info">
                <el-avatar :src="userStore.avatar" :size="32" />
                <span class="username">{{ userStore.nickname }}</span>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="admin" v-if="userStore.isAdmin">后台管理</el-dropdown-item>
                  <el-dropdown-item command="profile">个人信息</el-dropdown-item>
                  <el-dropdown-item command="logout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
          <template v-else>
            <router-link to="/login">
              <el-button type="primary" size="small">登录</el-button>
            </router-link>
          </template>
        </div>
      </div>
    </header>

    <!-- 主内容区 -->
    <main class="front-main">
      <div class="container">
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </div>
    </main>

    <!-- 底部版权信息 -->
    <footer class="front-footer">
      <div class="container">
        <p>&copy; {{ new Date().getFullYear() }} 王熙磊. All Rights Reserved.</p>
        <p>Powered by Spring Boot & Vue 3</p>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()
const searchKeyword = ref('')

/** 搜索文章 */
const handleSearch = () => {
  if (searchKeyword.value.trim()) {
    router.push({ path: '/', query: { keyword: searchKeyword.value } })
  }
}

/** 处理下拉菜单命令 */
const handleCommand = (command) => {
  switch (command) {
    case 'admin':
      router.push('/admin/dashboard')
      break
    case 'profile':
      router.push('/admin/profile')
      break
    case 'logout':
      userStore.logout()
      router.push('/')
      break
  }
}
</script>

<style lang="scss" scoped>
.front-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.front-header {
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 100;

  .header-content {
    display: flex;
    align-items: center;
    height: 60px;
    gap: 30px;
  }

  .logo {
    .logo-text {
      font-size: 20px;
      font-weight: bold;
      color: var(--primary-color);
    }
  }

  .nav-menu {
    display: flex;
    gap: 20px;

    .nav-item {
      color: var(--text-regular);
      font-size: 15px;
      padding: 4px 0;
      border-bottom: 2px solid transparent;
      transition: all 0.3s;

      &:hover, &.router-link-active {
        color: var(--primary-color);
        border-bottom-color: var(--primary-color);
      }
    }
  }

  .header-right {
    margin-left: auto;
    display: flex;
    align-items: center;
    gap: 15px;

    .search-input {
      width: 200px;
    }

    .user-info {
      display: flex;
      align-items: center;
      gap: 8px;
      cursor: pointer;

      .username {
        color: var(--text-regular);
      }
    }
  }
}

.front-main {
  flex: 1;
  padding: 20px 0;
}

.front-footer {
  background: #2c3e50;
  color: #ecf0f1;
  text-align: center;
  padding: 20px 0;
  margin-top: 40px;

  p {
    margin: 5px 0;
    font-size: 13px;
  }
}
</style>
