package com.whoiszxl.user.controller;

import com.whoiszxl.starter.crud.enums.Api;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

import com.whoiszxl.starter.crud.annotation.CrudRequestMapping;
import com.whoiszxl.starter.crud.controller.BaseController;
import com.whoiszxl.user.model.query.UserClientQuery;
import com.whoiszxl.user.model.req.UserClientReq;
import com.whoiszxl.user.model.resp.UserClientDetailResp;
import com.whoiszxl.user.model.resp.UserClientResp;
import com.whoiszxl.user.service.UserClientService;

/**
 * 用户客户端管理 API
 *
 * @author whoiszxl
 */
@Tag(name = "用户客户端管理 API")
@RestController
@CrudRequestMapping(value = "/user/userClient", api = {Api.PAGE, Api.GET, Api.ADD, Api.UPDATE, Api.DELETE, Api.EXPORT})
public class UserClientController extends BaseController<UserClientService, UserClientResp, UserClientDetailResp, UserClientQuery, UserClientReq> {}