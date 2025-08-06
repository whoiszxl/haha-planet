package com.whoiszxl.user.controller;

import com.whoiszxl.starter.crud.enums.Api;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

import com.whoiszxl.starter.crud.annotation.CrudRequestMapping;
import com.whoiszxl.starter.crud.controller.BaseController;
import com.whoiszxl.user.model.query.UserLevelQuery;
import com.whoiszxl.user.model.req.UserLevelReq;
import com.whoiszxl.user.model.resp.UserLevelDetailResp;
import com.whoiszxl.user.model.resp.UserLevelResp;
import com.whoiszxl.user.service.UserLevelService;

/**
 * 用户等级管理 API
 *
 * @author whoiszxl
 */
@Tag(name = "用户等级管理 API")
@RestController
@CrudRequestMapping(value = "/user/userLevel", api = {Api.PAGE, Api.GET, Api.ADD, Api.UPDATE, Api.DELETE, Api.EXPORT})
public class UserLevelController extends BaseController<UserLevelService, UserLevelResp, UserLevelDetailResp, UserLevelQuery, UserLevelReq> {}