package com.whoiszxl.planet.service;

import com.whoiszxl.starter.crud.service.BaseService;
import com.whoiszxl.planet.model.query.PlanetNotificationQuery;
import com.whoiszxl.planet.model.req.PlanetNotificationReq;
import com.whoiszxl.planet.model.resp.PlanetNotificationDetailResp;
import com.whoiszxl.planet.model.resp.PlanetNotificationResp;

/**
 * 星球通知业务接口
 *
 * @author whoiszxl
 */
public interface PlanetNotificationService extends BaseService<PlanetNotificationResp, PlanetNotificationDetailResp, PlanetNotificationQuery, PlanetNotificationReq> {}