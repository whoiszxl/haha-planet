package com.whoiszxl.planet.controller;

import com.whoiszxl.planet.service.PlanetNotificationService;
import com.whoiszxl.starter.crud.enums.Api;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

import com.whoiszxl.starter.crud.annotation.CrudRequestMapping;
import com.whoiszxl.starter.crud.controller.BaseController;
import com.whoiszxl.planet.model.query.PlanetNotificationQuery;
import com.whoiszxl.planet.model.req.PlanetNotificationReq;
import com.whoiszxl.planet.model.resp.PlanetNotificationDetailResp;
import com.whoiszxl.planet.model.resp.PlanetNotificationResp;

/**
 * 星球通知管理 API
 *
 * @author whoiszxl
 */
@Tag(name = "星球通知管理 API")
@RestController
@CrudRequestMapping(value = "/planet/planetNotification", api = {Api.PAGE, Api.GET, Api.ADD, Api.UPDATE, Api.DELETE, Api.EXPORT})
public class PlanetNotificationController extends BaseController<PlanetNotificationService, PlanetNotificationResp, PlanetNotificationDetailResp, PlanetNotificationQuery, PlanetNotificationReq> {}