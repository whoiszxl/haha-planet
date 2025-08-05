import http from '@/utils/http'

const BASE_URL = '/whoiszxl/planet'

export interface PlanetResp {
  id: string
  planetCode: string
  name: string
  description: string
  avatar: string
  coverImage: string
  ownerId: string
  categoryId: string
  tags: string
  priceType: string
  price: string
  originalPrice: string
  discountPrice: string
  discountStartTime: string
  discountEndTime: string
  joinType: string
  isPublic: string
  maxMembers: string
  joinQuestion: string
  autoApprove: string
  allowMemberPost: string
  postNeedApprove: string
  allowAnonymous: string
  watermarkEnabled: string
  memberCount: string
  postCount: string
  viewCount: string
  likeCount: string
  shareCount: string
  totalIncome: string
  hotScore: string
  qualityScore: string
  lastActiveTime: string
  recommendWeight: string
  isFeatured: string
  isOfficial: string
  validStartTime: string
  validEndTime: string
  closeReason: string
  extraConfig: string
  notice: string
  rules: string
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
export interface PlanetDetailResp {
  id: string
  planetCode: string
  name: string
  description: string
  avatar: string
  coverImage: string
  ownerId: string
  categoryId: string
  tags: string
  priceType: string
  price: string
  originalPrice: string
  discountPrice: string
  discountStartTime: string
  discountEndTime: string
  joinType: string
  isPublic: string
  maxMembers: string
  joinQuestion: string
  autoApprove: string
  allowMemberPost: string
  postNeedApprove: string
  allowAnonymous: string
  watermarkEnabled: string
  memberCount: string
  postCount: string
  viewCount: string
  likeCount: string
  shareCount: string
  totalIncome: string
  hotScore: string
  qualityScore: string
  lastActiveTime: string
  recommendWeight: string
  isFeatured: string
  isOfficial: string
  validStartTime: string
  validEndTime: string
  closeReason: string
  extraConfig: string
  notice: string
  rules: string
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
export interface PlanetQuery {
  planetCode: string
  name: string
  ownerId: string
  version: string
  createdBy: string
  sort: Array<string>
}
export interface PlanetPageQuery extends PlanetQuery, PageQuery {}

/** @desc 查询星球列表 */
export function listPlanet(query: PlanetPageQuery) {
  return http.get<PageRes<PlanetResp[]>>(`${BASE_URL}`, query)
}

/** @desc 查询星球详情 */
export function getPlanet(id: string) {
  return http.get<PlanetDetailResp>(`${BASE_URL}/${id}`)
}

/** @desc 新增星球 */
export function addPlanet(data: any) {
  return http.post(`${BASE_URL}`, data)
}

/** @desc 修改星球 */
export function updatePlanet(data: any, id: string) {
  return http.put(`${BASE_URL}/${id}`, data)
}

/** @desc 删除星球 */
export function deletePlanet(id: string) {
  return http.del(`${BASE_URL}/${id}`)
}

/** @desc 导出星球 */
export function exportPlanet(query: PlanetQuery) {
  return http.download(`${BASE_URL}/export`, query)
}
