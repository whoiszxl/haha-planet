package com.whoiszxl.user.controller;

import com.whoiszxl.starter.crud.enums.Api;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

import com.whoiszxl.starter.crud.annotation.CrudRequestMapping;
import com.whoiszxl.starter.crud.controller.BaseController;
import com.whoiszxl.user.model.query.UserFollowingQuery;
import com.whoiszxl.user.model.req.UserFollowingReq;
import com.whoiszxl.user.model.resp.UserFollowingDetailResp;
import com.whoiszxl.user.model.resp.UserFollowingResp;
import com.whoiszxl.user.service.UserFollowingService;

/**
 * 用户关注(我关注的人)管理 API
 *
 * @author whoiszxl
 */
@Tag(name = "用户关注(我关注的人)管理 API")
@RestController
@CrudRequestMapping(value = "/user/userFollowing", api = {Api.PAGE, Api.GET, Api.ADD, Api.UPDATE, Api.DELETE, Api.EXPORT})
public class UserFollowingController extends BaseController<UserFollowingService, UserFollowingResp, UserFollowingDetailResp, UserFollowingQuery, UserFollowingReq> {}