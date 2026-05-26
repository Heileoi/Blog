/**
 * 分类相关API
 */
import request from '@/utils/request'

/** 创建分类 */
export function createCategory(data) {
  return request.post('/admin/category', data)
}

/** 更新分类 */
export function updateCategory(data) {
  return request.put('/admin/category', data)
}

/** 删除分类 */
export function deleteCategory(id) {
  return request.delete(`/admin/category/${id}`)
}

/** 获取分类详情 */
export function getCategoryDetail(id) {
  return request.get(`/admin/category/${id}`)
}

/** 获取分类列表（后台） */
export function listCategories() {
  return request.get('/admin/category/list')
}

/** 获取分类列表（前台，含文章数量） */
export function listFrontCategories() {
  return request.get('/front/category/list')
}
