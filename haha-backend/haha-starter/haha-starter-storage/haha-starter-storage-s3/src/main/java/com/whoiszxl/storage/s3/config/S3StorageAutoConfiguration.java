package com.whoiszxl.storage.s3.config;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.whoiszxl.storage.core.service.StorageService;
import com.whoiszxl.storage.s3.properties.S3StorageProperties;
import com.whoiszxl.storage.s3.service.S3StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;

/**
 * S3存储自动配置类
 *
 * @author whoiszxl
 * @since 1.0.0
 */
@Slf4j
@AutoConfiguration
@RequiredArgsConstructor
@ConditionalOnClass(AmazonS3.class)
@EnableConfigurationProperties(S3StorageProperties.class)
@ConditionalOnProperty(prefix = "haha.storage.s3", name = "enabled", havingValue = "true", matchIfMissing = true)
public class S3StorageAutoConfiguration {
    
    private final S3StorageProperties properties;
    
    /**
     * 创建AmazonS3客户端
     */
    @Bean
    @ConditionalOnMissingBean
    public AmazonS3 amazonS3() {
        // 验证必要配置
        if (!StringUtils.hasText(properties.getAccessKey()) || 
            !StringUtils.hasText(properties.getSecretKey())) {
            throw new IllegalArgumentException("S3存储配置不完整：accessKey和secretKey不能为空");
        }


        // 创建认证信息
        BasicAWSCredentials credentials = new BasicAWSCredentials(
            properties.getAccessKey(), properties.getSecretKey());
        
        // 创建客户端配置
        ClientConfiguration clientConfig = new ClientConfiguration();
        clientConfig.setConnectionTimeout(properties.getConnectionTimeout());
        clientConfig.setSocketTimeout(properties.getSocketTimeout());
        clientConfig.setMaxConnections(properties.getMaxConnections());
        
        // 创建S3客户端构建器
        AmazonS3ClientBuilder builder = AmazonS3ClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withClientConfiguration(clientConfig)
            .withPathStyleAccessEnabled(properties.getPathStyleAccess());
        
        // 设置端点和区域
        if (StringUtils.hasText(properties.getEndpoint())) {
            builder.withEndpointConfiguration(
                new AwsClientBuilder.EndpointConfiguration(
                    properties.getEndpoint(), properties.getRegion()));
        } else {
            builder.withRegion(properties.getRegion());
        }
        
        AmazonS3 amazonS3 = builder.build();
        
        log.info("S3存储客户端初始化完成: endpoint={}, region={}, pathStyleAccess={}", 
            properties.getEndpoint(), properties.getRegion(), properties.getPathStyleAccess());
        
        return amazonS3;
    }
    
    /**
     * 创建S3存储服务
     */
    @Bean
    @ConditionalOnMissingBean(name = "s3StorageService")
    public StorageService s3StorageService(AmazonS3 amazonS3) {
        return new S3StorageService(amazonS3, properties);
    }
}