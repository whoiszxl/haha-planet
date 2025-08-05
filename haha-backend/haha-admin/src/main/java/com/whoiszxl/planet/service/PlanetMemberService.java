package com.whoiszxl.planet.service;

import com.whoiszxl.starter.crud.service.BaseService;
import com.whoiszxl.planet.model.query.PlanetMemberQuery;
import com.whoiszxl.planet.model.req.PlanetMemberReq;
import com.whoiszxl.planet.model.resp.PlanetMemberDetailResp;
import com.whoiszxl.planet.model.resp.PlanetMemberResp;

/**
 * 星球成员业务接口
 *
 * @author whoiszxl
 */
public interface PlanetMemberService extends BaseService<PlanetMemberResp, PlanetMemberDetailResp, PlanetMemberQuery, PlanetMemberReq> {}