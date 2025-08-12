package com.whoiszxl.model.cache;

import com.whoiszxl.model.resp.PlanetResp;
import lombok.Data;

import java.util.List;

/**
 * 星球列表缓存模型
 *
 * @author whoiszxl
 */
@Data
public class PlanetListCache {

    /**
     * 星球列表
     */
    private List<PlanetResp> planetList;

    /**
     * 总数
     */
    private Long total;

    /**
     * 版本号，用于缓存一致性控制
     */
    private Long version;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 页码
     */
    private Integer page;

    /**
     * 页大小
     */
    private Integer pageSize;

    /**
     * 排序类型
     */
    private Integer sortType;

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
    public PlanetListCache notExist() {
        this.exist = false;
        this.later = false;
        this.total = 0L;
        return this;
    }

    /**
     * 设置为稍后再试
     */
    public PlanetListCache tryLater() {
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
     * 设置星球列表
     */
    public PlanetListCache setPlanetList(List<PlanetResp> planetList) {
        this.planetList = planetList;
        return this;
    }

    /**
     * 设置总数
     */
    public PlanetListCache setTotal(Long total) {
        this.total = total;
        return this;
    }

    /**
     * 设置版本号
     */
    public PlanetListCache setVersion(Long version) {
        this.version = version;
        return this;
    }

    /**
     * 设置分类ID
     */
    public PlanetListCache setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
        return this;
    }

    /**
     * 设置页码
     */
    public PlanetListCache setPage(Integer page) {
        this.page = page;
        return this;
    }

    /**
     * 设置页大小
     */
    public PlanetListCache setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    /**
     * 设置排序类型
     */
    public PlanetListCache setSortType(Integer sortType) {
        this.sortType = sortType;
        return this;
    }

    /**
     * 设置存在标志
     */
    public PlanetListCache setExist(Boolean exist) {
        this.exist = exist;
        return this;
    }

    /**
     * 设置稍后重试标志
     */
    public PlanetListCache setLater(Boolean later) {
        this.later = later;
        return this;
    }
}
