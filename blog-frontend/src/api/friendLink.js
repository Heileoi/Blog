/**
 * 友情链接相关API
 */
import request from '@/utils/request'

/** 获取所有友链（后台） */
export function listAllFriendLinks() {
  return request.get('/admin/friend-link/list')
}

/** 审核友链 */
export function auditFriendLink(id, status) {
  return request.put('/admin/friend-link/audit', null, { params: { id, status } })
}

/** 更新友链 */
export function updateFriendLink(data) {
  return request.put('/admin/friend-link', data)
}

/** 删除友链 */
export function deleteFriendLink(id) {
  return request.delete(`/admin/friend-link/${id}`)
}

/** 获取已审核友链（前台） */
export function listApprovedFriendLinks() {
  return request.get('/front/friend-link/list')
}

/** 申请友链 */
export function applyFriendLink(data) {
  return request.post('/front/friend-link/apply', data)
}
