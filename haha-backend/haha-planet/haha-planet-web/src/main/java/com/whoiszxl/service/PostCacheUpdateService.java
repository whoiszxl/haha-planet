package com.whoiszxl.service;

/**
 * 帖子缓存更新服务接口
 *
 * @author whoiszxl
 */
public interface PostCacheUpdateService {

    /**
     * 清除指定星球的帖子列表缓存
     *
     * @param planetId 星球ID
     */
    void clearPostListCache(Long planetId);

    /**
     * 更新帖子缓存版本号
     *
     * @param planetId 星球ID
     */
    void updatePostCacheVersion(Long planetId);

    /**
     * 预热指定星球的帖子列表缓存
     *
     * @param planetId 星球ID
     */
    void warmUpPostListCache(Long planetId);
}
