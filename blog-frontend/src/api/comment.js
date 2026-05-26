/**
 * 评论相关API
 */
import request from '@/utils/request'

/** 提交评论 */
export function createComment(data) {
  return request.post('/front/comment', data)
}

/** 获取文章评论列表 */
export function listCommentsByArticle(articleId, params) {
  return request.get(`/front/comment/article/${articleId}`, { params })
}

/** 获取最新评论 */
export function getLatestComments(limit = 10) {
  return request.get('/front/comment/latest', { params: { limit } })
}

/** 分页查询评论（后台） */
export function listCommentsForAdmin(params) {
  return request.get('/admin/comment/list', { params })
}

/** 审核评论 */
export function auditComment(id, status) {
  return request.put('/admin/comment/audit', null, { params: { id, status } })
}

/** 删除评论 */
export function deleteComment(id) {
  return request.delete(`/admin/comment/${id}`)
}
