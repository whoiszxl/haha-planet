package com.whoiszxl.planet.service.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.whoiszxl.starter.crud.service.impl.BaseServiceImpl;
import com.whoiszxl.planet.mapper.PlanetLikeMapper;
import com.whoiszxl.planet.model.entity.PlanetLikeDO;
import com.whoiszxl.planet.model.query.PlanetLikeQuery;
import com.whoiszxl.planet.model.req.PlanetLikeReq;
import com.whoiszxl.planet.model.resp.PlanetLikeDetailResp;
import com.whoiszxl.planet.model.resp.PlanetLikeResp;
import com.whoiszxl.planet.service.PlanetLikeService;

/**
 * 点赞记录业务实现
 *
 * @author whoiszxl
 */
@Service
@RequiredArgsConstructor
public class PlanetLikeServiceImpl extends BaseServiceImpl<PlanetLikeMapper, PlanetLikeDO, PlanetLikeResp, PlanetLikeDetailResp, PlanetLikeQuery, PlanetLikeReq> implements PlanetLikeService {}