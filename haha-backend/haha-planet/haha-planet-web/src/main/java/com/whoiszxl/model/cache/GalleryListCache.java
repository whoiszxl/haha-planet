package com.whoiszxl.model.cache;

import com.whoiszxl.model.resp.GalleryResp;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 画廊列表缓存对象
 *
 * @author whoiszxl
 */
@Data
public class GalleryListCache implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 是否存在数据
     */
    private boolean exist = false;

    /**
     * 是否稍后重试
     */
    private boolean later = false;

    /**
     * 缓存版本号
     */
    private Long version;

    /**
     * 按分类分组的画廊列表
     */
    private Map<String, List<GalleryResp>> galleryMap;

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 设置数据存在
     */
    public GalleryListCache exist() {
        this.exist = true;
        return this;
    }

    /**
     * 设置数据不存在
     */
    public GalleryListCache notExist() {
        this.exist = false;
        return this;
    }

    /**
     * 设置稍后重试
     */
    public GalleryListCache tryLater() {
        this.later = true;
        return this;
    }

    /**
     * 设置画廊映射
     */
    public GalleryListCache setGalleryMap(Map<String, List<GalleryResp>> galleryMap) {
        this.galleryMap = galleryMap;
        return this;
    }

    /**
     * 设置总数
     */
    public GalleryListCache setTotal(Long total) {
        this.total = total;
        return this;
    }

    /**
     * 设置版本号
     */
    public GalleryListCache setVersion(Long version) {
        this.version = version;
        return this;
    }
}