package com.whoiszxl.planet.controller;

import com.whoiszxl.starter.crud.enums.Api;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

import com.whoiszxl.starter.crud.annotation.CrudRequestMapping;
import com.whoiszxl.starter.crud.controller.BaseController;
import com.whoiszxl.planet.model.query.PlanetLikeQuery;
import com.whoiszxl.planet.model.req.PlanetLikeReq;
import com.whoiszxl.planet.model.resp.PlanetLikeDetailResp;
import com.whoiszxl.planet.model.resp.PlanetLikeResp;
import com.whoiszxl.planet.service.PlanetLikeService;

/**
 * 点赞记录管理 API
 *
 * @author whoiszxl
 */
@Tag(name = "点赞记录管理 API")
@RestController
@CrudRequestMapping(value = "/whoiszxl/planetLike", api = {Api.PAGE, Api.GET, Api.ADD, Api.UPDATE, Api.DELETE, Api.EXPORT})
public class PlanetLikeController extends BaseController<PlanetLikeService, PlanetLikeResp, PlanetLikeDetailResp, PlanetLikeQuery, PlanetLikeReq> {}