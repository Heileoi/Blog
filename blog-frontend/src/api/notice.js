/**
 * 公告相关API
 */
import request from '@/utils/request'

/** 创建公告 */
export function createNotice(data) {
  return request.post('/admin/notice', data)
}

/** 更新公告 */
export function updateNotice(data) {
  return request.put('/admin/notice', data)
}

/** 删除公告 */
export function deleteNotice(id) {
  return request.delete(`/admin/notice/${id}`)
}

/** 获取所有公告（后台） */
export function listAllNotices() {
  return request.get('/admin/notice/list')
}

/** 获取已发布公告（前台） */
export function listPublishedNotices() {
  return request.get('/front/notice/list')
}
