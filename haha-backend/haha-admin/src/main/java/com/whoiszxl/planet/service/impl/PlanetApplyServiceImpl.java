package com.whoiszxl.planet.service.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.whoiszxl.starter.crud.service.impl.BaseServiceImpl;
import com.whoiszxl.planet.mapper.PlanetApplyMapper;
import com.whoiszxl.planet.model.entity.PlanetApplyDO;
import com.whoiszxl.planet.model.query.PlanetApplyQuery;
import com.whoiszxl.planet.model.req.PlanetApplyReq;
import com.whoiszxl.planet.model.resp.PlanetApplyDetailResp;
import com.whoiszxl.planet.model.resp.PlanetApplyResp;
import com.whoiszxl.planet.service.PlanetApplyService;

/**
 * 星球申请业务实现
 *
 * @author whoiszxl
 */
@Service
@RequiredArgsConstructor
public class PlanetApplyServiceImpl extends BaseServiceImpl<PlanetApplyMapper, PlanetApplyDO, PlanetApplyResp, PlanetApplyDetailResp, PlanetApplyQuery, PlanetApplyReq> implements PlanetApplyService {}