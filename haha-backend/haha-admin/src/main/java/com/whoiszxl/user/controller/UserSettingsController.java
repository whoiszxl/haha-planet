package com.whoiszxl.user.controller;

import com.whoiszxl.starter.crud.enums.Api;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

import com.whoiszxl.starter.crud.annotation.CrudRequestMapping;
import com.whoiszxl.starter.crud.controller.BaseController;
import com.whoiszxl.user.model.query.UserSettingsQuery;
import com.whoiszxl.user.model.req.UserSettingsReq;
import com.whoiszxl.user.model.resp.UserSettingsDetailResp;
import com.whoiszxl.user.model.resp.UserSettingsResp;
import com.whoiszxl.user.service.UserSettingsService;

/**
 * 用户设置管理 API
 *
 * @author whoiszxl
 */
@Tag(name = "用户设置管理 API")
@RestController
@CrudRequestMapping(value = "/user/userSettings", api = {Api.PAGE, Api.GET, Api.ADD, Api.UPDATE, Api.DELETE, Api.EXPORT})
public class UserSettingsController extends BaseController<UserSettingsService, UserSettingsResp, UserSettingsDetailResp, UserSettingsQuery, UserSettingsReq> {}