/**
 * Pinia状态管理
 * 功能：管理用户状态和全局数据
 */
import { createStore as createPinia } from 'pinia'
import { defineStore } from 'pinia'

export function createStore() {
  return createPinia()
}

/**
 * 用户状态Store
 */
export const useUserStore = defineStore('user', {
  state: () => ({
    token: uni.getStorageSync('token') || '',
    userInfo: JSON.parse(uni.getStorageSync('userInfo') || '{}')
  }),

  getters: {
    isLoggedIn: (state) => !!state.token,
    nickname: (state) => state.userInfo?.nickname || state.userInfo?.username || ''
  },

  actions: {
    setToken(token) {
      this.token = token
      uni.setStorageSync('token', token)
    },

    setUserInfo(info) {
      this.userInfo = info
      uni.setStorageSync('userInfo', JSON.stringify(info))
    },

    logout() {
      this.token = ''
      this.userInfo = {}
      uni.removeStorageSync('token')
      uni.removeStorageSync('userInfo')
    }
  }
})
