package com.whoiszxl.planet.controller;

import com.whoiszxl.starter.crud.enums.Api;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

import com.whoiszxl.starter.crud.annotation.CrudRequestMapping;
import com.whoiszxl.starter.crud.controller.BaseController;
import com.whoiszxl.planet.model.query.PlanetCollectQuery;
import com.whoiszxl.planet.model.req.PlanetCollectReq;
import com.whoiszxl.planet.model.resp.PlanetCollectDetailResp;
import com.whoiszxl.planet.model.resp.PlanetCollectResp;
import com.whoiszxl.planet.service.PlanetCollectService;

/**
 * 收藏记录管理 API
 *
 * @author whoiszxl
 */
@Tag(name = "收藏记录管理 API")
@RestController
@CrudRequestMapping(value = "/planet/planetCollect", api = {Api.PAGE, Api.GET, Api.ADD, Api.UPDATE, Api.DELETE, Api.EXPORT})
public class PlanetCollectController extends BaseController<PlanetCollectService, PlanetCollectResp, PlanetCollectDetailResp, PlanetCollectQuery, PlanetCollectReq> {}