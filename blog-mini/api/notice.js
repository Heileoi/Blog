/**
 * 公告相关API
 */
import { get } from '@/utils/request'

/** 获取已发布公告 */
export const listNotices = () => get('/front/notice/list')
