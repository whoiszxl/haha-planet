import http from '@/utils/http'

const BASE_URL = '/planet/planetSetting'

export interface PlanetSettingResp {
  id: string
  planetId: string
  settingKey: string
  settingValue: string
  settingType: string
  description: string
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
export interface PlanetSettingDetailResp {
  id: string
  planetId: string
  settingKey: string
  settingValue: string
  settingType: string
  description: string
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
export interface PlanetSettingQuery {
  planetId: string
  settingKey: string
  version: string
  createdBy: string
  sort: Array<string>
}
export interface PlanetSettingPageQuery extends PlanetSettingQuery, PageQuery {}

/** @desc 查询星球设置列表 */
export function listPlanetSetting(query: PlanetSettingPageQuery) {
  return http.get<PageRes<PlanetSettingResp[]>>(`${BASE_URL}`, query)
}

/** @desc 查询星球设置详情 */
export function getPlanetSetting(id: string) {
  return http.get<PlanetSettingDetailResp>(`${BASE_URL}/${id}`)
}

/** @desc 新增星球设置 */
export function addPlanetSetting(data: any) {
  return http.post(`${BASE_URL}`, data)
}

/** @desc 修改星球设置 */
export function updatePlanetSetting(data: any, id: string) {
  return http.put(`${BASE_URL}/${id}`, data)
}

/** @desc 删除星球设置 */
export function deletePlanetSetting(id: string) {
  return http.del(`${BASE_URL}/${id}`)
}

/** @desc 导出星球设置 */
export function exportPlanetSetting(query: PlanetSettingQuery) {
  return http.download(`${BASE_URL}/export`, query)
}
