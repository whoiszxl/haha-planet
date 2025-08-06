package com.whoiszxl.user.service;

import com.whoiszxl.starter.crud.service.BaseService;
import com.whoiszxl.user.model.query.UserFollowerQuery;
import com.whoiszxl.user.model.req.UserFollowerReq;
import com.whoiszxl.user.model.resp.UserFollowerDetailResp;
import com.whoiszxl.user.model.resp.UserFollowerResp;

/**
 * 用户粉丝(关注我的人)业务接口
 *
 * @author whoiszxl
 */
public interface UserFollowerService extends BaseService<UserFollowerResp, UserFollowerDetailResp, UserFollowerQuery, UserFollowerReq> {}