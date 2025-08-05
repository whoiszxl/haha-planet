import http from '@/utils/http'

const BASE_URL = '/whoiszxl/planetComment'

export interface PlanetCommentResp {
  id: string
  postId: string
  planetId: string
  userId: string
  parentId: string
  replyToUserId: string
  content: string
  mediaUrls: string
  isAnonymous: string
  likeCount: string
  replyCount: string
  auditStatus: string
  version: string
  status: string
  isDeleted: string
  createdBy: string
  updatedBy: string
  createdAt: string
  updatedAt: string
  createUserString: string
  updateUserString: string
}
export interface PlanetCommentDetailResp {
  id: string
  postId: string
  planetId: string
  userId: string
  parentId: string
  replyToUserId: string
  content: string
  mediaUrls: string
  isAnonymous: string
  likeCount: string
  replyCount: string
  auditStatus: string
  version: string
  status: string
  isDeleted: string
  createdBy: string
  updatedBy: string
  createdAt: string
  updatedAt: string
  createUserString: string
  updateUserString: string
}
export interface PlanetCommentQuery {
  postId: string
  planetId: string
  userId: string
  content: string
  version: string
  createdBy: string
  sort: Array<string>
}
export interface PlanetCommentPageQuery extends PlanetCommentQuery, PageQuery {}

/** @desc 查询帖子评论列表 */
export function listPlanetComment(query: PlanetCommentPageQuery) {
  return http.get<PageRes<PlanetCommentResp[]>>(`${BASE_URL}`, query)
}

/** @desc 查询帖子评论详情 */
export function getPlanetComment(id: string) {
  return http.get<PlanetCommentDetailResp>(`${BASE_URL}/${id}`)
}

/** @desc 新增帖子评论 */
export function addPlanetComment(data: any) {
  return http.post(`${BASE_URL}`, data)
}

/** @desc 修改帖子评论 */
export function updatePlanetComment(data: any, id: string) {
  return http.put(`${BASE_URL}/${id}`, data)
}

/** @desc 删除帖子评论 */
export function deletePlanetComment(id: string) {
  return http.del(`${BASE_URL}/${id}`)
}

/** @desc 导出帖子评论 */
export function exportPlanetComment(query: PlanetCommentQuery) {
  return http.download(`${BASE_URL}/export`, query)
}

/** @desc 审核帖子评论 */
export function auditPlanetComment(id: string, data: { auditStatus: number }) {
  return http.put(`${BASE_URL}/${id}`, data)
}
