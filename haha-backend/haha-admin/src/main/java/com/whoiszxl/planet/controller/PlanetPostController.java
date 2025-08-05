package com.whoiszxl.planet.controller;

import com.whoiszxl.planet.service.PlanetPostService;
import com.whoiszxl.starter.crud.enums.Api;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

import com.whoiszxl.starter.crud.annotation.CrudRequestMapping;
import com.whoiszxl.starter.crud.controller.BaseController;
import com.whoiszxl.planet.model.query.PlanetPostQuery;
import com.whoiszxl.planet.model.req.PlanetPostReq;
import com.whoiszxl.planet.model.resp.PlanetPostDetailResp;
import com.whoiszxl.planet.model.resp.PlanetPostResp;

/**
 * 星球帖子管理 API
 *
 * @author whoiszxl
 */
@Tag(name = "星球帖子管理 API")
@RestController
@CrudRequestMapping(value = "/whoiszxl/planetPost", api = {Api.PAGE, Api.GET, Api.ADD, Api.UPDATE, Api.DELETE, Api.EXPORT})
public class PlanetPostController extends BaseController<PlanetPostService, PlanetPostResp, PlanetPostDetailResp, PlanetPostQuery, PlanetPostReq> {}