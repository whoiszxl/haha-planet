package com.whoiszxl.planet.service.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.whoiszxl.starter.crud.service.impl.BaseServiceImpl;
import com.whoiszxl.planet.mapper.PlanetCollectMapper;
import com.whoiszxl.planet.model.entity.PlanetCollectDO;
import com.whoiszxl.planet.model.query.PlanetCollectQuery;
import com.whoiszxl.planet.model.req.PlanetCollectReq;
import com.whoiszxl.planet.model.resp.PlanetCollectDetailResp;
import com.whoiszxl.planet.model.resp.PlanetCollectResp;
import com.whoiszxl.planet.service.PlanetCollectService;

/**
 * 收藏记录业务实现
 *
 * @author whoiszxl
 */
@Service
@RequiredArgsConstructor
public class PlanetCollectServiceImpl extends BaseServiceImpl<PlanetCollectMapper, PlanetCollectDO, PlanetCollectResp, PlanetCollectDetailResp, PlanetCollectQuery, PlanetCollectReq> implements PlanetCollectService {}