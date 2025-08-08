// 星球相关API
import http from '../../utils/http'


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
  createTime: string | null;
}

// 分页请求类型
export interface PlanetListReq {
  categoryId: number;
  page: number;
  pageSize: number;
  sortType?: 1 | 2 | 3;
}

// 分页响应类型
export interface PageResponse<T> {
  list: T[];
  total: number;
}

// 获取星球分类列表
export const getPlanetCategories = () => {
  return http.get<PlanetCategory[]>('/planet/api/planet/categories');
};

// 根据分类获取星球列表
export const getPlanetsByCategory = (params: PlanetListReq) => {
  return http.post<PageResponse<Planet>>('/planet/api/planet/list', params);
};

// 获取热门星球
export const getHotPlanets = (limit: number = 10) => {
  return http.post<PageResponse<Planet>>('/planet/api/planet/list', {
    categoryId: 0, // 0表示所有分类
    page: 1,
    pageSize: limit,
    sortType: 1
  });
};
