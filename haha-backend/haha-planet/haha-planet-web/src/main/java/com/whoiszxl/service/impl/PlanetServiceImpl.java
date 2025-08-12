package com.whoiszxl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whoiszxl.model.req.PlanetListReq;
import com.whoiszxl.model.resp.PlanetResp;
import com.whoiszxl.planet.mapper.PlanetCategoryMapper;
import com.whoiszxl.planet.mapper.PlanetMapper;
import com.whoiszxl.planet.model.entity.PlanetCategoryDO;
import com.whoiszxl.planet.model.entity.PlanetDO;
import com.whoiszxl.service.PlanetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 星球服务接口实现
 * @author whoiszxl
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PlanetServiceImpl implements PlanetService {

    private final PlanetMapper planetMapper;
    private final PlanetCategoryMapper planetCategoryMapper;

    @Override
    public Page<PlanetResp> listPlanetsByCategory(PlanetListReq req) {
        log.info("[星球服务] 查询分类ID: {} 的星球列表，页码: {}, 页大小: {}, 排序: {}", 
                req.getCategoryId(), req.getPage(), req.getPageSize(), req.getSortType());
        
        // 创建分页对象
        Page<PlanetDO> page = new Page<>(req.getPage(), req.getPageSize());
        
        // 构建查询条件
        LambdaQueryWrapper<PlanetDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PlanetDO::getStatus, 1)
                   .eq(PlanetDO::getIsPublic, 1);

        if(req.getCategoryId() != null) {
            queryWrapper.eq(PlanetDO::getCategoryId, req.getCategoryId());
        }
        
        // 根据排序类型设置排序规则
        switch (req.getSortType()) {
            case 2:
                queryWrapper.orderByDesc(PlanetDO::getHotScore)
                           .orderByDesc(PlanetDO::getViewCount)
                           .orderByDesc(PlanetDO::getMemberCount);
                break;
            case 3:
                queryWrapper.orderByDesc(PlanetDO::getRecommendWeight)
                           .orderByDesc(PlanetDO::getQualityScore)
                           .orderByDesc(PlanetDO::getIsFeatured);
                break;
            default: // 最新
                queryWrapper.orderByDesc(PlanetDO::getCreatedBy);
                break;
        }
        
        // 执行分页查询
        Page<PlanetDO> planetPage = planetMapper.selectPage(page, queryWrapper);
        
        // 获取分类信息用于填充分类名称
        List<Long> categoryIds = planetPage.getRecords().stream()
                .map(PlanetDO::getCategoryId)
                .distinct()
                .collect(Collectors.toList());
        
        Map<Long, String> categoryNameMap = getCategoryNameMap(categoryIds);
        
        // 转换为响应对象
        List<PlanetResp> planetRespList = planetPage.getRecords().stream()
                .map(planetDO -> convertToResp(planetDO, categoryNameMap))
                .collect(Collectors.toList());
        
        // 构建响应分页对象
        Page<PlanetResp> respPage = new Page<>();
        BeanUtils.copyProperties(planetPage, respPage, "records");
        respPage.setRecords(planetRespList);
        
        return respPage;
    }
    
    /**
     * 获取分类名称映射
     *
     * @param categoryIds 分类ID列表
     * @return 分类ID到名称的映射
     */
    private Map<Long, String> getCategoryNameMap(List<Long> categoryIds) {
        if (categoryIds.isEmpty()) {
            return Map.of();
        }
        
        List<PlanetCategoryDO> categories = planetCategoryMapper.selectBatchIds(categoryIds);
        return categories.stream()
                .collect(Collectors.toMap(
                    PlanetCategoryDO::getId,
                    PlanetCategoryDO::getCategoryName,
                    (existing, replacement) -> existing
                ));
    }
    
    /**
     * 转换为响应对象
     *
     * @param planetDO 星球实体
     * @param categoryNameMap 分类名称映射
     * @return 星球响应对象
     */
    private PlanetResp convertToResp(PlanetDO planetDO, Map<Long, String> categoryNameMap) {
        PlanetResp resp = new PlanetResp();
        BeanUtils.copyProperties(planetDO, resp);
        
        // 设置分类名称
        String categoryName = categoryNameMap.get(planetDO.getCategoryId());
        resp.setCategoryName(categoryName);
        
        return resp;
    }

}
