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
}