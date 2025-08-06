package com.whoiszxl.user.service;

import com.whoiszxl.starter.crud.service.BaseService;
import com.whoiszxl.user.model.query.UserFollowingQuery;
import com.whoiszxl.user.model.req.UserFollowingReq;
import com.whoiszxl.user.model.resp.UserFollowingDetailResp;
import com.whoiszxl.user.model.resp.UserFollowingResp;

/**
 * 用户关注(我关注的人)业务接口
 *
 * @author whoiszxl
 */
public interface UserFollowingService extends BaseService<UserFollowingResp, UserFollowingDetailResp, UserFollowingQuery, UserFollowingReq> {}