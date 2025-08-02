import { getAvatarPresignedUrl } from '@/apis/system/user-center'

interface CacheItem {
  url: string
  expiresAt: number
}

class AvatarUrlCache {
  private cache = new Map<string, CacheItem>()
  private readonly CACHE_BUFFER = 5 * 60 * 1000 // 提前5分钟过期

  /**
   * 获取头像的预签名URL（带缓存）
   * @param avatarKey 头像键
   * @returns 预签名URL
   */
  async getPresignedUrl(avatarKey: string): Promise<string | null> {
    if (!avatarKey) return null

    // 检查缓存
    const cached = this.cache.get(avatarKey)
    if (cached && cached.expiresAt > Date.now() + this.CACHE_BUFFER) {
      console.log(`[AvatarCache] 使用缓存: ${avatarKey}`)
      return cached.url
    }

    try {
      console.log(`[AvatarCache] 获取新URL: ${avatarKey}`)
      const response = await getAvatarPresignedUrl(avatarKey)
      const { presignedUrl, expiresIn } = response.data

      // 缓存结果
      this.cache.set(avatarKey, {
        url: presignedUrl,
        expiresAt: Date.now() + parseInt(expiresIn) * 1000
      })

      return presignedUrl
    } catch (error) {
      console.error(`[AvatarCache] 获取URL失败: ${avatarKey}`, error)
      return null
    }
  }

  /**
   * 预加载头像URL
   * @param avatarKeys 头像键数组
   */
  async preloadUrls(avatarKeys: string[]): Promise<void> {
    const promises = avatarKeys
      .filter(key => key && !this.isValidCache(key))
      .map(key => this.getPresignedUrl(key))
    
    await Promise.allSettled(promises)
  }

  /**
   * 检查缓存是否有效
   * @param avatarKey 头像键
   * @returns 是否有效
   */
  private isValidCache(avatarKey: string): boolean {
    const cached = this.cache.get(avatarKey)
    return cached ? cached.expiresAt > Date.now() + this.CACHE_BUFFER : false
  }

  /**
   * 清除过期缓存
   */
  clearExpiredCache(): void {
    const now = Date.now()
    for (const [key, item] of this.cache.entries()) {
      if (item.expiresAt <= now) {
        this.cache.delete(key)
      }
    }
  }

  /**
   * 清除所有缓存
   */
  clearAllCache(): void {
    this.cache.clear()
  }

  /**
   * 获取缓存统计信息
   */
  getCacheStats() {
    const now = Date.now()
    const total = this.cache.size
    const expired = Array.from(this.cache.values()).filter(item => item.expiresAt <= now).length
    
    return {
      total,
      valid: total - expired,
      expired
    }
  }
}

// 全局单例
export const avatarUrlCache = new AvatarUrlCache()

// 定期清理过期缓存（每10分钟）
setInterval(() => {
  avatarUrlCache.clearExpiredCache()
}, 10 * 60 * 1000)