package com.whoiszxl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.whoiszxl.model.resp.PlanetCategoryResp;
import com.whoiszxl.planet.mapper.PlanetCategoryMapper;
import com.whoiszxl.planet.model.entity.PlanetCategoryDO;
import com.whoiszxl.service.PlanetCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 星球分类服务接口实现
 * @author whoiszxl
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PlanetCategoryServiceImpl implements PlanetCategoryService {

    private final PlanetCategoryMapper planetCategoryMapper;

    @Override
    public List<PlanetCategoryResp> listFirstLevelCategories() {
        log.info("[星球分类服务] 查询一级分类列表");
        
        // 查询一级分类（parentId = 0 且 level = 1）
        LambdaQueryWrapper<PlanetCategoryDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PlanetCategoryDO::getParentId, 0L)
                   .eq(PlanetCategoryDO::getLevel, 1)
                   .eq(PlanetCategoryDO::getStatus, 1)
                   .eq(PlanetCategoryDO::getIsDeleted, 0)
                   .orderByDesc(PlanetCategoryDO::getSort)
                   .orderByAsc(PlanetCategoryDO::getId);
        
        List<PlanetCategoryDO> categoryList = planetCategoryMapper.selectList(queryWrapper);
        
        // 转换为响应对象
        return categoryList.stream()
                .map(this::convertToResp)
                .collect(Collectors.toList());
    }
    
    /**
     * 转换为响应对象
     *
     * @param categoryDO 分类实体
     * @return 分类响应对象
     */
    private PlanetCategoryResp convertToResp(PlanetCategoryDO categoryDO) {
        PlanetCategoryResp resp = new PlanetCategoryResp();
        BeanUtils.copyProperties(categoryDO, resp);
        return resp;
    }

}
