package com.whoiszxl.planet.service.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.whoiszxl.starter.crud.service.impl.BaseServiceImpl;
import com.whoiszxl.planet.mapper.PlanetTagRelationMapper;
import com.whoiszxl.planet.model.entity.PlanetTagRelationDO;
import com.whoiszxl.planet.model.query.PlanetTagRelationQuery;
import com.whoiszxl.planet.model.req.PlanetTagRelationReq;
import com.whoiszxl.planet.model.resp.PlanetTagRelationDetailResp;
import com.whoiszxl.planet.model.resp.PlanetTagRelationResp;
import com.whoiszxl.planet.service.PlanetTagRelationService;

/**
 * 星球标签关联业务实现
 *
 * @author whoiszxl
 */
@Service
@RequiredArgsConstructor
public class PlanetTagRelationServiceImpl extends BaseServiceImpl<PlanetTagRelationMapper, PlanetTagRelationDO, PlanetTagRelationResp, PlanetTagRelationDetailResp, PlanetTagRelationQuery, PlanetTagRelationReq> implements PlanetTagRelationService {}