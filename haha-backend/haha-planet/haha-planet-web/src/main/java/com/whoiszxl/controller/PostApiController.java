package com.whoiszxl.controller;

import com.whoiszxl.model.cache.PostListCache;
import com.whoiszxl.model.resp.PostResp;
import com.whoiszxl.model.resp.VersionedResponse;
import com.whoiszxl.service.PostCachedService;
import com.whoiszxl.starter.crud.model.PageResponse;
import com.whoiszxl.starter.web.model.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequiredArgsConstructor
@Validated
public class PostApiController {

    private final PostCachedService postCachedService;

    @Operation(summary = "根据星球ID查询帖子列表", description = "分页查询指定星球下的帖子列表，支持多种排序方式，使用多级缓存")
    @GetMapping("/planet/{planetId}/{version}")
    public R<VersionedResponse<PageResponse<PostResp>>> getPostsByPlanetId(
            @Parameter(description = "星球ID", required = true) @PathVariable Long planetId,
            @Parameter(description = "版本号，用于缓存版本控制") @PathVariable Long version,
            @Parameter(description = "页码，默认1") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页大小，默认20") @RequestParam(defaultValue = "20") Integer pageSize,
            @Parameter(description = "排序方式：1-最新发布 2-最多点赞 3-最多评论 4-最多浏览，默认1") @RequestParam(defaultValue = "1") Integer sortType) {
        
        log.info("[帖子API] 查询星球帖子列表，星球ID: {}, 页码: {}, 页大小: {}, 排序方式: {}, 版本号: {}", 
                planetId, page, pageSize, sortType, version);
        
        try {
            // 参数校验
            if (planetId == null || planetId <= 0) {
                log.warn("[帖子API] 星球ID参数无效: {}", planetId);
                return R.fail("星球ID参数无效");
            }
            
            // 使用缓存服务获取帖子列表
            PostListCache postListCache = postCachedService.getCachedPostList(planetId, page, pageSize, sortType, version);
            
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
}
