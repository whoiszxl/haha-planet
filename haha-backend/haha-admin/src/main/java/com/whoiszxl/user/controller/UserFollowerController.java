package com.whoiszxl.user.controller;

import com.whoiszxl.starter.crud.enums.Api;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

import com.whoiszxl.starter.crud.annotation.CrudRequestMapping;
import com.whoiszxl.starter.crud.controller.BaseController;
import com.whoiszxl.user.model.query.UserFollowerQuery;
import com.whoiszxl.user.model.req.UserFollowerReq;
import com.whoiszxl.user.model.resp.UserFollowerDetailResp;
import com.whoiszxl.user.model.resp.UserFollowerResp;
import com.whoiszxl.user.service.UserFollowerService;

/**
 * 用户粉丝(关注我的人)管理 API
 *
 * @author whoiszxl
 */
@Tag(name = "用户粉丝(关注我的人)管理 API")
@RestController
@CrudRequestMapping(value = "/user/userFollower", api = {Api.PAGE, Api.GET, Api.ADD, Api.UPDATE, Api.DELETE, Api.EXPORT})
public class UserFollowerController extends BaseController<UserFollowerService, UserFollowerResp, UserFollowerDetailResp, UserFollowerQuery, UserFollowerReq> {}