package com.whoiszxl.planet.service.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.whoiszxl.starter.crud.service.impl.BaseServiceImpl;
import com.whoiszxl.planet.mapper.PlanetCommentMapper;
import com.whoiszxl.planet.model.entity.PlanetCommentDO;
import com.whoiszxl.planet.model.query.PlanetCommentQuery;
import com.whoiszxl.planet.model.req.PlanetCommentReq;
import com.whoiszxl.planet.model.resp.PlanetCommentDetailResp;
import com.whoiszxl.planet.model.resp.PlanetCommentResp;
import com.whoiszxl.planet.service.PlanetCommentService;

/**
 * 帖子评论业务实现
 *
 * @author whoiszxl
 */
@Service
@RequiredArgsConstructor
public class PlanetCommentServiceImpl extends BaseServiceImpl<PlanetCommentMapper, PlanetCommentDO, PlanetCommentResp, PlanetCommentDetailResp, PlanetCommentQuery, PlanetCommentReq> implements PlanetCommentService {}