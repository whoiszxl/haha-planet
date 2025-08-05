package com.whoiszxl.planet.service.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.whoiszxl.starter.crud.service.impl.BaseServiceImpl;
import com.whoiszxl.planet.mapper.PlanetCategoryMapper;
import com.whoiszxl.planet.model.entity.PlanetCategoryDO;
import com.whoiszxl.planet.model.query.PlanetCategoryQuery;
import com.whoiszxl.planet.model.req.PlanetCategoryReq;
import com.whoiszxl.planet.model.resp.PlanetCategoryDetailResp;
import com.whoiszxl.planet.model.resp.PlanetCategoryResp;
import com.whoiszxl.planet.service.PlanetCategoryService;

/**
 * 星球分类业务实现
 *
 * @author whoiszxl
 */
@Service
@RequiredArgsConstructor
public class PlanetCategoryServiceImpl extends BaseServiceImpl<PlanetCategoryMapper, PlanetCategoryDO, PlanetCategoryResp, PlanetCategoryDetailResp, PlanetCategoryQuery, PlanetCategoryReq> implements PlanetCategoryService {}