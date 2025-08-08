package com.whoiszxl.planet.controller;

import com.whoiszxl.starter.crud.enums.Api;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

import com.whoiszxl.starter.crud.annotation.CrudRequestMapping;
import com.whoiszxl.starter.crud.controller.BaseController;
import com.whoiszxl.planet.model.query.PlanetCommentQuery;
import com.whoiszxl.planet.model.req.PlanetCommentReq;
import com.whoiszxl.planet.model.resp.PlanetCommentDetailResp;
import com.whoiszxl.planet.model.resp.PlanetCommentResp;
import com.whoiszxl.planet.service.PlanetCommentService;

/**
 * 帖子评论管理 API
 *
 * @author whoiszxl
 */
@Tag(name = "帖子评论管理 API")
@RestController
@CrudRequestMapping(value = "/planet/planetComment", api = {Api.PAGE, Api.GET, Api.ADD, Api.UPDATE, Api.DELETE, Api.EXPORT})
public class PlanetCommentController extends BaseController<PlanetCommentService, PlanetCommentResp, PlanetCommentDetailResp, PlanetCommentQuery, PlanetCommentReq> {}