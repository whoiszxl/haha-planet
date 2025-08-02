package com.whoiszxl.storage.core.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 存储对象
 *
 * @author whoiszxl
 * @since 1.0.0
 */
@Data
@Accessors(chain = true)
public class StorageObject {
    
    /**
     * 对象键
     */
    private String key;
    
    /**
     * 存储桶名称
     */
    private String bucketName;
    
    /**
     * 文件名
     */
    private String fileName;
    
    /**
     * 内容类型
     */
    private String contentType;
    
    /**
     * 文件大小
     */
    private Long size;
    
    /**
     * 文件内容
     */
    private InputStream content;
    
    /**
     * 元数据
     */
    private Map<String, String> metadata;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
    
    /**
     * ETag
     */
    private String etag;
    
    /**
     * 访问URL
     */
    private String url;
}