package com.whoiszxl.planet.service;

import com.whoiszxl.starter.crud.service.BaseService;
import com.whoiszxl.planet.model.query.PlanetLikeQuery;
import com.whoiszxl.planet.model.req.PlanetLikeReq;
import com.whoiszxl.planet.model.resp.PlanetLikeDetailResp;
import com.whoiszxl.planet.model.resp.PlanetLikeResp;

/**
 * 点赞记录业务接口
 *
 * @author whoiszxl
 */
public interface PlanetLikeService extends BaseService<PlanetLikeResp, PlanetLikeDetailResp, PlanetLikeQuery, PlanetLikeReq> {}