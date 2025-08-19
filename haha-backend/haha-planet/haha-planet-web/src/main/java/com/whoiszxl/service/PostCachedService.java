package com.whoiszxl.service;

import com.whoiszxl.model.cache.PostListCache;

/**
 * 帖子缓存服务接口
 *
 * @author whoiszxl
 */
public interface PostCachedService {

    /**
     * 获取缓存的帖子列表
     *
     * @param planetId 星球ID
     * @param page 页码
     * @param pageSize 页大小
     * @param sortType 排序类型
     * @param version 版本号
     * @return 帖子列表缓存对象
     */
    PostListCache getCachedPostList(Long planetId, Integer page, Integer pageSize, Integer sortType, Long version);
}
