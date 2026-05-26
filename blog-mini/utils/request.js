/**
 * 网络请求封装
 * 功能：
 * 1. 统一配置请求基础路径
 * 2. 自动添加请求头
 * 3. 统一处理响应和错误
 */
const BASE_URL = 'http://localhost:8080/api'

/**
 * 封装uni.request
 * @param {Object} options - 请求配置
 * @returns {Promise}
 */
export const request = (options) => {
  return new Promise((resolve, reject) => {
    // 获取token
    const token = uni.getStorageSync('token')

    uni.request({
      url: BASE_URL + options.url,
      method: options.method || 'GET',
      data: options.data || {},
      header: {
        'Content-Type': 'application/json',
        'Authorization': token ? `Bearer ${token}` : '',
        ...options.header
      },
      success: (res) => {
        if (res.statusCode === 200) {
          const data = res.data
          if (data.code === 200) {
            resolve(data)
          } else {
            uni.showToast({ title: data.message || '请求失败', icon: 'none' })
            reject(data)
          }
        } else if (res.statusCode === 401) {
          uni.removeStorageSync('token')
          uni.removeStorageSync('userInfo')
          uni.showToast({ title: '登录已过期', icon: 'none' })
          reject(res.data)
        } else {
          uni.showToast({ title: '网络异常', icon: 'none' })
          reject(res.data)
        }
      },
      fail: (err) => {
        uni.showToast({ title: '网络连接失败', icon: 'none' })
        reject(err)
      }
    })
  })
}

/**
 * GET请求
 */
export const get = (url, data) => request({ url, method: 'GET', data })

/**
 * POST请求
 */
export const post = (url, data) => request({ url, method: 'POST', data })

/**
 * PUT请求
 */
export const put = (url, data) => request({ url, method: 'PUT', data })

/**
 * DELETE请求
 */
export const del = (url, data) => request({ url, method: 'DELETE', data })
