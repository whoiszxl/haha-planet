package com.whoiszxl.planet.service;

import com.whoiszxl.starter.crud.service.BaseService;
import com.whoiszxl.planet.model.query.PlanetQuery;
import com.whoiszxl.planet.model.req.PlanetReq;
import com.whoiszxl.planet.model.resp.PlanetDetailResp;
import com.whoiszxl.planet.model.resp.PlanetResp;

/**
 * 星球业务接口
 *
 * @author whoiszxl
 */
public interface PlanetService extends BaseService<PlanetResp, PlanetDetailResp, PlanetQuery, PlanetReq> {}