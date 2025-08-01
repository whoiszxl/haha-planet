/** 系统用户类型 */
export interface UserResp {
  id: string
  username: string
  nickname: string
  avatar: string
  gender: number
  email: string
  mobile: string
  description: string
  status: 0 | 1 | 2
  isSystem?: boolean
  createdByString: string
  createdAt: string
  updatedByString: string
  updatedAt: string
  deptId: string
  deptName: string
  roleIds: Array<number>
  roleNames: Array<string>
  disabled: boolean
}

export type UserDetailResp = UserResp & {
  pwdResetTime?: string
}

export interface UserQuery {
  description?: string
  status?: number
  deptId?: string
  sort: Array<string>
}

export interface UserPageQuery extends UserQuery, PageQuery {
}

/** 系统角色类型 */
export interface RoleResp {
  id: string
  name: string
  code: string
  sort: number
  description: string
  dataScope: number
  isSystem: boolean
  createdByString: string
  createdAt: string
  updatedByString: string
  updatedAt: string
  disabled: boolean
}

export interface RoleDetailResp {
  id: string
  name: string
  code: string
  sort: number
  description: string
  menuIds: Array<number>
  dataScope: number
  deptIds: Array<number>
  isSystem: boolean
  createdByString: string
  createdAt: string
  updatedByString: string
  updatedAt: string
  disabled: boolean
}

export interface RoleQuery {
  description?: string
  sort: Array<string>
}

export interface RolePageQuery extends RoleQuery, PageQuery {
}

/** 系统菜单类型 */
export interface MenuResp {
  id: string
  title: string
  parentId: string
  type: 1 | 2 | 3
  path: string
  name: string
  component: string
  redirect: string
  icon: string
  isExternal: boolean
  isCache: boolean
  isHidden: boolean
  permission: string
  sort: number
  status: 1 | 2
  createdByString: string
  createdAt: string
  updatedByString: string
  updatedAt: string
  children: MenuResp[]
}

export interface MenuQuery {
  title?: string
  status?: number
  sort: Array<string>
}

/** 系统部门类型 */
export interface DeptResp {
  id: string
  name: string
  sort: number
  status: 1 | 2
  isSystem: boolean
  description: string
  createdByString: string
  createdAt: string
  updatedByString: string
  updatedAt: string
  parentId: string
  children: DeptResp[]
}

export interface DeptQuery {
  description?: string
  status?: number
  sort: Array<string>
}

/** 系统字典类型 */
export interface DictResp {
  id: string
  name: string
  code: string
  isSystem: boolean
  description: string
  createdByString: string
  createdAt: string
  updatedByString: string
  updatedAt: string
}

export interface DictQuery {
  description?: string
  sort: Array<string>
}

export type DictItemResp = {
  id: string
  label: string
  value: string
  color: string
  sort: number
  description: string
  status: 1 | 2
  dictId: number
  createdByString: string
  createdAt: string
  updatedByString: string
  updatedAt: string
}

export interface DictItemQuery {
  description?: string
  status?: number
  sort: Array<string>
  dictId: string
}

export interface DictItemPageQuery extends DictItemQuery, PageQuery {
}

/** 系统公告类型 */
export interface NoticeResp {
  id: string
  title: string
  content: string
  status: number
  type: string
  effectiveTime: string
  terminateTime: string
  createdByString: string
  createdAt: string
  updatedByString: string
  updatedAt: string
}

export interface NoticeQuery {
  title?: string
  type?: string
  sort: Array<string>
}

export interface NoticePageQuery extends NoticeQuery, PageQuery {
}

/** 系统文件类型 */
export type FileItem = {
  id: string
  name: string
  size: number
  url: string
  thumbnailSize: number
  thumbnailUrl: string
  extension: string
  type: number
  storageId: string
  createdByString: string
  createdAt: string
  updatedByString: string
  updatedAt: string
}

/** 文件资源统计信息 */
export interface FileStatisticsResp {
  type: string
  size: any
  number: number
  unit: string
  data: Array<FileStatisticsResp>
}

export interface FileQuery {
  name?: string
  type?: string
  sort: Array<string>
}

export interface FilePageQuery extends FileQuery, PageQuery {
}

/** 系统存储类型 */
export type StorageResp = {
  id: string
  name: string
  code: string
  type: number
  accessKey: string
  secretKey: string
  endpoint: string
  bucketName: string
  domain: string
  description: string
  isDefault: boolean
  sort: number
  status: number
  createdByString: string
  createdAt: string
  updatedByString: string
  updatedAt: string
}

export interface StorageQuery {
  description?: string
  status?: number
  sort: Array<string>
}

export interface StoragePageQuery extends StorageQuery, PageQuery {
}

/** 系统参数类型 */
export interface OptionResp {
  name: string
  code: string
  value: string
  description: string
}

export interface OptionQuery {
  code: Array<string>
}

/** 基础配置类型 */
export interface BasicConfig {
  SITE_FAVICON: string
  SITE_LOGO: string
  SITE_TITLE: string
  SITE_COPYRIGHT: string
}

/** 邮箱配置类型 */
export interface MailConfig {
  MAIL_SEND_TYPE: string
  MAIL_SMTP_SERVER: string
  MAIL_SMTP_PORT: string
  MAIL_SMTP_USERNAME: string
  MAIL_SMTP_PASSWORD: string
  MAIL_SMTP_VERIFY_TYPE: string
  MAIL_FROM: string
}

/** 安全配置类型 */
export interface SecurityConfig {
  PASSWORD_ERROR_LOCK_COUNT: OptionResp
  PASSWORD_ERROR_LOCK_MINUTES: OptionResp
  PASSWORD_EXPIRATION_DAYS: OptionResp
  PASSWORD_EXPIRATION_WARNING_DAYS: OptionResp
  PASSWORD_REUSE_POLICY: OptionResp
  PASSWORD_MIN_LENGTH: OptionResp
  PASSWORD_ALLOW_CONTAIN_USERNAME: OptionResp
  PASSWORD_CONTAIN_SPECIAL_CHARACTERS: OptionResp
}

/** 绑定三方账号信息 */
export interface BindSocialAccountRes {
  source: string
  description: string
}

/** 系统消息类型 */
export interface MessageResp {
  id: string
  title: string
  content: string
  type: number
  isRead: boolean
  readTime: string
  createdByString: string
  createdAt: string
}

export interface MessageQuery {
  title?: string
  type?: number
  isRead?: boolean
  sort: Array<string>
}

export interface MessagePageQuery extends MessageQuery, PageQuery {
}
