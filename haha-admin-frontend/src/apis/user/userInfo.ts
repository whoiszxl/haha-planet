import http from '@/utils/http'

const BASE_URL = '/user/userInfo'

export interface UserInfoResp {
  id: string
  userCode: string
  username: string
  nickname: string
  password: string
  avatar: string
  gender: string
  birthday: string
  phone: string
  email: string
  realName: string
  idCard: string
  province: string
  city: string
  district: string
  address: string
  bio: string
  profession: string
  company: string
  education: string
  school: string
  userType: string
  level: string
  experience: string
  points: string
  balance: string
  followerCount: string
  followingCount: string
  postCount: string
  likeCount: string
  lastLoginTime: string
  lastLoginIp: string
  loginCount: string
  registerSource: string
  registerIp: string
  isVerified: string
  verifiedTime: string
  status: string
  version: string
  isDeleted: string
  createdBy: string
  updatedBy: string
  createdAt: string
  updatedAt: string
  createUserString: string
  updateUserString: string
}
export interface UserInfoDetailResp {
  id: string
  userCode: string
  username: string
  nickname: string
  password: string
  avatar: string
  gender: string
  birthday: string
  phone: string
  email: string
  realName: string
  idCard: string
  province: string
  city: string
  district: string
  address: string
  bio: string
  profession: string
  company: string
  education: string
  school: string
  userType: string
  level: string
  experience: string
  points: string
  balance: string
  followerCount: string
  followingCount: string
  postCount: string
  likeCount: string
  lastLoginTime: string
  lastLoginIp: string
  loginCount: string
  registerSource: string
  registerIp: string
  isVerified: string
  verifiedTime: string
  status: string
  version: string
  isDeleted: string
  createdBy: string
  updatedBy: string
  createdAt: string
  updatedAt: string
  createUserString: string
  updateUserString: string
}
export interface UserInfoQuery {
  userCode: string
  username: string
  version: string
  sort: Array<string>
}
export interface UserInfoPageQuery extends UserInfoQuery, PageQuery {}

/** @desc 查询用户基础信息列表 */
export function listUserInfo(query: UserInfoPageQuery) {
  return http.get<PageRes<UserInfoResp[]>>(`${BASE_URL}`, query)
}

/** @desc 查询用户基础信息详情 */
export function getUserInfo(id: string) {
  return http.get<UserInfoDetailResp>(`${BASE_URL}/${id}`)
}

/** @desc 新增用户基础信息 */
export function addUserInfo(data: any) {
  return http.post(`${BASE_URL}`, data)
}

/** @desc 修改用户基础信息 */
export function updateUserInfo(data: any, id: string) {
  return http.put(`${BASE_URL}/${id}`, data)
}

/** @desc 删除用户基础信息 */
export function deleteUserInfo(id: string) {
  return http.del(`${BASE_URL}/${id}`)
}

/** @desc 导出用户基础信息 */
export function exportUserInfo(query: UserInfoQuery) {
  return http.download(`${BASE_URL}/export`, query)
}
