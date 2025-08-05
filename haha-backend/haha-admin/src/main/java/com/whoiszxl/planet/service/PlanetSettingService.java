package com.whoiszxl.planet.service;

import com.whoiszxl.starter.crud.service.BaseService;
import com.whoiszxl.planet.model.query.PlanetSettingQuery;
import com.whoiszxl.planet.model.req.PlanetSettingReq;
import com.whoiszxl.planet.model.resp.PlanetSettingDetailResp;
import com.whoiszxl.planet.model.resp.PlanetSettingResp;

/**
 * 星球设置业务接口
 *
 * @author whoiszxl
 */
public interface PlanetSettingService extends BaseService<PlanetSettingResp, PlanetSettingDetailResp, PlanetSettingQuery, PlanetSettingReq> {}