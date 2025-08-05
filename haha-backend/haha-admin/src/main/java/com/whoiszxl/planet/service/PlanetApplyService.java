package com.whoiszxl.planet.service;

import com.whoiszxl.starter.crud.service.BaseService;
import com.whoiszxl.planet.model.query.PlanetApplyQuery;
import com.whoiszxl.planet.model.req.PlanetApplyReq;
import com.whoiszxl.planet.model.resp.PlanetApplyDetailResp;
import com.whoiszxl.planet.model.resp.PlanetApplyResp;

/**
 * 星球申请业务接口
 *
 * @author whoiszxl
 */
public interface PlanetApplyService extends BaseService<PlanetApplyResp, PlanetApplyDetailResp, PlanetApplyQuery, PlanetApplyReq> {}