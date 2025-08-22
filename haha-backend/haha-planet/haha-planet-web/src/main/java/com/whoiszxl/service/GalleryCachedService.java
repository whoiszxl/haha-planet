package com.whoiszxl.service;

import com.whoiszxl.model.cache.GalleryListCache;

/**
 * 画廊缓存服务接口
 *
 * @author whoiszxl
 */
public interface GalleryCachedService {

    /**
     * 获取缓存的画廊列表（按分类分组）
     *
     * @param version 版本号
     * @return 画廊列表缓存对象
     */
    GalleryListCache getCachedGalleryList(Long version);
}