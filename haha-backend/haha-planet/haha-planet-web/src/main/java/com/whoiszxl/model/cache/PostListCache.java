package com.whoiszxl.model.cache;

import com.whoiszxl.model.resp.PostResp;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 帖子列表缓存对象
 *
 * @author whoiszxl
 */
@Data
public class PostListCache implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 帖子列表
     */
    private List<PostResp> postList;

    /**
     * 总数
     */
    private Long total;

    /**
     * 星球ID
     */
    private Long planetId;

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
     * 版本号
     */
    private Long version;

    /**
     * 是否存在
     */
    private Boolean exist;

    /**
     * 是否稍后重试
     */
    private Boolean later;

    /**
     * 设置为存在
     */
    public PostListCache exist() {
        this.exist = true;
        this.later = false;
        return this;
    }

    /**
     * 设置为不存在
     */
    public PostListCache notExist() {
        this.exist = false;
        this.later = false;
        return this;
    }

    /**
     * 设置为稍后重试
     */
    public PostListCache tryLater() {
        this.exist = false;
        this.later = true;
        return this;
    }

    /**
     * 是否存在
     */
    public boolean isExist() {
        return Boolean.TRUE.equals(exist);
    }

    /**
     * 是否稍后重试
     */
    public boolean isLater() {
        return Boolean.TRUE.equals(later);
    }

    // ================================ 链式调用方法 ================================

    /**
     * 设置帖子列表
     */
    public PostListCache setPostList(List<PostResp> postList) {
        this.postList = postList;
        return this;
    }

    /**
     * 设置总数
     */
    public PostListCache setTotal(Long total) {
        this.total = total;
        return this;
    }

    /**
     * 设置星球ID
     */
    public PostListCache setPlanetId(Long planetId) {
        this.planetId = planetId;
        return this;
    }

    /**
     * 设置页码
     */
    public PostListCache setPage(Integer page) {
        this.page = page;
        return this;
    }

    /**
     * 设置页大小
     */
    public PostListCache setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    /**
     * 设置排序类型
     */
    public PostListCache setSortType(Integer sortType) {
        this.sortType = sortType;
        return this;
    }

    /**
     * 设置版本号
     */
    public PostListCache setVersion(Long version) {
        this.version = version;
        return this;
    }

    /**
     * 设置存在标志
     */
    public PostListCache setExist(Boolean exist) {
        this.exist = exist;
        return this;
    }

    /**
     * 设置稍后重试标志
     */
    public PostListCache setLater(Boolean later) {
        this.later = later;
        return this;
    }
}
