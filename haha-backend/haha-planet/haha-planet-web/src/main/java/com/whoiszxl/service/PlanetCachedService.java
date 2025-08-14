package com.whoiszxl.service;

import com.whoiszxl.model.cache.PlanetCategoryListCache;
import com.whoiszxl.model.cache.PlanetDetailCache;
import com.whoiszxl.model.cache.PlanetListCache;

/**
 * 星球缓存服务接口
 *
 * @author whoiszxl
 */
public interface PlanetCachedService {

    /**
     * 获取缓存的星球分类列表
     *
     * @param version 版本号
     * @return 分类列表缓存
     */
    PlanetCategoryListCache getCachedPlanetCategoryList(Long version);

    /**
     * 获取缓存的星球列表
     *
     * @param categoryId 分类ID
     * @param page 页码
     * @param pageSize 页大小
     * @param sortType 排序类型
     * @param version 版本号
     * @return 星球列表缓存
     */
    PlanetListCache getCachedPlanetList(Long categoryId, Integer page, Integer pageSize, Integer sortType, Long version);

    /**
     * 获取缓存的星球详情
     *
     * @param planetId 星球ID
     * @param version 版本号
     * @return 星球详情缓存
     */
    PlanetDetailCache getCachedPlanetDetail(Long planetId, Long version);
}
