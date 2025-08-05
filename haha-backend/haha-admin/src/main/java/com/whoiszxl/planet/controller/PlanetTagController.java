package com.whoiszxl.planet.controller;

import com.whoiszxl.starter.crud.enums.Api;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

import com.whoiszxl.starter.crud.annotation.CrudRequestMapping;
import com.whoiszxl.starter.crud.controller.BaseController;
import com.whoiszxl.planet.model.query.PlanetTagQuery;
import com.whoiszxl.planet.model.req.PlanetTagReq;
import com.whoiszxl.planet.model.resp.PlanetTagDetailResp;
import com.whoiszxl.planet.model.resp.PlanetTagResp;
import com.whoiszxl.planet.service.PlanetTagService;

/**
 * 星球标签管理 API
 *
 * @author whoiszxl
 */
@Tag(name = "星球标签管理 API")
@RestController
@CrudRequestMapping(value = "/whoiszxl/planetTag", api = {Api.PAGE, Api.GET, Api.ADD, Api.UPDATE, Api.DELETE, Api.EXPORT})
public class PlanetTagController extends BaseController<PlanetTagService, PlanetTagResp, PlanetTagDetailResp, PlanetTagQuery, PlanetTagReq> {}