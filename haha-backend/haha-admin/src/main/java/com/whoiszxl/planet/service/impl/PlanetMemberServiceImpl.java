package com.whoiszxl.planet.service.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.whoiszxl.starter.crud.service.impl.BaseServiceImpl;
import com.whoiszxl.planet.mapper.PlanetMemberMapper;
import com.whoiszxl.planet.model.entity.PlanetMemberDO;
import com.whoiszxl.planet.model.query.PlanetMemberQuery;
import com.whoiszxl.planet.model.req.PlanetMemberReq;
import com.whoiszxl.planet.model.resp.PlanetMemberDetailResp;
import com.whoiszxl.planet.model.resp.PlanetMemberResp;
import com.whoiszxl.planet.service.PlanetMemberService;

/**
 * 星球成员业务实现
 *
 * @author whoiszxl
 */
@Service
@RequiredArgsConstructor
public class PlanetMemberServiceImpl extends BaseServiceImpl<PlanetMemberMapper, PlanetMemberDO, PlanetMemberResp, PlanetMemberDetailResp, PlanetMemberQuery, PlanetMemberReq> implements PlanetMemberService {}