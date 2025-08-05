package com.whoiszxl.planet.service.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.whoiszxl.starter.crud.service.impl.BaseServiceImpl;
import com.whoiszxl.planet.mapper.PlanetNotificationMapper;
import com.whoiszxl.planet.model.entity.PlanetNotificationDO;
import com.whoiszxl.planet.model.query.PlanetNotificationQuery;
import com.whoiszxl.planet.model.req.PlanetNotificationReq;
import com.whoiszxl.planet.model.resp.PlanetNotificationDetailResp;
import com.whoiszxl.planet.model.resp.PlanetNotificationResp;
import com.whoiszxl.planet.service.PlanetNotificationService;

/**
 * 星球通知业务实现
 *
 * @author whoiszxl
 */
@Service
@RequiredArgsConstructor
public class PlanetNotificationServiceImpl extends BaseServiceImpl<PlanetNotificationMapper, PlanetNotificationDO, PlanetNotificationResp, PlanetNotificationDetailResp, PlanetNotificationQuery, PlanetNotificationReq> implements PlanetNotificationService {}