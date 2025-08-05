package com.whoiszxl.planet.service;

import com.whoiszxl.starter.crud.service.BaseService;
import com.whoiszxl.planet.model.query.PlanetCategoryQuery;
import com.whoiszxl.planet.model.req.PlanetCategoryReq;
import com.whoiszxl.planet.model.resp.PlanetCategoryDetailResp;
import com.whoiszxl.planet.model.resp.PlanetCategoryResp;

/**
 * 星球分类业务接口
 *
 * @author whoiszxl
 */
public interface PlanetCategoryService extends BaseService<PlanetCategoryResp, PlanetCategoryDetailResp, PlanetCategoryQuery, PlanetCategoryReq> {}