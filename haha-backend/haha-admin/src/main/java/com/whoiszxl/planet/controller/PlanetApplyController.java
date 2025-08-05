package com.whoiszxl.planet.controller;

import com.whoiszxl.starter.crud.enums.Api;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

import com.whoiszxl.starter.crud.annotation.CrudRequestMapping;
import com.whoiszxl.starter.crud.controller.BaseController;
import com.whoiszxl.planet.model.query.PlanetApplyQuery;
import com.whoiszxl.planet.model.req.PlanetApplyReq;
import com.whoiszxl.planet.model.resp.PlanetApplyDetailResp;
import com.whoiszxl.planet.model.resp.PlanetApplyResp;
import com.whoiszxl.planet.service.PlanetApplyService;

/**
 * 星球申请管理 API
 *
 * @author whoiszxl
 */
@Tag(name = "星球申请管理 API")
@RestController
@CrudRequestMapping(value = "/planet/planetApply", api = {Api.PAGE, Api.GET, Api.ADD, Api.UPDATE, Api.DELETE, Api.EXPORT})
public class PlanetApplyController extends BaseController<PlanetApplyService, PlanetApplyResp, PlanetApplyDetailResp, PlanetApplyQuery, PlanetApplyReq> {}