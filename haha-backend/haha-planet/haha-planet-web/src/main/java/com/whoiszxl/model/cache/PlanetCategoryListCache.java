package com.whoiszxl.model.cache;

import com.whoiszxl.model.resp.PlanetCategoryResp;
import lombok.Data;

import java.util.List;

/**
 * 星球分类列表缓存模型
 *
 * @author whoiszxl
 */
@Data
public class PlanetCategoryListCache {

    /**
     * 分类列表
     */
    private List<PlanetCategoryResp> categoryList;

    /**
     * 总数
     */
    private Integer total;

    /**
     * 版本号，用于缓存一致性控制
     */
    private Long version;

    /**
     * 是否存在数据
     */
    private Boolean exist;

    /**
     * 是否稍后再试
     */
    private Boolean later;

    /**
     * 设置为不存在
     */
    public PlanetCategoryListCache notExist() {
        this.exist = false;
        this.later = false;
        this.total = 0;
        return this;
    }

    /**
     * 设置为稍后再试
     */
    public PlanetCategoryListCache tryLater() {
        this.exist = false;
        this.later = true;
        return this;
    }

    /**
     * 是否稍后再试
     */
    public boolean isLater() {
        return Boolean.TRUE.equals(this.later);
    }

    /**
     * 是否存在数据
     */
    public boolean isExist() {
        return Boolean.TRUE.equals(this.exist);
    }

    // ================================ 链式调用方法 ================================

    /**
     * 设置分类列表
     */
    public PlanetCategoryListCache setCategoryList(List<PlanetCategoryResp> categoryList) {
        this.categoryList = categoryList;
        return this;
    }

    /**
     * 设置总数
     */
    public PlanetCategoryListCache setTotal(Integer total) {
        this.total = total;
        return this;
    }

    /**
     * 设置版本号
     */
    public PlanetCategoryListCache setVersion(Long version) {
        this.version = version;
        return this;
    }

    /**
     * 设置存在标志
     */
    public PlanetCategoryListCache setExist(Boolean exist) {
        this.exist = exist;
        return this;
    }

    /**
     * 设置稍后重试标志
     */
    public PlanetCategoryListCache setLater(Boolean later) {
        this.later = later;
        return this;
    }
}
