/**
 * 文件上传相关API
 */
import request from '@/utils/request'

/** 上传文件 */
export function uploadFile(file) {
  const formData = new FormData()
  formData.append('file', file)
  return request.post('/admin/file/upload', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

/** 删除文件 */
export function deleteFile(id) {
  return request.delete(`/admin/file/${id}`)
}

/** 分页查询文件 */
export function listFiles(params) {
  return request.get('/admin/file/list', { params })
}
