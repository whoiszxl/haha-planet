package com.whoiszxl.planet.service.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.whoiszxl.starter.crud.service.impl.BaseServiceImpl;
import com.whoiszxl.planet.mapper.PlanetMapper;
import com.whoiszxl.planet.model.entity.PlanetDO;
import com.whoiszxl.planet.model.query.PlanetQuery;
import com.whoiszxl.planet.model.req.PlanetReq;
import com.whoiszxl.planet.model.resp.PlanetDetailResp;
import com.whoiszxl.planet.model.resp.PlanetResp;
import com.whoiszxl.planet.service.PlanetService;

/**
 * 星球业务实现
 *
 * @author whoiszxl
 */
@Service
@RequiredArgsConstructor
public class PlanetServiceImpl extends BaseServiceImpl<PlanetMapper, PlanetDO, PlanetResp, PlanetDetailResp, PlanetQuery, PlanetReq> implements PlanetService {}