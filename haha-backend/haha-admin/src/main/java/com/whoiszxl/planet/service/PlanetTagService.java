package com.whoiszxl.planet.service;

import com.whoiszxl.starter.crud.service.BaseService;
import com.whoiszxl.planet.model.query.PlanetTagQuery;
import com.whoiszxl.planet.model.req.PlanetTagReq;
import com.whoiszxl.planet.model.resp.PlanetTagDetailResp;
import com.whoiszxl.planet.model.resp.PlanetTagResp;

/**
 * 星球标签业务接口
 *
 * @author whoiszxl
 */
public interface PlanetTagService extends BaseService<PlanetTagResp, PlanetTagDetailResp, PlanetTagQuery, PlanetTagReq> {}