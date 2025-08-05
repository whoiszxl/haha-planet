package com.whoiszxl.planet.service.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.whoiszxl.starter.crud.service.impl.BaseServiceImpl;
import com.whoiszxl.planet.mapper.PlanetInviteMapper;
import com.whoiszxl.planet.model.entity.PlanetInviteDO;
import com.whoiszxl.planet.model.query.PlanetInviteQuery;
import com.whoiszxl.planet.model.req.PlanetInviteReq;
import com.whoiszxl.planet.model.resp.PlanetInviteDetailResp;
import com.whoiszxl.planet.model.resp.PlanetInviteResp;
import com.whoiszxl.planet.service.PlanetInviteService;

/**
 * 星球邀请业务实现
 *
 * @author whoiszxl
 */
@Service
@RequiredArgsConstructor
public class PlanetInviteServiceImpl extends BaseServiceImpl<PlanetInviteMapper, PlanetInviteDO, PlanetInviteResp, PlanetInviteDetailResp, PlanetInviteQuery, PlanetInviteReq> implements PlanetInviteService {}