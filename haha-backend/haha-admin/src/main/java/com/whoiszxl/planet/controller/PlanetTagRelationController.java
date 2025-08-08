package com.whoiszxl.planet.controller;

import com.whoiszxl.starter.crud.enums.Api;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

import com.whoiszxl.starter.crud.annotation.CrudRequestMapping;
import com.whoiszxl.starter.crud.controller.BaseController;
import com.whoiszxl.planet.model.query.PlanetTagRelationQuery;
import com.whoiszxl.planet.model.req.PlanetTagRelationReq;
import com.whoiszxl.planet.model.resp.PlanetTagRelationDetailResp;
import com.whoiszxl.planet.model.resp.PlanetTagRelationResp;
import com.whoiszxl.planet.service.PlanetTagRelationService;

/**
 * 星球标签关联管理 API
 *
 * @author whoiszxl
 */
@Tag(name = "星球标签关联管理 API")
@RestController
@CrudRequestMapping(value = "/planet/planetTagRelation", api = {Api.PAGE, Api.GET, Api.ADD, Api.UPDATE, Api.DELETE, Api.EXPORT})
public class PlanetTagRelationController extends BaseController<PlanetTagRelationService, PlanetTagRelationResp, PlanetTagRelationDetailResp, PlanetTagRelationQuery, PlanetTagRelationReq> {}