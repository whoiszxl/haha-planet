package com.whoiszxl.controller;

import com.whoiszxl.model.cache.PostDetailCache;
import com.whoiszxl.model.cache.PostListCache;
import com.whoiszxl.model.resp.PostResp;
import com.whoiszxl.model.resp.VersionedResponse;
import com.whoiszxl.service.PostCachedService;
import com.whoiszxl.starter.crud.model.PageResponse;
import com.whoiszxl.starter.web.model.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    
    public PostApiController(
            @Qualifier("postRedisCachedServiceImpl") PostCachedService postCachedService) {
        this.postCachedService = postCachedService;
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
            if (planetId == null || planetId <= 0) {
                log.warn("[帖子API] 星球ID参数无效: {}", planetId);
                return R.fail("星球ID参数无效");
            }
            
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
        if (postId == null || postId <= 0) {
            log.warn("[帖子API] 帖子ID参数无效: {}", postId);
            return R.fail("帖子ID不能为空");
        }
        
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
            
            log.info("[帖子API] 成功获取帖子详情，帖子ID: {}, 标题: {}", 
                    postId, 
                    postDetailCache.getPostDetail() != null ? postDetailCache.getPostDetail().getTitle() : "未知");
            
            return R.ok(postDetailCache);
            
        } catch (Exception e) {
            log.error("[帖子API] 获取帖子详情异常，帖子ID: {}", postId, e);
            return R.fail("获取帖子详情失败");
        }
    }
}
