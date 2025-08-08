package com.whoiszxl.planet.controller;

import com.whoiszxl.starter.crud.enums.Api;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

import com.whoiszxl.starter.crud.annotation.CrudRequestMapping;
import com.whoiszxl.starter.crud.controller.BaseController;
import com.whoiszxl.planet.model.query.PlanetInviteQuery;
import com.whoiszxl.planet.model.req.PlanetInviteReq;
import com.whoiszxl.planet.model.resp.PlanetInviteDetailResp;
import com.whoiszxl.planet.model.resp.PlanetInviteResp;
import com.whoiszxl.planet.service.PlanetInviteService;

/**
 * 星球邀请管理 API
 *
 * @author whoiszxl
 */
@Tag(name = "星球邀请管理 API")
@RestController
@CrudRequestMapping(value = "/planet/planetInvite", api = {Api.PAGE, Api.GET, Api.ADD, Api.UPDATE, Api.DELETE, Api.EXPORT})
public class PlanetInviteController extends BaseController<PlanetInviteService, PlanetInviteResp, PlanetInviteDetailResp, PlanetInviteQuery, PlanetInviteReq> {}