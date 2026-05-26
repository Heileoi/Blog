/**
 * 友链相关API
 */
import { get, post } from '@/utils/request'

/** 获取已审核友链 */
export const listFriendLinks = () => get('/front/friend-link/list')

/** 申请友链 */
export const applyFriendLink = (data) => post('/front/friend-link/apply', data)
