package com.whoiszxl.planet.service;

import com.whoiszxl.starter.crud.service.BaseService;
import com.whoiszxl.planet.model.query.PlanetCollectQuery;
import com.whoiszxl.planet.model.req.PlanetCollectReq;
import com.whoiszxl.planet.model.resp.PlanetCollectDetailResp;
import com.whoiszxl.planet.model.resp.PlanetCollectResp;

/**
 * 收藏记录业务接口
 *
 * @author whoiszxl
 */
public interface PlanetCollectService extends BaseService<PlanetCollectResp, PlanetCollectDetailResp, PlanetCollectQuery, PlanetCollectReq> {}