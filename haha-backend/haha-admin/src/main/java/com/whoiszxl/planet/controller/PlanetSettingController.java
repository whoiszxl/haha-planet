package com.whoiszxl.planet.controller;

import com.whoiszxl.starter.crud.enums.Api;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

import com.whoiszxl.starter.crud.annotation.CrudRequestMapping;
import com.whoiszxl.starter.crud.controller.BaseController;
import com.whoiszxl.planet.model.query.PlanetSettingQuery;
import com.whoiszxl.planet.model.req.PlanetSettingReq;
import com.whoiszxl.planet.model.resp.PlanetSettingDetailResp;
import com.whoiszxl.planet.model.resp.PlanetSettingResp;
import com.whoiszxl.planet.service.PlanetSettingService;

/**
 * 星球设置管理 API
 *
 * @author whoiszxl
 */
@Tag(name = "星球设置管理 API")
@RestController
@CrudRequestMapping(value = "/whoiszxl/planetSetting", api = {Api.PAGE, Api.GET, Api.ADD, Api.UPDATE, Api.DELETE, Api.EXPORT})
public class PlanetSettingController extends BaseController<PlanetSettingService, PlanetSettingResp, PlanetSettingDetailResp, PlanetSettingQuery, PlanetSettingReq> {}