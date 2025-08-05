package com.whoiszxl.planet.service;

import com.whoiszxl.starter.crud.service.BaseService;
import com.whoiszxl.planet.model.query.PlanetInviteQuery;
import com.whoiszxl.planet.model.req.PlanetInviteReq;
import com.whoiszxl.planet.model.resp.PlanetInviteDetailResp;
import com.whoiszxl.planet.model.resp.PlanetInviteResp;

/**
 * 星球邀请业务接口
 *
 * @author whoiszxl
 */
public interface PlanetInviteService extends BaseService<PlanetInviteResp, PlanetInviteDetailResp, PlanetInviteQuery, PlanetInviteReq> {}