package com.whoiszxl.storage.s3.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * S3存储配置属性
 *
 * @author whoiszxl
 * @since 1.0.0
 */
@Data
@ConfigurationProperties(prefix = "haha.storage.s3")
public class S3StorageProperties {
    
    /**
     * 是否启用S3存储
     */
    private Boolean enabled = true;
    
    /**
     * 访问密钥ID
     */
    private String accessKey;
    
    /**
     * 秘密访问密钥
     */
    private String secretKey;
    
    /**
     * 区域
     */
    private String region = "us-east-1";
    
    /**
     * 端点URL（用于兼容S3的服务）
     */
    private String endpoint;
    
    /**
     * 默认存储桶名称
     */
    private String defaultBucket;
    
    /**
     * 是否启用路径样式访问
     */
    private Boolean pathStyleAccess = false;
    
    /**
     * 连接超时时间（毫秒）
     */
    private Integer connectionTimeout = 10000;
    
    /**
     * 读取超时时间（毫秒）
     */
    private Integer socketTimeout = 50000;
    
    /**
     * 最大连接数
     */
    private Integer maxConnections = 50;
    
    /**
     * 是否启用SSL
     */
    private Boolean useSSL = true;
    
    /**
     * 预签名URL默认过期时间（秒）
     */
    private Long defaultPresignedUrlExpiration = 3600L;
}