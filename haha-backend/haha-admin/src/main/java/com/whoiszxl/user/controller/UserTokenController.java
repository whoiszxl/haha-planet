package com.whoiszxl.user.controller;

import com.whoiszxl.starter.crud.annotation.CrudRequestMapping;
import com.whoiszxl.starter.crud.controller.BaseController;
import com.whoiszxl.starter.crud.enums.Api;
import com.whoiszxl.user.model.query.UserTokenQuery;
import com.whoiszxl.user.model.req.UserTokenReq;
import com.whoiszxl.user.model.resp.UserTokenDetailResp;
import com.whoiszxl.user.model.resp.UserTokenResp;
import com.whoiszxl.user.service.UserTokenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户令牌管理 API
 *
 * @author whoiszxl
 */
@Tag(name = "用户令牌管理 API")
@RestController
@CrudRequestMapping(value = "/user/userToken", api = {Api.PAGE, Api.GET, Api.ADD, Api.UPDATE, Api.DELETE, Api.EXPORT})
public class UserTokenController extends BaseController<UserTokenService, UserTokenResp, UserTokenDetailResp, UserTokenQuery, UserTokenReq> {}