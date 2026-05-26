/**
 * 文章相关API
 */
import { get, post } from '@/utils/request'

/** 获取文章详情 */
export const getArticleDetail = (id) => get(`/front/article/${id}`)

/** 分页查询文章 */
export const listArticles = (params) => get('/front/article/list', params)

/** 获取热门文章 */
export const getHotArticles = (limit = 10) => get('/front/article/hot', { limit })

/** 获取推荐文章 */
export const getFeaturedArticles = (limit = 5) => get('/front/article/featured', { limit })

/** 获取最新文章 */
export const getLatestArticles = (limit = 5) => get('/front/article/latest', { limit })

/** 获取文章归档 */
export const getArchives = () => get('/front/article/archives')

/** 点赞文章 */
export const likeArticle = (id) => post(`/front/article/like/${id}`)
