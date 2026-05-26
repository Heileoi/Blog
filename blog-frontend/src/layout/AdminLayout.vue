<!--
  后台管理布局组件
  功能：后台管理页面的整体布局结构，包含侧边栏菜单、顶部栏、主内容区
-->
<template>
  <div class="admin-layout">
    <!-- 侧边栏 -->
    <aside class="admin-sidebar" :class="{ collapsed: isCollapsed }">
      <div class="sidebar-header">
        <span class="logo-text" v-if="!isCollapsed">博客管理系统</span>
        <span class="logo-text" v-else>BS</span>
      </div>
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapsed"
        router
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409eff"
      >
        <el-menu-item index="/admin/dashboard">
          <el-icon><DataBoard /></el-icon>
          <span>仪表盘</span>
        </el-menu-item>

        <el-sub-menu index="content">
          <template #title>
            <el-icon><Document /></el-icon>
            <span>内容管理</span>
          </template>
          <el-menu-item index="/admin/article">文章管理</el-menu-item>
          <el-menu-item index="/admin/category">分类管理</el-menu-item>
          <el-menu-item index="/admin/tag">标签管理</el-menu-item>
          <el-menu-item index="/admin/comment">评论管理</el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="system">
          <template #title>
            <el-icon><Setting /></el-icon>
            <span>系统管理</span>
          </template>
          <el-menu-item index="/admin/user">用户管理</el-menu-item>
          <el-menu-item index="/admin/file">文件管理</el-menu-item>
          <el-menu-item index="/admin/notice">公告管理</el-menu-item>
          <el-menu-item index="/admin/friend-link">友链管理</el-menu-item>
        </el-sub-menu>

        <el-menu-item index="/admin/profile">
          <el-icon><User /></el-icon>
          <span>个人信息</span>
        </el-menu-item>
      </el-menu>
    </aside>

    <!-- 右侧内容区 -->
    <div class="admin-content">
      <!-- 顶部栏 -->
      <header class="admin-header">
        <div class="header-left">
          <el-icon class="collapse-btn" @click="isCollapsed = !isCollapsed">
            <Fold v-if="!isCollapsed" />
            <Expand v-else />
          </el-icon>
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/admin/dashboard' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>{{ $route.meta.title }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="header-right">
          <el-tooltip content="返回前台">
            <el-icon class="action-btn" @click="$router.push('/')"><HomeFilled /></el-icon>
          </el-tooltip>
          <el-dropdown @command="handleCommand">
            <span class="user-info">
              <el-avatar :src="userStore.avatar" :size="32" />
              <span class="username">{{ userStore.nickname }}</span>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">个人信息</el-dropdown-item>
                <el-dropdown-item command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </header>

      <!-- 主内容区 -->
      <main class="admin-main">
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </main>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const isCollapsed = ref(false)

const activeMenu = computed(() => route.path)

const handleCommand = (command) => {
  if (command === 'profile') {
    router.push('/admin/profile')
  } else if (command === 'logout') {
    userStore.logout()
    router.push('/login')
  }
}
</script>

<style lang="scss" scoped>
.admin-layout {
  display: flex;
  min-height: 100vh;
}

.admin-sidebar {
  width: 220px;
  background: #304156;
  transition: width 0.3s;
  overflow: hidden;

  &.collapsed {
    width: 64px;
  }

  .sidebar-header {
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: center;
    background: #263445;

    .logo-text {
      color: #fff;
      font-size: 18px;
      font-weight: bold;
      white-space: nowrap;
    }
  }
}

.admin-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.admin-header {
  height: 60px;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;

  .header-left {
    display: flex;
    align-items: center;
    gap: 15px;

    .collapse-btn {
      font-size: 20px;
      cursor: pointer;
      color: var(--text-regular);
    }
  }

  .header-right {
    display: flex;
    align-items: center;
    gap: 20px;

    .action-btn {
      font-size: 20px;
      cursor: pointer;
      color: var(--text-regular);
    }

    .user-info {
      display: flex;
      align-items: center;
      gap: 8px;
      cursor: pointer;
    }
  }
}

.admin-main {
  flex: 1;
  padding: 20px;
  background: var(--bg-color);
  overflow-y: auto;
}
</style>
