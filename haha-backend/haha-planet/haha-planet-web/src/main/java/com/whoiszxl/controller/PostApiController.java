package com.whoiszxl.controller;

import com.whoiszxl.model.resp.PostResp;
import com.whoiszxl.service.PostService;
import com.whoiszxl.starter.crud.model.PageResponse;
import com.whoiszxl.starter.web.model.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    private final PostService postService;

    @Operation(summary = "根据星球ID查询帖子列表", description = "分页查询指定星球下的帖子列表，支持多种排序方式")
    @GetMapping("/planet/{planetId}")
    public R<PageResponse<PostResp>> getPostsByPlanetId(
            @Parameter(description = "星球ID", required = true) @PathVariable Long planetId,
            @Parameter(description = "页码，默认1") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页大小，默认20") @RequestParam(defaultValue = "20") Integer pageSize,
            @Parameter(description = "排序方式：1-最新发布 2-最多点赞 3-最多评论 4-最多浏览，默认1") @RequestParam(defaultValue = "1") Integer sortType) {
        
        log.info("[帖子API] 查询星球帖子列表，星球ID: {}, 页码: {}, 页大小: {}, 排序方式: {}", planetId, page, pageSize, sortType);
        
        try {
            PageResponse<PostResp> pageResponse = postService.getPostsByPlanetId(planetId, page, pageSize, sortType);
            return R.ok(pageResponse);
        } catch (IllegalArgumentException e) {
            log.warn("[帖子API] 参数错误: {}", e.getMessage());
            return R.fail(e.getMessage());
        } catch (Exception e) {
            log.error("[帖子API] 查询星球帖子列表失败，星球ID: {}", planetId, e);
            return R.fail("查询帖子列表失败，请稍后重试");
        }
    }
}
