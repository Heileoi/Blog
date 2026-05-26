/**
 * 文章相关API
 * 功能：文章的增删改查接口调用
 */
import request from '@/utils/request'

// ========== 后台管理接口 ==========

/** 创建文章 */
export function createArticle(data) {
  return request.post('/admin/article', data)
}

/** 更新文章 */
export function updateArticle(data) {
  return request.put('/admin/article', data)
}

/** 删除文章 */
export function deleteArticle(id) {
  return request.delete(`/admin/article/${id}`)
}

/** 获取文章详情（后台） */
export function getArticleDetail(id) {
  return request.get(`/admin/article/${id}`)
}

/** 分页查询文章（后台） */
export function listArticlesForAdmin(params) {
  return request.get('/admin/article/list', { params })
}

/** 更新文章状态 */
export function updateArticleStatus(id, status) {
  return request.put('/admin/article/status', null, { params: { id, status } })
}

/** 更新文章置顶 */
export function updateArticleTop(id, isTop) {
  return request.put('/admin/article/top', null, { params: { id, isTop } })
}

// ========== 前台接口 ==========

/** 获取文章详情（前台，增加浏览量） */
export function getFrontArticleDetail(id) {
  return request.get(`/front/article/${id}`)
}

/** 分页查询已发布文章 */
export function listPublishedArticles(params) {
  return request.get('/front/article/list', { params })
}

/** 获取热门文章 */
export function getHotArticles(limit = 10) {
  return request.get('/front/article/hot', { params: { limit } })
}

/** 获取推荐文章 */
export function getFeaturedArticles(limit = 5) {
  return request.get('/front/article/featured', { params: { limit } })
}

/** 获取最新文章 */
export function getLatestArticles(limit = 5) {
  return request.get('/front/article/latest', { params: { limit } })
}

/** 获取文章归档 */
export function getArchives() {
  return request.get('/front/article/archives')
}

/** 点赞文章 */
export function likeArticle(id) {
  return request.post(`/front/article/like/${id}`)
}
