package com.whoiszxl.planet.controller;

import com.whoiszxl.starter.crud.enums.Api;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

import com.whoiszxl.starter.crud.annotation.CrudRequestMapping;
import com.whoiszxl.starter.crud.controller.BaseController;
import com.whoiszxl.planet.model.query.PlanetCategoryQuery;
import com.whoiszxl.planet.model.req.PlanetCategoryReq;
import com.whoiszxl.planet.model.resp.PlanetCategoryDetailResp;
import com.whoiszxl.planet.model.resp.PlanetCategoryResp;
import com.whoiszxl.planet.service.PlanetCategoryService;

/**
 * 星球分类管理 API
 *
 * @author whoiszxl
 */
@Tag(name = "星球分类管理 API")
@RestController
@CrudRequestMapping(value = "/whoiszxl/planetCategory", api = {Api.PAGE, Api.GET, Api.ADD, Api.UPDATE, Api.DELETE, Api.EXPORT, Api.TREE})
public class PlanetCategoryController extends BaseController<PlanetCategoryService, PlanetCategoryResp, PlanetCategoryDetailResp, PlanetCategoryQuery, PlanetCategoryReq> {}