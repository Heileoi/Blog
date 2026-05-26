/**
 * 用户状态管理
 * 功能：管理用户登录状态、Token、用户信息
 * 使用Pinia进行状态管理
 */
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { login as loginApi, getUserInfo as getUserInfoApi } from '@/api/auth'

export const useUserStore = defineStore('user', () => {
  // 状态
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref(JSON.parse(localStorage.getItem('userInfo') || '{}'))

  // 计算属性
  const isLoggedIn = computed(() => !!token.value)
  const isAdmin = computed(() => userInfo.value?.role === 'admin')
  const nickname = computed(() => userInfo.value?.nickname || userInfo.value?.username || '')
  const avatar = computed(() => userInfo.value?.avatar || '/default-avatar.png')

  /**
   * 登录
   * @param {Object} loginForm - { username, password }
   */
  async function login(loginForm) {
    const res = await loginApi(loginForm)
    token.value = res.data.token
    userInfo.value = {
      userId: res.data.userId,
      username: res.data.username,
      nickname: res.data.nickname,
      avatar: res.data.avatar,
      role: res.data.role
    }
    // 持久化存储
    localStorage.setItem('token', token.value)
    localStorage.setItem('userInfo', JSON.stringify(userInfo.value))
    return res
  }

  /**
   * 获取用户信息
   */
  async function fetchUserInfo() {
    const res = await getUserInfoApi()
    userInfo.value = res.data
    localStorage.setItem('userInfo', JSON.stringify(userInfo.value))
    return res
  }

  /**
   * 登出
   */
  function logout() {
    token.value = ''
    userInfo.value = {}
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
  }

  return {
    token,
    userInfo,
    isLoggedIn,
    isAdmin,
    nickname,
    avatar,
    login,
    fetchUserInfo,
    logout
  }
})
