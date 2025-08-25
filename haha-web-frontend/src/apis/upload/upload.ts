// 文件上传相关API
import http from '../../utils/http';

// ================================ 类型定义 ================================

// 文件上传响应接口
export interface UploadResponse {
  fileUrl: string;
  fileName: string;
  imageUrl?: string; // 图片上传时返回
  originalFileName?: string; // 原始文件名
  fileSize?: string; // 文件大小
  folderUuid?: string; // 文件夹UUID
}

// ================================ API接口 ================================

/**
 * 上传帖子图片
 * @param file 图片文件
 * @returns 上传结果
 */
export const uploadPostImage = async (file: File) => {
  try {
    const formData = new FormData();
    formData.append('imageFile', file);
    
    const response = await http.post<UploadResponse>('/planet/api/post/upload/image', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    });
    
    return {
      data: response.data,
      code: 'SUCCESS',
      message: '图片上传成功'
    };
  } catch (error) {
    console.error('图片上传失败:', error);
    throw error;
  }
};

/**
 * 上传帖子文件
 * @param file 文件
 * @returns 上传结果
 */
export const uploadPostFile = async (file: File) => {
  try {
    const formData = new FormData();
    formData.append('file', file);
    
    const response = await http.post<UploadResponse>('/planet/api/post/upload/file', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    });
    
    return {
      data: response.data,
      code: 'SUCCESS',
      message: '文件上传成功'
    };
  } catch (error) {
    console.error('文件上传失败:', error);
    throw error;
  }
};