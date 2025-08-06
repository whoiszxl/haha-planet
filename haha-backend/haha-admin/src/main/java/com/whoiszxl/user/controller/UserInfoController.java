package com.whoiszxl.user.controller;

import com.whoiszxl.starter.crud.enums.Api;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

import com.whoiszxl.starter.crud.annotation.CrudRequestMapping;
import com.whoiszxl.starter.crud.controller.BaseController;
import com.whoiszxl.user.model.query.UserInfoQuery;
import com.whoiszxl.user.model.req.UserInfoReq;
import com.whoiszxl.user.model.resp.UserInfoDetailResp;
import com.whoiszxl.user.model.resp.UserInfoResp;
import com.whoiszxl.user.service.UserInfoService;

/**
 * 用户基础信息管理 API
 *
 * @author whoiszxl
 */
@Tag(name = "用户基础信息管理 API")
@RestController
@CrudRequestMapping(value = "/user/userInfo", api = {Api.PAGE, Api.GET, Api.ADD, Api.UPDATE, Api.DELETE, Api.EXPORT})
public class UserInfoController extends BaseController<UserInfoService, UserInfoResp, UserInfoDetailResp, UserInfoQuery, UserInfoReq> {}