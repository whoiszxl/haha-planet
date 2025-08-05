package com.whoiszxl.planet.controller;

import com.whoiszxl.starter.crud.enums.Api;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

import com.whoiszxl.starter.crud.annotation.CrudRequestMapping;
import com.whoiszxl.starter.crud.controller.BaseController;
import com.whoiszxl.planet.model.query.PlanetQuery;
import com.whoiszxl.planet.model.req.PlanetReq;
import com.whoiszxl.planet.model.resp.PlanetDetailResp;
import com.whoiszxl.planet.model.resp.PlanetResp;
import com.whoiszxl.planet.service.PlanetService;

/**
 * 星球管理 API
 *
 * @author whoiszxl
 */
@Tag(name = "星球管理 API")
@RestController
@CrudRequestMapping(value = "/whoiszxl/planet", api = {Api.PAGE, Api.GET, Api.ADD, Api.UPDATE, Api.DELETE, Api.EXPORT})
public class PlanetController extends BaseController<PlanetService, PlanetResp, PlanetDetailResp, PlanetQuery, PlanetReq> {}