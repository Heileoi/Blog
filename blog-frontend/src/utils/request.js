/**
 * Axios请求封装
 * 功能：
 * 1. 统一配置请求基础路径和超时时间
 * 2. 请求拦截器：自动添加JWT Token
 * 3. 响应拦截器：统一处理错误和Token过期
 */
import axios from 'axios'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import router from '@/router'

// 创建axios实例
const service = axios.create({
  baseURL: '/api',
  timeout: 15000
})

// 请求拦截器
service.interceptors.request.use(
  (config) => {
    const userStore = useUserStore()
    // 如果有token，自动添加到请求头
    if (userStore.token) {
      config.headers['Authorization'] = `Bearer ${userStore.token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  (response) => {
    const res = response.data
    // 业务成功
    if (res.code === 200) {
      return res
    }
    // 未登录或Token过期
    if (res.code === 401) {
      ElMessage.error('登录已过期，请重新登录')
      const userStore = useUserStore()
      userStore.logout()
      router.push('/login')
      return Promise.reject(new Error(res.message))
    }
    // 权限不足
    if (res.code === 403) {
      ElMessage.error('没有操作权限')
      return Promise.reject(new Error(res.message))
    }
    // 其他业务错误
    ElMessage.error(res.message || '请求失败')
    return Promise.reject(new Error(res.message))
  },
  (error) => {
    // 网络错误
    if (error.message.includes('timeout')) {
      ElMessage.error('请求超时，请稍后重试')
    } else if (error.message.includes('Network Error')) {
      ElMessage.error('网络连接异常')
    } else {
      ElMessage.error(error.message || '请求失败')
    }
    return Promise.reject(error)
  }
)

export default service
