/**
 * 标签相关API
 */
import { get } from '@/utils/request'

/** 获取热门标签 */
export const getHotTags = (limit = 20) => get('/front/tag/hot', { limit })
