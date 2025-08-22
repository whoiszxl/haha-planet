// 画廊相关API
import http from '../../utils/http';

// ================================ 类型定义 ================================

// 画廊响应接口
export interface GalleryItem {
  id: number;
  userId: number;
  fileName: string;
  fileUrl: string;
  fileSize: number;
  fileType: string;
  width: number;
  height: number;
  thumbnailUrl: string;
  description: string;
  category: string;
  year: number;
  author: string;
  sortOrder: number;
  createdAt: string;
  updatedAt: string;
}

// 带版本号的响应类型
export interface VersionedResponse<T> {
  data: T;
  version: number;
  exist: boolean;
  later?: boolean;
}

// 画廊列表响应接口
export interface GalleryListResponse {
  [category: string]: GalleryItem[];
}

// 带版本号的画廊列表响应接口
export interface VersionedGalleryListResponse extends VersionedResponse<GalleryListResponse> {}

// 画廊列表请求参数
export interface GalleryListParams {
  version?: number;
}

// ================================ API接口 ================================

/**
 * 获取画廊列表
 */
export const getGalleryList = async (params?: GalleryListParams) => {
  try {
    const response = await http.get<VersionedGalleryListResponse>('/planet/api/post/gallery', {
      params
    });
    
    // 如果缓存不存在或需要稍后重试
    if (!response.data.exist || response.data.later) {
      console.warn('画廊列表缓存未就绪:', response.data.later ? '稍后重试' : '数据不存在');
      return {
        data: null,
        code: response.data.later ? 'CACHE_NOT_READY' : 'NO_DATA',
        message: response.data.later ? '缓存正在构建中，请稍后重试' : '画廊数据不存在'
      };
    }
    
    return {
      data: response.data,
      code: 'SUCCESS',
      message: '获取成功'
    };
  } catch (error) {
    console.error('获取画廊列表失败:', error);
    throw error;
  }
};