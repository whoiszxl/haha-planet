package com.whoiszxl.storage.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.whoiszxl.storage.core.domain.StorageObject;
import com.whoiszxl.storage.core.domain.UploadRequest;
import com.whoiszxl.storage.core.service.StorageService;
import com.whoiszxl.storage.s3.properties.S3StorageProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * S3存储服务实现
 *
 * @author whoiszxl
 * @since 1.0.0
 */
@Slf4j
@RequiredArgsConstructor
public class S3StorageService implements StorageService {
    
    private final AmazonS3 amazonS3;
    private final S3StorageProperties properties;
    
    @Override
    public StorageObject upload(UploadRequest request) {
        try {
            String bucketName = getBucketName(request.getBucketName());
            
            // 检查存储桶是否存在，不存在则创建
            if (!amazonS3.doesBucketExistV2(bucketName)) {
                createBucket(bucketName);
            }
            
            // 检查文件是否已存在
            if (!request.getOverwrite() && exists(bucketName, request.getKey())) {
                throw new RuntimeException("文件已存在: " + request.getKey());
            }
            
            // 创建上传请求
            ObjectMetadata metadata = new ObjectMetadata();
            if (request.getContentLength() != null) {
                metadata.setContentLength(request.getContentLength());
            }
            if (StringUtils.hasText(request.getContentType())) {
                metadata.setContentType(request.getContentType());
            }
            if (request.getMetadata() != null) {
                request.getMetadata().forEach(metadata::addUserMetadata);
            }
            
            PutObjectRequest putRequest = new PutObjectRequest(
                bucketName, request.getKey(), request.getContent(), metadata);
            
            // 执行上传
            PutObjectResult result = amazonS3.putObject(putRequest);
            
            log.info("文件上传成功: bucket={}, key={}, etag={}", 
                bucketName, request.getKey(), result.getETag());
            
            // 构建返回对象
            return new StorageObject()
                .setKey(request.getKey())
                .setBucketName(bucketName)
                .setFileName(request.getFileName())
                .setContentType(request.getContentType())
                .setSize(request.getContentLength())
                .setMetadata(request.getMetadata())
                .setEtag(result.getETag())
                .setCreateTime(LocalDateTime.now())
                .setUrl(getObjectUrl(bucketName, request.getKey()));
                
        } catch (Exception e) {
            log.error("文件上传失败: bucket={}, key={}", request.getBucketName(), request.getKey(), e);
            throw new RuntimeException("文件上传失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public InputStream download(String bucketName, String key) {
        try {
            bucketName = getBucketName(bucketName);
            S3Object s3Object = amazonS3.getObject(bucketName, key);
            return s3Object.getObjectContent();
        } catch (Exception e) {
            log.error("文件下载失败: bucket={}, key={}", bucketName, key, e);
            throw new RuntimeException("文件下载失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public StorageObject getObject(String bucketName, String key) {
        try {
            bucketName = getBucketName(bucketName);
            ObjectMetadata metadata = amazonS3.getObjectMetadata(bucketName, key);
            
            return new StorageObject()
                .setKey(key)
                .setBucketName(bucketName)
                .setContentType(metadata.getContentType())
                .setSize(metadata.getContentLength())
                .setEtag(metadata.getETag())
                .setCreateTime(metadata.getLastModified() != null ? 
                    LocalDateTime.ofInstant(metadata.getLastModified().toInstant(), ZoneId.systemDefault()) : null)
                .setUrl(getObjectUrl(bucketName, key));
                
        } catch (Exception e) {
            log.error("获取对象信息失败: bucket={}, key={}", bucketName, key, e);
            throw new RuntimeException("获取对象信息失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean delete(String bucketName, String key) {
        try {
            bucketName = getBucketName(bucketName);
            amazonS3.deleteObject(bucketName, key);
            log.info("文件删除成功: bucket={}, key={}", bucketName, key);
            return true;
        } catch (Exception e) {
            log.error("文件删除失败: bucket={}, key={}", bucketName, key, e);
            return false;
        }
    }
    
    @Override
    public List<String> batchDelete(String bucketName, List<String> keys) {
        List<String> failedKeys = new ArrayList<>();
        bucketName = getBucketName(bucketName);
        
        try {
            DeleteObjectsRequest deleteRequest = new DeleteObjectsRequest(bucketName)
                .withKeys(keys.toArray(new String[0]));
            
            DeleteObjectsResult result = amazonS3.deleteObjects(deleteRequest);
            
            // 获取删除失败的键
            List<String> deletedKeys = result.getDeletedObjects().stream()
                .map(DeleteObjectsResult.DeletedObject::getKey)
                .toList();
            
            failedKeys = keys.stream()
                .filter(key -> !deletedKeys.contains(key))
                .collect(Collectors.toList());
                
            log.info("批量删除完成: bucket={}, 成功={}, 失败={}", 
                bucketName, deletedKeys.size(), failedKeys.size());
                
        } catch (Exception e) {
            log.error("批量删除失败: bucket={}", bucketName, e);
            failedKeys = keys;
        }
        
        return failedKeys;
    }
    
    @Override
    public boolean exists(String bucketName, String key) {
        try {
            bucketName = getBucketName(bucketName);
            return amazonS3.doesObjectExist(bucketName, key);
        } catch (Exception e) {
            log.error("检查文件存在性失败: bucket={}, key={}", bucketName, key, e);
            return false;
        }
    }
    
    @Override
    public List<StorageObject> listObjects(String bucketName, String prefix, Integer maxKeys) {
        try {
            bucketName = getBucketName(bucketName);
            
            ListObjectsV2Request request = new ListObjectsV2Request()
                .withBucketName(bucketName)
                .withPrefix(prefix)
                .withMaxKeys(maxKeys != null ? maxKeys : 1000);
            
            ListObjectsV2Result result = amazonS3.listObjectsV2(request);

            String finalBucketName = bucketName;
            return result.getObjectSummaries().stream()
                .map(summary -> new StorageObject()
                    .setKey(summary.getKey())
                    .setBucketName(finalBucketName)
                    .setSize(summary.getSize())
                    .setEtag(summary.getETag())
                    .setCreateTime(summary.getLastModified() != null ?
                        LocalDateTime.ofInstant(summary.getLastModified().toInstant(), ZoneId.systemDefault()) : null)
                    .setUrl(getObjectUrl(finalBucketName, summary.getKey())))
                .collect(Collectors.toList());
                
        } catch (Exception e) {
            log.error("列出对象失败: bucket={}, prefix={}", bucketName, prefix, e);
            throw new RuntimeException("列出对象失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public URL generatePresignedUrl(String bucketName, String key, Duration expiration) {
        try {
            bucketName = getBucketName(bucketName);
            Date expirationDate = new Date(System.currentTimeMillis() + expiration.toMillis());
            
            return amazonS3.generatePresignedUrl(bucketName, key, expirationDate);
        } catch (Exception e) {
            log.error("生成预签名URL失败: bucket={}, key={}", bucketName, key, e);
            throw new RuntimeException("生成预签名URL失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean createBucket(String bucketName) {
        try {
            bucketName = getBucketName(bucketName);
            if (!amazonS3.doesBucketExistV2(bucketName)) {
                amazonS3.createBucket(bucketName);
                log.info("存储桶创建成功: {}", bucketName);
                return true;
            }
            return true;
        } catch (Exception e) {
            log.error("创建存储桶失败: {}", bucketName, e);
            return false;
        }
    }
    
    @Override
    public boolean deleteBucket(String bucketName) {
        try {
            bucketName = getBucketName(bucketName);
            amazonS3.deleteBucket(bucketName);
            log.info("存储桶删除成功: {}", bucketName);
            return true;
        } catch (Exception e) {
            log.error("删除存储桶失败: {}", bucketName, e);
            return false;
        }
    }
    
    @Override
    public boolean bucketExists(String bucketName) {
        try {
            bucketName = getBucketName(bucketName);
            return amazonS3.doesBucketExistV2(bucketName);
        } catch (Exception e) {
            log.error("检查存储桶存在性失败: {}", bucketName, e);
            return false;
        }
    }
    
    /**
     * 获取存储桶名称
     */
    private String getBucketName(String bucketName) {
        return StringUtils.hasText(bucketName) ? bucketName : properties.getDefaultBucket();
    }
    
    /**
     * 获取对象访问URL
     */
    private String getObjectUrl(String bucketName, String key) {
        try {
            return amazonS3.getUrl(bucketName, key).toString();
        } catch (Exception e) {
            log.warn("获取对象URL失败: bucket={}, key={}", bucketName, key, e);
            return null;
        }
    }
}