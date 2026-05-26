/**
 * 分类相关API
 */
import { get } from '@/utils/request'

/** 获取分类列表 */
export const listCategories = () => get('/front/category/list')
