package com.whoiszxl.planet.service;

import com.whoiszxl.starter.crud.service.BaseService;
import com.whoiszxl.planet.model.query.PlanetCommentQuery;
import com.whoiszxl.planet.model.req.PlanetCommentReq;
import com.whoiszxl.planet.model.resp.PlanetCommentDetailResp;
import com.whoiszxl.planet.model.resp.PlanetCommentResp;

/**
 * 帖子评论业务接口
 *
 * @author whoiszxl
 */
public interface PlanetCommentService extends BaseService<PlanetCommentResp, PlanetCommentDetailResp, PlanetCommentQuery, PlanetCommentReq> {}