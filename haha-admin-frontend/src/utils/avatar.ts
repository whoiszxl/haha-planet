import Unknown from '../assets/images/avatar/unknown.png'
import Male from '../assets/images/avatar/male.png'
import Female from '../assets/images/avatar/female.png'

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
      // 如果是普通 URL，直接返回
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
