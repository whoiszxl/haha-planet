package com.whoiszxl.planet.service.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.whoiszxl.starter.crud.service.impl.BaseServiceImpl;
import com.whoiszxl.planet.mapper.PlanetSettingMapper;
import com.whoiszxl.planet.model.entity.PlanetSettingDO;
import com.whoiszxl.planet.model.query.PlanetSettingQuery;
import com.whoiszxl.planet.model.req.PlanetSettingReq;
import com.whoiszxl.planet.model.resp.PlanetSettingDetailResp;
import com.whoiszxl.planet.model.resp.PlanetSettingResp;
import com.whoiszxl.planet.service.PlanetSettingService;

/**
 * 星球设置业务实现
 *
 * @author whoiszxl
 */
@Service
@RequiredArgsConstructor
public class PlanetSettingServiceImpl extends BaseServiceImpl<PlanetSettingMapper, PlanetSettingDO, PlanetSettingResp, PlanetSettingDetailResp, PlanetSettingQuery, PlanetSettingReq> implements PlanetSettingService {}