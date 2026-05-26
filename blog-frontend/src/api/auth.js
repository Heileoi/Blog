/**
 * 认证相关API
 * 功能：用户登录、注册、获取个人信息
 */
import request from '@/utils/request'

/** 用户登录 */
export function login(data) {
  return request.post('/auth/login', data)
}

/** 用户注册 */
export function register(data) {
  return request.post('/auth/register', data)
}

/** 获取当前用户信息 */
export function getUserInfo() {
  return request.get('/admin/profile')
}

/** 修改密码 */
export function changePassword(data) {
  return request.put('/admin/profile/password', data)
}
