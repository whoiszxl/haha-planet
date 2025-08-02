import Unknown from '../assets/images/avatar/unknown.png'
import Male from '../assets/images/avatar/male.png'
import Female from '../assets/images/avatar/female.png'

// 获取图片基础URL
const getImageBaseUrl = () => {
  return import.meta.env.VITE_IMAGE_BASE_URL || ''
}

// 判断是否为完整URL
const isFullUrl = (url: string) => {
  return url.startsWith('http://') || url.startsWith('https://') || url.startsWith('data:')
}

export default function getAvatar(avatar: string | undefined, gender: number | undefined) {
  // 检查头像数据是否有效
  if (avatar && avatar.trim() && !avatar.includes('undefined')) {
    // 如果是 base64 格式，确保数据完整
    if (avatar.startsWith('data:image/') && avatar.includes('base64,')) {
      const base64Data = avatar.split('base64,')[1]
      if (base64Data && base64Data !== 'undefined' && base64Data.length > 0) {
        return avatar
      }
    } else if (!avatar.startsWith('data:image/')) {
      // 如果是完整URL，直接返回
      if (isFullUrl(avatar)) {
        return avatar
      }
      // 如果是相对路径（key），拼接基础URL
      const baseUrl = getImageBaseUrl()
      if (baseUrl) {
        // 确保URL拼接正确，避免双斜杠
        const cleanBaseUrl = baseUrl.endsWith('/') ? baseUrl.slice(0, -1) : baseUrl
        const cleanAvatar = avatar.startsWith('/') ? avatar.slice(1) : avatar
        return `${cleanBaseUrl}/${cleanAvatar}`
      }
      // 如果没有配置基础URL，直接返回key（可能用于开发环境）
      return avatar
    }
  }
  
  // 根据性别返回默认头像
  if (gender === 1) {
    return Male
  }
  if (gender === 2) {
    return Female
  }
  return Unknown
}

// 导出一个通用的图片URL拼接函数
export const getImageUrl = (imageKey: string | undefined): string => {
  if (!imageKey || imageKey.trim() === '' || imageKey.includes('undefined')) {
    return ''
  }
  
  // 如果已经是完整URL，直接返回
  if (isFullUrl(imageKey)) {
    return imageKey
  }
  
  // 拼接基础URL
  const baseUrl = getImageBaseUrl()
  if (baseUrl) {
    const cleanBaseUrl = baseUrl.endsWith('/') ? baseUrl.slice(0, -1) : baseUrl
    const cleanImageKey = imageKey.startsWith('/') ? imageKey.slice(1) : imageKey
    return `${cleanBaseUrl}/${cleanImageKey}`
  }
  
  return imageKey
}
