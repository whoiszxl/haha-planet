package com.whoiszxl.storage.core.domain;

import lombok.Data;

import java.io.InputStream;
import java.util.Map;

/**
 * 上传请求
 *
 * @author whoiszxl
 * @since 1.0.0
 */
@Data
public class UploadRequest {
    
    /**
     * 存储桶名称
     */
    private String bucketName;
    
    /**
     * 对象键
     */
    private String key;
    
    /**
     * 文件名
     */
    private String fileName;
    
    /**
     * 内容类型
     */
    private String contentType;
    
    /**
     * 文件内容
     */
    private InputStream content;
    
    /**
     * 文件大小
     */
    private Long contentLength;
    
    /**
     * 元数据
     */
    private Map<String, String> metadata;
    
    /**
     * 是否覆盖已存在的文件
     */
    private Boolean overwrite = false;
    
    /**
     * 设置存储桶名称
     * @param bucketName 存储桶名称
     * @return 当前对象
     */
    public UploadRequest bucketName(String bucketName) {
        this.bucketName = bucketName;
        return this;
    }
    
    /**
     * 设置对象键
     * @param key 对象键
     * @return 当前对象
     */
    public UploadRequest key(String key) {
        this.key = key;
        return this;
    }
    
    /**
     * 设置文件名
     * @param fileName 文件名
     * @return 当前对象
     */
    public UploadRequest fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }
    
    /**
     * 设置内容类型
     * @param contentType 内容类型
     * @return 当前对象
     */
    public UploadRequest contentType(String contentType) {
        this.contentType = contentType;
        return this;
    }
    
    /**
     * 设置文件内容
     * @param content 文件内容
     * @return 当前对象
     */
    public UploadRequest content(InputStream content) {
        this.content = content;
        return this;
    }
    
    /**
     * 设置文件大小
     * @param contentLength 文件大小
     * @return 当前对象
     */
    public UploadRequest contentLength(Long contentLength) {
        this.contentLength = contentLength;
        return this;
    }
    
    /**
     * 设置元数据
     * @param metadata 元数据
     * @return 当前对象
     */
    public UploadRequest metadata(Map<String, String> metadata) {
        this.metadata = metadata;
        return this;
    }
    
    /**
     * 设置是否覆盖已存在的文件
     * @param overwrite 是否覆盖
     * @return 当前对象
     */
    public UploadRequest overwrite(Boolean overwrite) {
        this.overwrite = overwrite;
        return this;
    }
    
    /**
     * 创建一个新的上传请求实例
     * @return 新的上传请求实例
     */
    public static UploadRequest builder() {
        return new UploadRequest();
    }
}