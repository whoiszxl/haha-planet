package com.whoiszxl.model.cache;

import com.whoiszxl.model.resp.PostResp;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 帖子详情缓存对象
 *
 * @author whoiszxl
 */
@Data
public class PostDetailCache implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 帖子详情
     */
    private PostResp postDetail;

    /**
     * 帖子ID
     */
    private Long postId;

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
    public PostDetailCache exist() {
        this.exist = true;
        this.later = false;
        return this;
    }

    /**
     * 设置为不存在
     */
    public PostDetailCache notExist() {
        this.exist = false;
        this.later = false;
        return this;
    }

    /**
     * 设置为稍后重试
     */
    public PostDetailCache tryLater() {
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
     * 设置帖子详情
     */
    public PostDetailCache setPostDetail(PostResp postDetail) {
        this.postDetail = postDetail;
        return this;
    }

    /**
     * 设置帖子ID
     */
    public PostDetailCache setPostId(Long postId) {
        this.postId = postId;
        return this;
    }

    /**
     * 设置版本号
     */
    public PostDetailCache setVersion(Long version) {
        this.version = version;
        return this;
    }

    /**
     * 设置存在标志
     */
    public PostDetailCache setExist(Boolean exist) {
        this.exist = exist;
        return this;
    }

    /**
     * 设置稍后重试标志
     */
    public PostDetailCache setLater(Boolean later) {
        this.later = later;
        return this;
    }
}