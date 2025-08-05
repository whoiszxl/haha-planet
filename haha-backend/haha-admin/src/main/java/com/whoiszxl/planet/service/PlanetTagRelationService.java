package com.whoiszxl.planet.service;

import com.whoiszxl.starter.crud.service.BaseService;
import com.whoiszxl.planet.model.query.PlanetTagRelationQuery;
import com.whoiszxl.planet.model.req.PlanetTagRelationReq;
import com.whoiszxl.planet.model.resp.PlanetTagRelationDetailResp;
import com.whoiszxl.planet.model.resp.PlanetTagRelationResp;

/**
 * 星球标签关联业务接口
 *
 * @author whoiszxl
 */
public interface PlanetTagRelationService extends BaseService<PlanetTagRelationResp, PlanetTagRelationDetailResp, PlanetTagRelationQuery, PlanetTagRelationReq> {}