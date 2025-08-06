package com.whoiszxl.user.service;

import com.whoiszxl.starter.crud.service.BaseService;
import com.whoiszxl.user.model.query.UserTokenQuery;
import com.whoiszxl.user.model.req.UserTokenReq;
import com.whoiszxl.user.model.resp.UserTokenDetailResp;
import com.whoiszxl.user.model.resp.UserTokenResp;

/**
 * 用户令牌业务接口
 *
 * @author whoiszxl
 */
public interface UserTokenService extends BaseService<UserTokenResp, UserTokenDetailResp, UserTokenQuery, UserTokenReq> {}