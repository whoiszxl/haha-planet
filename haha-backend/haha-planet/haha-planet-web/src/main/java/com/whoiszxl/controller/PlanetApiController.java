package com.whoiszxl.controller;

import com.whoiszxl.model.cache.PlanetCategoryListCache;
import com.whoiszxl.model.cache.PlanetListCache;
import com.whoiszxl.model.req.PlanetListReq;
import com.whoiszxl.model.resp.PlanetCategoryResp;
import com.whoiszxl.model.resp.PlanetResp;
import com.whoiszxl.model.resp.VersionedResponse;
import com.whoiszxl.service.PlanetCachedService;
import com.whoiszxl.starter.crud.model.PageResponse;
import com.whoiszxl.starter.web.model.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 星球管理 API
 *
 * @author whoiszxl
 */
@Slf4j
@Tag(name = "星球管理 API")
@RestController
@RequestMapping(value = "/api/planet")
@RequiredArgsConstructor
@Validated
public class PlanetApiController {

    private final PlanetCachedService planetCachedService;

    @Operation(summary = "查询星球一级分类列表", description = "获取所有启用状态的一级分类，使用多级缓存优化性能")
    @GetMapping("/categories/{version}")
    public R<VersionedResponse<List<PlanetCategoryResp>>> listFirstLevelCategories(@PathVariable Long version) {
        log.info("[星球API] 查询一级分类列表，版本号: {}", version);
        
        PlanetCategoryListCache categoryListCache = planetCachedService.getCachedPlanetCategoryList(version);
        
        // 如果需要稍后重试
        if (categoryListCache.isLater()) {
            log.warn("[星球API] 分类列表缓存获取失败，请稍后再试");
            return R.ok(VersionedResponse.tryLater());
        }

        List<PlanetCategoryResp> categories = categoryListCache.getCategoryList();
        Long currentVersion = categoryListCache.getVersion();
        
        log.info("[星球API] 查询到 {} 个一级分类，版本号: {}", 
                categories != null ? categories.size() : 0, currentVersion);
        
        // 如果数据不存在
        if (!categoryListCache.isExist()) {
            return R.ok(VersionedResponse.notExist(currentVersion));
        }
        
        // 返回成功响应
        return R.ok(VersionedResponse.success(categories, currentVersion));
    }

    @Operation(summary = "根据分类ID查询星球列表", description = "分页查询指定分类下的星球，支持多种排序方式，使用多级缓存")
    @PostMapping("/list/{version}")
    public R<VersionedResponse<PageResponse<PlanetResp>>> listPlanetsByCategory(@Valid @RequestBody PlanetListReq req, @PathVariable Long version) {
        log.info("[星球API] 查询分类ID: {} 的星球列表，页码: {}, 页大小: {}, 排序: {}, 版本号: {}", 
                req.getCategoryId(), req.getPage(), req.getPageSize(), req.getSortType(), version);

        PlanetListCache planetListCache = planetCachedService.getCachedPlanetList(
                req.getCategoryId(), req.getPage(), req.getPageSize(), req.getSortType(), version);
        
        // 如果需要稍后重试
        if (planetListCache.isLater()) {
            log.warn("[星球API] 星球列表缓存获取失败，请稍后再试");
            return R.ok(VersionedResponse.tryLater());
        }

        List<PlanetResp> planetList = planetListCache.getPlanetList();
        Long total = planetListCache.getTotal();
        Long currentVersion = planetListCache.getVersion();
        
        log.info("[星球API] 查询到 {} 条星球记录，总计 {} 条，版本号: {}", 
                planetList != null ? planetList.size() : 0, total, currentVersion);

        // 如果数据不存在
        if (!planetListCache.isExist()) {
            return R.ok(VersionedResponse.notExist(currentVersion));
        }

        // 构建分页响应
        PageResponse<PlanetResp> pageResponse = new PageResponse<>();
        pageResponse.setList(planetList);
        pageResponse.setTotal(total);
        
        // 返回成功响应
        return R.ok(VersionedResponse.success(pageResponse, currentVersion));
    }

}