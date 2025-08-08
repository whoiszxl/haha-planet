package com.whoiszxl.service;

import com.whoiszxl.model.resp.PlanetCategoryResp;

import java.util.List;

/**
 * 星球分类服务接口
 * @author whoiszxl
 */
public interface PlanetCategoryService {

    /**
     * 查询星球的一级分类列表
     * 使用多级缓存：本地缓存30分钟，远程缓存2小时
     *
     * @return 一级分类列表
     */
    List<PlanetCategoryResp> listFirstLevelCategories();

}
