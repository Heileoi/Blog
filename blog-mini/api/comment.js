/**
 * 评论相关API
 */
import { get, post } from '@/utils/request'

/** 获取文章评论 */
export const listComments = (articleId, params) => get(`/front/comment/article/${articleId}`, params)

/** 提交评论 */
export const createComment = (data) => post('/front/comment', data)
