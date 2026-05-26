/**
 * 仪表盘相关API
 */
import request from '@/utils/request'

/** 获取统计数据 */
export function getDashboardStats() {
  return request.get('/admin/dashboard/stats')
}

/** 获取文章发布趋势 */
export function getArticleTrend() {
  return request.get('/admin/dashboard/trend')
}
