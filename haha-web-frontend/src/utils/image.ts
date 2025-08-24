// 图片处理工具类

/**
 * 获取图片基础URL
 * @returns 图片基础URL
 */
export const getImageBaseUrl = (): string => {
  return process.env.REACT_APP_IMAGE_BASE_URL || '';
};

/**
 * 处理图片URL，如果是公共图片则拼接基础URL
 * @param imagePath 图片路径
 * @param isPublic 是否为公共图片，默认为true
 * @returns 完整的图片URL
 */
export const getImageUrl = (imagePath: string, isPublic: boolean = true): string => {
  // 如果路径为空，返回空字符串
  if (!imagePath) {
    return '';
  }

  // 如果已经是完整的URL（包含http或https），直接返回
  if (imagePath.startsWith('http://') || imagePath.startsWith('https://')) {
    return imagePath;
  }

  // 如果是公共图片，拼接基础URL
  if (isPublic) {
    const baseUrl = getImageBaseUrl();
    // 确保baseUrl以/结尾，imagePath不以/开头，避免重复斜杠
    const cleanBaseUrl = baseUrl.endsWith('/') ? baseUrl : `${baseUrl}/`;
    const cleanImagePath = imagePath.startsWith('/') ? imagePath.substring(1) : imagePath;
    return `${cleanBaseUrl}${cleanImagePath}`;
  }

  // 如果不是公共图片，直接返回原路径
  return imagePath;
};

/**
 * 获取用户头像URL
 * @param avatarPath 头像路径
 * @returns 完整的头像URL
 */
export const getAvatarUrl = (avatarPath: string): string => {
  return getImageUrl(avatarPath, true);
};

/**
 * 获取封面图片URL
 * @param coverPath 封面图片路径
 * @returns 完整的封面图片URL
 */
export const getCoverUrl = (coverPath: string): string => {
  return getImageUrl(coverPath, true);
};

/**
 * 获取内容图片URL（帖子中的图片）
 * @param imagePath 图片路径
 * @returns 完整的图片URL
 */
export const getContentImageUrl = (imagePath: string): string => {
  return getImageUrl(imagePath, true);
};

/**
 * 处理多个图片URL
 * @param imagePaths 图片路径数组
 * @param isPublic 是否为公共图片，默认为true
 * @returns 完整的图片URL数组
 */
export const getImageUrls = (imagePaths: string[], isPublic: boolean = true): string[] => {
  return imagePaths.map(path => getImageUrl(path, isPublic));
};

/**
 * 获取默认头像URL
 * @returns 默认头像URL
 */
export const getDefaultAvatarUrl = (): string => {
  return getImageUrl('default/avatar.png', true);
};

/**
 * 获取默认封面图片URL
 * @returns 默认封面图片URL
 */
export const getDefaultCoverUrl = (): string => {
  return getImageUrl('default/cover.jpg', true);
};

/**
 * 从上传响应中提取文件名
 * @param uploadResponse 上传接口的响应数据
 * @returns 文件名，如果提取失败则返回默认名称
 */
export const getFileNameFromUploadResponse = (uploadResponse: any): string => {
  try {
    if (uploadResponse && uploadResponse.data && uploadResponse.data.fileName) {
      return uploadResponse.data.fileName;
    }
    // 如果有imageUrl，尝试从URL中提取文件名
    if (uploadResponse && uploadResponse.data && uploadResponse.data.imageUrl) {
      const url = uploadResponse.data.imageUrl;
      const urlParts = url.split('/');
      return urlParts[urlParts.length - 1] || '未知文件';
    }
    return '未知文件';
  } catch (error) {
    console.error('提取文件名失败:', error);
    return '未知文件';
  }
};