package com.whoiszxl.planet.service.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.whoiszxl.starter.crud.service.impl.BaseServiceImpl;
import com.whoiszxl.planet.mapper.PlanetTagMapper;
import com.whoiszxl.planet.model.entity.PlanetTagDO;
import com.whoiszxl.planet.model.query.PlanetTagQuery;
import com.whoiszxl.planet.model.req.PlanetTagReq;
import com.whoiszxl.planet.model.resp.PlanetTagDetailResp;
import com.whoiszxl.planet.model.resp.PlanetTagResp;
import com.whoiszxl.planet.service.PlanetTagService;

/**
 * 星球标签业务实现
 *
 * @author whoiszxl
 */
@Service
@RequiredArgsConstructor
public class PlanetTagServiceImpl extends BaseServiceImpl<PlanetTagMapper, PlanetTagDO, PlanetTagResp, PlanetTagDetailResp, PlanetTagQuery, PlanetTagReq> implements PlanetTagService {}