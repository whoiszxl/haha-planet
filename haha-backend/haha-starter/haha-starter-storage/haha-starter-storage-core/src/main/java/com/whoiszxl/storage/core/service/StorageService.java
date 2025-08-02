package com.whoiszxl.storage.core.service;

import com.whoiszxl.storage.core.domain.StorageObject;
import com.whoiszxl.storage.core.domain.UploadRequest;

import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import java.util.List;

/**
 * 对象存储服务接口
 *
 * @author whoiszxl
 * @since 1.0.0
 */
public interface StorageService {
    
    /**
     * 上传文件
     *
     * @param request 上传请求
     * @return 存储对象
     */
    StorageObject upload(UploadRequest request);
    
    /**
     * 下载文件
     *
     * @param bucketName 存储桶名称
     * @param key 对象键
     * @return 文件流
     */
    InputStream download(String bucketName, String key);
    
    /**
     * 获取对象信息
     *
     * @param bucketName 存储桶名称
     * @param key 对象键
     * @return 存储对象
     */
    StorageObject getObject(String bucketName, String key);
    
    /**
     * 删除文件
     *
     * @param bucketName 存储桶名称
     * @param key 对象键
     * @return 是否删除成功
     */
    boolean delete(String bucketName, String key);
    
    /**
     * 批量删除文件
     *
     * @param bucketName 存储桶名称
     * @param keys 对象键列表
     * @return 删除结果
     */
    List<String> batchDelete(String bucketName, List<String> keys);
    
    /**
     * 检查文件是否存在
     *
     * @param bucketName 存储桶名称
     * @param key 对象键
     * @return 是否存在
     */
    boolean exists(String bucketName, String key);
    
    /**
     * 列出对象
     *
     * @param bucketName 存储桶名称
     * @param prefix 前缀
     * @param maxKeys 最大数量
     * @return 对象列表
     */
    List<StorageObject> listObjects(String bucketName, String prefix, Integer maxKeys);
    
    /**
     * 生成预签名URL
     *
     * @param bucketName 存储桶名称
     * @param key 对象键
     * @param expiration 过期时间
     * @return 预签名URL
     */
    URL generatePresignedUrl(String bucketName, String key, Duration expiration);
    
    /**
     * 创建存储桶
     *
     * @param bucketName 存储桶名称
     * @return 是否创建成功
     */
    boolean createBucket(String bucketName);
    
    /**
     * 删除存储桶
     *
     * @param bucketName 存储桶名称
     * @return 是否删除成功
     */
    boolean deleteBucket(String bucketName);
    
    /**
     * 检查存储桶是否存在
     *
     * @param bucketName 存储桶名称
     * @return 是否存在
     */
    boolean bucketExists(String bucketName);
}