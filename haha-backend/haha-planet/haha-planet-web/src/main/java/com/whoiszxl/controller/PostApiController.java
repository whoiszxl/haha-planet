package com.whoiszxl.controller;

import cn.hutool.core.util.StrUtil;
import com.whoiszxl.common.context.UserContext;
import com.whoiszxl.common.utils.UserLoginHelper;
import com.whoiszxl.model.cache.GalleryListCache;
import com.whoiszxl.model.cache.PostDetailCache;
import com.whoiszxl.model.cache.PostListCache;
import com.whoiszxl.model.req.PostCreateReq;
import com.whoiszxl.model.resp.GalleryResp;
import com.whoiszxl.model.resp.PostResp;
import com.whoiszxl.model.resp.VersionedResponse;
import com.whoiszxl.service.GalleryCachedService;
import com.whoiszxl.service.PostCachedService;
import com.whoiszxl.service.PostService;
import com.whoiszxl.starter.core.utils.validate.CheckUtils;
import com.whoiszxl.starter.core.utils.validate.ValidationUtils;
import com.whoiszxl.starter.crud.model.PageResponse;
import com.whoiszxl.starter.web.model.R;
import com.whoiszxl.storage.core.domain.StorageObject;
import com.whoiszxl.storage.core.domain.UploadRequest;
import com.whoiszxl.storage.core.service.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 帖子管理 API
 *
 * @author whoiszxl
 */
@Slf4j
@Tag(name = "帖子管理 API")
@RestController
@RequestMapping(value = "/api/post")
@Validated
public class PostApiController {

    private final PostCachedService postCachedService;
    private final PostService postService;
    private final GalleryCachedService galleryCachedService;
    private final StorageService storageService;
    
    public PostApiController(
            @Qualifier("postRedisCachedServiceImpl") PostCachedService postCachedService,
            PostService postService,
            @Qualifier("galleryCachedServiceImpl") GalleryCachedService galleryCachedService,
            StorageService storageService) {
        this.postCachedService = postCachedService;
        this.postService = postService;
        this.galleryCachedService = galleryCachedService;
        this.storageService = storageService;
    }

    @Operation(summary = "根据星球ID查询帖子列表", description = "分页查询指定星球下的帖子列表，支持多种排序方式，只使用Redis缓存")
    @GetMapping("/planet/{planetId}")
    public R<VersionedResponse<PageResponse<PostResp>>> getPostsByPlanetId(
            @Parameter(description = "星球ID", required = true) @PathVariable Long planetId,
            @Parameter(description = "页码，默认1") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页大小，默认20") @RequestParam(defaultValue = "20") Integer pageSize,
            @Parameter(description = "排序方式：1-最新发布 2-最多点赞 3-最多评论 4-最多浏览，默认1") @RequestParam(defaultValue = "1") Integer sortType) {
        
        log.info("[帖子API] 查询星球帖子列表，星球ID: {}, 页码: {}, 页大小: {}, 排序方式: {}", 
                planetId, page, pageSize, sortType);
        
        try {
            // 参数校验
            CheckUtils.throwIf(planetId == null || planetId <= 0, "星球ID参数无效");

            // 权限校验
            UserContext loginUser = UserLoginHelper.getLoginUser();
            CheckUtils.throwIf(!loginUser.getMyPlanetSet().contains(planetId), "无权限访问");

            // 使用缓存服务获取帖子列表
            PostListCache postListCache = postCachedService.getCachedPostList(planetId, page, pageSize, sortType, null);
            
            // 如果需要稍后重试
            if (postListCache.isLater()) {
                log.warn("[帖子API] 获取帖子列表需要稍后重试，星球ID: {}", planetId);
                return R.ok(VersionedResponse.tryLater());
            }
            
            List<PostResp> postList = postListCache.getPostList();
            Long total = postListCache.getTotal();
            Long currentVersion = postListCache.getVersion();
            
            log.info("[帖子API] 查询到 {} 条帖子记录，总计 {} 条，版本号: {}", 
                    postList != null ? postList.size() : 0, total, currentVersion);
            
            // 如果数据不存在
            if (!postListCache.isExist()) {
                return R.ok(VersionedResponse.notExist(currentVersion));
            }
            
            // 构建分页响应
            PageResponse<PostResp> pageResponse = new PageResponse<>();
            pageResponse.setTotal(total);
            pageResponse.setList(postList);
            
            // 返回成功响应
            return R.ok(VersionedResponse.success(pageResponse, currentVersion));
            
        } catch (IllegalArgumentException e) {
            log.warn("[帖子API] 参数错误: {}", e.getMessage());
            return R.fail(e.getMessage());
        } catch (Exception e) {
            log.error("[帖子API] 查询星球帖子列表失败，星球ID: {}", planetId, e);
            return R.fail("查询帖子列表失败，请稍后重试");
        }
    }

    @Operation(summary = "根据帖子ID获取帖子详情", description = "获取指定帖子的详细信息，使用多级缓存")
    @GetMapping("/detail/{postId}")
    public R<PostDetailCache> getPostDetail(
            @Parameter(description = "帖子ID", required = true) @PathVariable Long postId) {
        
        log.info("[帖子API] 获取帖子详情请求，帖子ID: {}", postId);
        
        // 参数校验
        CheckUtils.throwIf(postId == null || postId <= 0, "帖子ID不能为空");
        
        try {
            // 调用缓存服务获取帖子详情
            PostDetailCache postDetailCache = postCachedService.getCachedPostDetail(postId, null);
            
            // 检查缓存状态
            if (postDetailCache.isLater()) {
                log.warn("[帖子API] 获取帖子详情失败，请稍后重试，帖子ID: {}", postId);
                return R.fail("服务繁忙，请稍后重试");
            }
            
            if (!postDetailCache.isExist()) {
                log.info("[帖子API] 帖子不存在，帖子ID: {}", postId);
                return R.fail("帖子不存在或已被删除");
            }

            // 权限校验
            Long planetId = postDetailCache.getPostDetail().getPlanetId();
            UserContext loginUser = UserLoginHelper.getLoginUser();
            CheckUtils.throwIf(!loginUser.getMyPlanetSet().contains(planetId), "无权限访问");

            log.info("[帖子API] 成功获取帖子详情，帖子ID: {}, 标题: {}", 
                    postId, 
                    postDetailCache.getPostDetail() != null ? postDetailCache.getPostDetail().getTitle() : "未知");
            
            return R.ok(postDetailCache);
            
        } catch (Exception e) {
            log.error("[帖子API] 获取帖子详情异常，帖子ID: {}", postId, e);
            return R.fail("获取帖子详情失败");
        }
    }

    @Operation(summary = "新增帖子", description = "创建新的主题或文章帖子")
    @PostMapping
    public R<Long> createPost(@Validated @RequestBody PostCreateReq req) {
        
        log.info("[帖子API] 创建帖子请求，星球ID: {}, 内容类型: {}, 标题: {}", 
                req.getPlanetId(), req.getContentType(), req.getTitle());
        
        // 1. 参数校验
        CheckUtils.throwIf(req.getPlanetId() == null || req.getPlanetId() <= 0, "星球ID不能为空");
        CheckUtils.throwIf(req.getContentType() == null || (req.getContentType() != 1 && req.getContentType() != 2), 
                "内容类型必须为1(主题)或2(文章)");
        
        // 2. 获取当前登录用户
        UserContext userContext = UserLoginHelper.getLoginUser();
        CheckUtils.throwIfNull(userContext, "用户未登录");
        
        try {
            // 3. 调用服务层创建帖子
            Long postId = postService.createPost(req, userContext.getId());
            
            log.info("[帖子API] 帖子创建成功，帖子ID: {}, 用户ID: {}", postId, userContext.getId());
            return R.ok(postId);
            
        } catch (Exception e) {
            log.error("[帖子API] 创建帖子失败，用户ID: {}, 错误信息: {}", userContext.getId(), e.getMessage(), e);
            throw e;
        }
    }

    @Operation(summary = "获取画廊列表", description = "获取所有画廊图片，按分类分组，使用多级缓存")
    @GetMapping("/gallery")
    public R<VersionedResponse<Map<String, List<GalleryResp>>>> getGalleryList(
            @Parameter(description = "版本号，用于缓存控制") @RequestParam(required = false) Long version) {
        
        log.info("[帖子API] 获取画廊列表请求，版本号: {}", version);
        
        try {
            // 使用缓存服务获取画廊列表
            GalleryListCache galleryListCache = galleryCachedService.getCachedGalleryList(version);
            
            // 如果需要稍后重试
            if (galleryListCache.isLater()) {
                log.warn("[帖子API] 获取画廊列表需要稍后重试");
                return R.ok(VersionedResponse.tryLater());
            }
            
            Map<String, List<GalleryResp>> galleryMap = galleryListCache.getGalleryMap();
            Long total = galleryListCache.getTotal();
            Long currentVersion = galleryListCache.getVersion();
            
            log.info("[帖子API] 查询到画廊数据，总计 {} 张图片，分类数: {}，版本号: {}", 
                    total, galleryMap != null ? galleryMap.size() : 0, currentVersion);
            
            // 如果数据不存在
            if (!galleryListCache.isExist()) {
                return R.ok(VersionedResponse.notExist(currentVersion));
            }
            
            // 返回成功响应
            return R.ok(VersionedResponse.success(galleryMap, currentVersion));
            
        } catch (Exception e) {
            log.error("[帖子API] 获取画廊列表失败", e);
            return R.fail("获取画廊列表失败，请稍后重试");
        }
    }

    @Operation(summary = "上传帖子图片", description = "上传帖子相关的图片文件")
    @PostMapping("/upload/image")
    public R<Map<String, String>> uploadPostImage(@RequestParam("imageFile") MultipartFile file) {
        try {
            // 参数校验
            ValidationUtils.throwIfNull(file, "图片文件不能为空");
            ValidationUtils.throwIf(file.isEmpty(), "图片文件不能为空");
            
            // 文件类型校验
            String contentType = file.getContentType();
            ValidationUtils.throwIf(!StrUtil.startWith(contentType, "image/"), "只支持图片格式");
            
            // 文件大小校验 (限制10MB)
            long maxSize = 10 * 1024 * 1024;
            ValidationUtils.throwIf(file.getSize() > maxSize, "图片文件大小不能超过10MB");
            
            // 获取当前登录用户ID
            Long currentUserId = UserLoginHelper.getUserId();
            
            // 生成文件名
            String originalFilename = file.getOriginalFilename();
            String fileExtension = StrUtil.subAfter(originalFilename, ".", true);
            String fileName = "post/image/" + currentUserId + "/" + UUID.randomUUID() + "." + fileExtension;
            
            // 构建上传请求，帖子图片上传到公共bucket
            UploadRequest uploadRequest = UploadRequest.builder()
                    .bucketName("haha-public")
                    .key(fileName)
                    .fileName(originalFilename)
                    .contentType(contentType)
                    .content(file.getInputStream())
                    .contentLength(file.getSize());
            
            // 上传到S3
            StorageObject storageObject = storageService.upload(uploadRequest);
            
            // 返回结果
            Map<String, String> result = new HashMap<>();
            result.put("imageUrl", storageObject.getUrl());
            result.put("fileName", fileName);
            
            return R.ok(result);
            
        } catch (IOException e) {
            log.error("帖子图片上传失败", e);
            throw new RuntimeException("帖子图片上传失败", e);
        }
    }

    @Operation(summary = "上传帖子文件", description = "上传帖子相关的文件（文档、音频、视频等）")
    @PostMapping("/upload/file")
    public R<Map<String, String>> uploadPostFile(@RequestParam("file") MultipartFile file) {
        try {
            // 参数校验
            ValidationUtils.throwIfNull(file, "文件不能为空");
            ValidationUtils.throwIf(file.isEmpty(), "文件不能为空");
            
            // 文件大小校验 (限制50MB)
            long maxSize = 50 * 1024 * 1024;
            ValidationUtils.throwIf(file.getSize() > maxSize, "文件大小不能超过50MB");
            
            // 获取当前登录用户ID
            Long currentUserId = UserLoginHelper.getUserId();
            
            // 生成文件名
            String originalFilename = file.getOriginalFilename();
            String fileExtension = StrUtil.subAfter(originalFilename, ".", true);
            String fileName = "post/file/" + currentUserId + "/" + UUID.randomUUID() + "." + fileExtension;
            
            // 构建上传请求，帖子文件上传到公共bucket
            UploadRequest uploadRequest = UploadRequest.builder()
                    .bucketName("haha-public")
                    .key(fileName)
                    .fileName(originalFilename)
                    .contentType(file.getContentType())
                    .content(file.getInputStream())
                    .contentLength(file.getSize());
            
            // 上传到S3
            StorageObject storageObject = storageService.upload(uploadRequest);
            
            // 返回结果
            Map<String, String> result = new HashMap<>();
            result.put("fileUrl", storageObject.getUrl());
            result.put("fileName", fileName);
            result.put("originalFileName", originalFilename);
            result.put("fileSize", String.valueOf(file.getSize()));
            
            return R.ok(result);
            
        } catch (IOException e) {
            log.error("帖子文件上传失败", e);
            throw new RuntimeException("帖子文件上传失败", e);
        }
    }
}
