import http from '@/utils/http'

const BASE_URL = '/planet/planetPost'

export interface PlanetPostResp {
  id: string
  planetId: string
  userId: string
  title: string
  content: string
  contentType: string
  mediaUrls: string
  isAnonymous: string
  isTop: string
  isEssence: string
  viewCount: string
  likeCount: string
  commentCount: string
  shareCount: string
  collectCount: string
  auditStatus: string
  auditReason: string
  auditTime: string
  auditBy: string
  lastCommentTime: string
  hotScore: string
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
export interface PlanetPostDetailResp {
  id: string
  planetId: string
  userId: string
  title: string
  content: string
  contentType: string
  mediaUrls: string
  isAnonymous: string
  isTop: string
  isEssence: string
  viewCount: string
  likeCount: string
  commentCount: string
  shareCount: string
  collectCount: string
  auditStatus: string
  auditReason: string
  auditTime: string
  auditBy: string
  lastCommentTime: string
  hotScore: string
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
export interface PlanetPostQuery {
  planetId: string
  userId: string
  content: string
  version: string
  createdBy: string
  sort: Array<string>
}
export interface PlanetPostPageQuery extends PlanetPostQuery, PageQuery {}

/** @desc 查询星球帖子列表 */
export function listPlanetPost(query: PlanetPostPageQuery) {
  return http.get<PageRes<PlanetPostResp[]>>(`${BASE_URL}`, query)
}

/** @desc 查询星球帖子详情 */
export function getPlanetPost(id: string) {
  return http.get<PlanetPostDetailResp>(`${BASE_URL}/${id}`)
}

/** @desc 新增星球帖子 */
export function addPlanetPost(data: any) {
  return http.post(`${BASE_URL}`, data)
}

/** @desc 修改星球帖子 */
export function updatePlanetPost(data: any, id: string) {
  return http.put(`${BASE_URL}/${id}`, data)
}

/** @desc 删除星球帖子 */
export function deletePlanetPost(id: string) {
  return http.del(`${BASE_URL}/${id}`)
}

/** @desc 导出星球帖子 */
export function exportPlanetPost(query: PlanetPostQuery) {
  return http.download(`${BASE_URL}/export`, query)
}
