package com.whoiszxl.planet.service.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.whoiszxl.starter.crud.service.impl.BaseServiceImpl;
import com.whoiszxl.planet.mapper.PlanetPostMapper;
import com.whoiszxl.planet.model.entity.PlanetPostDO;
import com.whoiszxl.planet.model.query.PlanetPostQuery;
import com.whoiszxl.planet.model.req.PlanetPostReq;
import com.whoiszxl.planet.model.resp.PlanetPostDetailResp;
import com.whoiszxl.planet.model.resp.PlanetPostResp;
import com.whoiszxl.planet.service.PlanetPostService;

/**
 * 星球帖子业务实现
 *
 * @author whoiszxl
 */
@Service
@RequiredArgsConstructor
public class PlanetPostServiceImpl extends BaseServiceImpl<PlanetPostMapper, PlanetPostDO, PlanetPostResp, PlanetPostDetailResp, PlanetPostQuery, PlanetPostReq> implements PlanetPostService {}