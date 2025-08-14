// 星球相关API
import http from '../../utils/http'

// ================================ 版本号管理 ================================

// 本地存储键名
const STORAGE_KEYS = {
  PLANET_CATEGORIES_VERSION: 'planet_categories_version',
  PLANET_LIST_VERSION: 'planet_list_version'
};

// 版本号管理工具
class VersionManager {
  // 获取本地存储的版本号
  static getVersion(key: string): number | null {
    const version = localStorage.getItem(key);
    return version ? parseInt(version, 10) : null;
  }

  // 保存版本号到本地存储
  static setVersion(key: string, version: number): void {
    localStorage.setItem(key, version.toString());
  }

  // 清除版本号
  static clearVersion(key: string): void {
    localStorage.removeItem(key);
  }

  // 获取分类列表版本号
  static getCategoriesVersion(): number | null {
    return this.getVersion(STORAGE_KEYS.PLANET_CATEGORIES_VERSION);
  }

  // 保存分类列表版本号
  static setCategoriesVersion(version: number): void {
    this.setVersion(STORAGE_KEYS.PLANET_CATEGORIES_VERSION, version);
  }

  // 获取星球列表版本号
  static getPlanetListVersion(): number | null {
    return this.getVersion(STORAGE_KEYS.PLANET_LIST_VERSION);
  }

  // 保存星球列表版本号
  static setPlanetListVersion(version: number): void {
    this.setVersion(STORAGE_KEYS.PLANET_LIST_VERSION, version);
  }
}

// ================================ 类型定义 ================================

// 星球分类响应类型
export interface PlanetCategory {
  id: number;
  categoryName: string;
  iconUrl: string;
  parentId: number;
  level: boolean;
  sort: number;
  status: number;
}

// 星球成员用户信息类型
export interface PlanetMemberUser {
  userId: number;
  userCode: string;
  username: string;
  nickname: string;
  avatar: string;
  memberType: number;
  memberTypeName: string;
  planetNickname: string;
}

// 星球响应类型
export interface Planet {
  id: number;
  planetCode: string;
  name: string;
  description: string;
  avatar: string;
  coverImage: string;
  ownerId: number;
  categoryId: number;
  categoryName: string;
  tags: string;
  priceType: number;
  price: number;
  originalPrice: number;
  discountPrice: number;
  joinType: number;
  isPublic: number;
  maxMembers: number;
  memberCount: number;
  postCount: number;
  viewCount: number;
  likeCount: number;
  hotScore: number;
  qualityScore: number;
  lastActiveTime: string;
  recommendWeight: number;
  isFeatured: number;
  isOfficial: number;
  status: number;
  createdAt: string | null;
  adminUsers?: PlanetMemberUser[]; // 管理员和星球主信息
}

// 分页请求类型
export interface PlanetListReq {
  categoryId?: number; // 可选参数，全部分类时不传
  page: number;
  pageSize: number;
  sortType?: 1 | 2 | 3;
}

// 分页响应类型
export interface PageResponse<T> {
  list: T[];
  total: number;
}

// 带版本号的响应类型
export interface VersionedResponse<T> {
  data: T;
  version: number;
  exist: boolean;
  later?: boolean;
}

// 分类列表缓存响应
export interface CategoryListCacheResponse {
  categoryList: PlanetCategory[];
  total: number;
  version: number;
  exist: boolean;
  later?: boolean;
}

// 星球列表缓存响应
export interface PlanetListCacheResponse {
  planetList: Planet[];
  total: number;
  version: number;
  categoryId: number;
  page: number;
  pageSize: number;
  sortType: number;
  exist: boolean;
  later?: boolean;
}

// ================================ API接口 ================================

// 获取星球分类列表（支持版本号）
export const getPlanetCategories = async () => {
  try {
    // 获取本地存储的版本号
    const localVersion = VersionManager.getCategoriesVersion();
    
    // 构建请求URL，版本号作为路径参数
    const url = localVersion 
      ? `/planet/api/planet/categories/${localVersion}`
      : `/planet/api/planet/categories/0`; // 首次请求使用0作为默认版本号
    
    // 发送请求，注意响应结构是 { data: VersionedResponse }
    const response = await http.get<VersionedResponse<PlanetCategory[]>>(url);
    
    // 如果请求成功且有新版本号，保存到本地
    if (response.data && response.data.version) {
      VersionManager.setCategoriesVersion(response.data.version);
    }
    
    // 如果缓存不存在或需要稍后重试，返回空数组
    if (!response.data.exist || response.data.later) {
      console.warn('分类列表缓存未就绪:', response.data.later ? '稍后重试' : '数据不存在');
      return {
        data: [],
        code: response.data.later ? 'CACHE_NOT_READY' : 'NO_DATA',
        message: response.data.later ? '缓存正在构建中，请稍后重试' : '暂无分类数据'
      };
    }
    
    return {
      data: response.data.data || [],
      code: 'SUCCESS',
      message: '获取成功'
    };
  } catch (error) {
    console.error('获取星球分类列表失败:', error);
    throw error;
  }
};

// 根据分类获取星球列表（支持版本号）
export const getPlanetsByCategory = async (params: PlanetListReq) => {
  try {
    // 获取本地存储的版本号
    const localVersion = VersionManager.getPlanetListVersion();
    
    // 构建请求URL，版本号作为路径参数
    const url = localVersion 
      ? `/planet/api/planet/list/${localVersion}`
      : `/planet/api/planet/list/0`; // 首次请求使用0作为默认版本号
    
    // 发送请求，注意响应结构是 { data: VersionedResponse }
    const response = await http.post<VersionedResponse<PageResponse<Planet>>>(url, params);
    
    // 如果请求成功且有新版本号，保存到本地
    if (response.data && response.data.version) {
      VersionManager.setPlanetListVersion(response.data.version);
    }
    
    // 如果缓存不存在或需要稍后重试，返回空数据
    if (!response.data.exist || response.data.later) {
      console.warn('星球列表缓存未就绪:', response.data.later ? '稍后重试' : '数据不存在');
      return {
        data: {
          list: [],
          total: 0
        },
        code: response.data.later ? 'CACHE_NOT_READY' : 'NO_DATA',
        message: response.data.later ? '缓存正在构建中，请稍后重试' : '暂无星球数据'
      };
    }
    
    return {
      data: response.data.data || { list: [], total: 0 },
      code: 'SUCCESS',
      message: '获取成功'
    };
  } catch (error) {
    console.error('获取星球列表失败:', error);
    throw error;
  }
};

// 获取热门星球（支持版本号）
export const getHotPlanets = async (limit: number = 10) => {
  return await getPlanetsByCategory({
    categoryId: 0, // 0表示所有分类
    page: 1,
    pageSize: limit,
    sortType: 2 // 2表示按热度排序
  });
};

// 获取星球详情（支持版本号）
export const getPlanetDetail = async (planetId: number) => {
  try {
    // 获取本地存储的版本号（可以为星球详情单独管理版本号）
    const localVersion = localStorage.getItem(`planet_detail_version_${planetId}`);
    const version = localVersion ? parseInt(localVersion, 10) : 0;
    
    // 构建请求URL，版本号作为路径参数
    const url = `/planet/api/planet/detail/${planetId}/${version}`;
    
    // 发送请求
    const response = await http.get<VersionedResponse<Planet>>(url);
    
    // 如果请求成功且有新版本号，保存到本地
    if (response.data && response.data.version) {
      localStorage.setItem(`planet_detail_version_${planetId}`, response.data.version.toString());
    }
    
    // 如果缓存不存在或需要稍后重试
    if (!response.data.exist || response.data.later) {
      console.warn('星球详情缓存未就绪:', response.data.later ? '稍后重试' : '数据不存在');
      return {
        data: null,
        code: response.data.later ? 'CACHE_NOT_READY' : 'NO_DATA',
        message: response.data.later ? '缓存正在构建中，请稍后重试' : '星球不存在或已被删除'
      };
    }
    
    return {
      data: response.data.data,
      code: 'SUCCESS',
      message: '获取成功'
    };
  } catch (error) {
    console.error('获取星球详情失败:', error);
    throw error;
  }
};

// ================================ 工具函数 ================================

// 清除所有版本号缓存（用于登出或重置）
export const clearAllVersions = () => {
  VersionManager.clearVersion(STORAGE_KEYS.PLANET_CATEGORIES_VERSION);
  VersionManager.clearVersion(STORAGE_KEYS.PLANET_LIST_VERSION);
  console.log('已清除所有版本号缓存');
};

// 获取当前所有版本号信息（用于调试）
export const getVersionInfo = () => {
  return {
    categoriesVersion: VersionManager.getCategoriesVersion(),
    planetListVersion: VersionManager.getPlanetListVersion()
  };
};

// 导出版本管理器（供其他模块使用）
export { VersionManager };
