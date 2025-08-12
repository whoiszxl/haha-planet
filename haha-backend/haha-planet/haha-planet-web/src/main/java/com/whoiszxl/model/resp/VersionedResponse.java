package com.whoiszxl.model.resp;

import lombok.Data;

/**
 * 带版本号的响应模型
 *
 * @author whoiszxl
 */
@Data
public class VersionedResponse<T> {

    /**
     * 响应数据
     */
    private T data;

    /**
     * 版本号
     */
    private Long version;

    /**
     * 是否存在数据
     */
    private Boolean exist;

    /**
     * 是否稍后重试
     */
    private Boolean later;

    /**
     * 构造成功响应
     */
    public static <T> VersionedResponse<T> success(T data, Long version) {
        VersionedResponse<T> response = new VersionedResponse<>();
        response.setData(data);
        response.setVersion(version);
        response.setExist(true);
        response.setLater(false);
        return response;
    }

    /**
     * 构造不存在数据的响应
     */
    public static <T> VersionedResponse<T> notExist(Long version) {
        VersionedResponse<T> response = new VersionedResponse<>();
        response.setData(null);
        response.setVersion(version);
        response.setExist(false);
        response.setLater(false);
        return response;
    }

    /**
     * 构造稍后重试的响应
     */
    public static <T> VersionedResponse<T> tryLater() {
        VersionedResponse<T> response = new VersionedResponse<>();
        response.setData(null);
        response.setVersion(null);
        response.setExist(false);
        response.setLater(true);
        return response;
    }

    // ================================ 链式调用方法 ================================

    /**
     * 设置数据
     */
    public VersionedResponse<T> setData(T data) {
        this.data = data;
        return this;
    }

    /**
     * 设置版本号
     */
    public VersionedResponse<T> setVersion(Long version) {
        this.version = version;
        return this;
    }

    /**
     * 设置存在标志
     */
    public VersionedResponse<T> setExist(Boolean exist) {
        this.exist = exist;
        return this;
    }

    /**
     * 设置稍后重试标志
     */
    public VersionedResponse<T> setLater(Boolean later) {
        this.later = later;
        return this;
    }
}
