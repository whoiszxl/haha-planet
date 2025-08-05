package com.whoiszxl.planet.service;

import com.whoiszxl.starter.crud.service.BaseService;
import com.whoiszxl.planet.model.query.PlanetPostQuery;
import com.whoiszxl.planet.model.req.PlanetPostReq;
import com.whoiszxl.planet.model.resp.PlanetPostDetailResp;
import com.whoiszxl.planet.model.resp.PlanetPostResp;

/**
 * 星球帖子业务接口
 *
 * @author whoiszxl
 */
public interface PlanetPostService extends BaseService<PlanetPostResp, PlanetPostDetailResp, PlanetPostQuery, PlanetPostReq> {}