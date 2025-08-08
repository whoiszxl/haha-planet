package com.whoiszxl.planet.controller;

import com.whoiszxl.starter.crud.enums.Api;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

import com.whoiszxl.starter.crud.annotation.CrudRequestMapping;
import com.whoiszxl.starter.crud.controller.BaseController;
import com.whoiszxl.planet.model.query.PlanetMemberQuery;
import com.whoiszxl.planet.model.req.PlanetMemberReq;
import com.whoiszxl.planet.model.resp.PlanetMemberDetailResp;
import com.whoiszxl.planet.model.resp.PlanetMemberResp;
import com.whoiszxl.planet.service.PlanetMemberService;

/**
 * 星球成员管理 API
 *
 * @author whoiszxl
 */
@Tag(name = "星球成员管理 API")
@RestController
@CrudRequestMapping(value = "/planet/planetMember", api = {Api.PAGE, Api.GET, Api.ADD, Api.UPDATE, Api.DELETE, Api.EXPORT})
public class PlanetMemberController extends BaseController<PlanetMemberService, PlanetMemberResp, PlanetMemberDetailResp, PlanetMemberQuery, PlanetMemberReq> {}