/**
 * 标签相关API
 */
import request from '@/utils/request'

/** 创建标签 */
export function createTag(data) {
  return request.post('/admin/tag', data)
}

/** 更新标签 */
export function updateTag(data) {
  return request.put('/admin/tag', data)
}

/** 删除标签 */
export function deleteTag(id) {
  return request.delete(`/admin/tag/${id}`)
}

/** 获取标签列表（后台） */
export function listTags() {
  return request.get('/admin/tag/list')
}

/** 获取标签列表（前台） */
export function listFrontTags() {
  return request.get('/front/tag/list')
}

/** 获取热门标签 */
export function getHotTags(limit = 20) {
  return request.get('/front/tag/hot', { params: { limit } })
}
