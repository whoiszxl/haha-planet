package com.whoiszxl.model.cache;

import com.whoiszxl.model.resp.PlanetResp;
import lombok.Data;

/**
 * 星球详情缓存模型
 *
 * @author whoiszxl
 */
@Data
public class PlanetDetailCache {

    /**
     * 星球详情数据
     */
    private PlanetResp planetDetail;

    /**
     * 版本号
     */
    private Long version;

    /**
     * 是否存在
     */
    private Boolean exist;

    /**
     * 是否稍后再试
     */
    private Boolean later;

    /**
     * 星球ID
     */
    private Long planetId;

    /**
     * 设置星球详情数据
     */
    public PlanetDetailCache setPlanetDetail(PlanetResp planetDetail) {
        this.planetDetail = planetDetail;
        return this;
    }

    /**
     * 设置版本号
     */
    public PlanetDetailCache setVersion(Long version) {
        this.version = version;
        return this;
    }

    /**
     * 设置是否存在
     */
    public PlanetDetailCache setExist(Boolean exist) {
        this.exist = exist;
        return this;
    }

    /**
     * 设置是否稍后再试
     */
    public PlanetDetailCache setLater(Boolean later) {
        this.later = later;
        return this;
    }

    /**
     * 设置星球ID
     */
    public PlanetDetailCache setPlanetId(Long planetId) {
        this.planetId = planetId;
        return this;
    }

    /**
     * 设置数据存在
     */
    public PlanetDetailCache exist() {
        this.exist = true;
        this.later = false;
        return this;
    }

    /**
     * 设置数据不存在
     */
    public PlanetDetailCache notExist() {
        this.exist = false;
        this.later = false;
        this.planetDetail = null;
        this.version = System.currentTimeMillis();
        return this;
    }

    /**
     * 设置稍后再试
     */
    public PlanetDetailCache tryLater() {
        this.later = true;
        this.exist = false;
        this.planetDetail = null;
        return this;
    }

    /**
     * 是否存在数据
     */
    public boolean isExist() {
        return Boolean.TRUE.equals(exist);
    }

    /**
     * 是否稍后再试
     */
    public boolean isLater() {
        return Boolean.TRUE.equals(later);
    }
}
