package com.whoiszxl.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whoiszxl.model.req.PlanetListReq;
import com.whoiszxl.model.resp.PlanetCategoryResp;
import com.whoiszxl.model.resp.PlanetResp;
import com.whoiszxl.service.PlanetCategoryService;
import com.whoiszxl.service.PlanetService;
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

    private final PlanetCategoryService planetCategoryService;
    private final PlanetService planetService;

    @Operation(summary = "查询星球一级分类列表", description = "获取所有启用状态的一级分类，使用多级缓存优化性能")
    @GetMapping("/categories")
    public R<List<PlanetCategoryResp>> listFirstLevelCategories() {
        log.info("[星球API] 查询一级分类列表");
        
        List<PlanetCategoryResp> categories = planetCategoryService.listFirstLevelCategories();
        
        log.info("[星球API] 查询到 {} 个一级分类", categories.size());
        return R.ok(categories);
    }

    @Operation(summary = "根据分类ID查询星球列表", description = "分页查询指定分类下的星球，支持多种排序方式，使用多级缓存")
    @PostMapping("/list")
    public R<PageResponse<PlanetResp>> listPlanetsByCategory(@Valid @RequestBody PlanetListReq req) {
        log.info("[星球API] 查询分类ID: {} 的星球列表，页码: {}, 页大小: {}, 排序: {}", 
                req.getCategoryId(), req.getPage(), req.getPageSize(), req.getSortType());

        Page<PlanetResp> planetPage = planetService.listPlanetsByCategory(req);
        
        log.info("[星球API] 查询到 {} 条星球记录，总计 {} 条", 
                planetPage.getTotal(), planetPage.getTotal());

        PageResponse<PlanetResp> pageResponse = PageResponse.build(planetPage);

        return R.ok(pageResponse);
    }

}