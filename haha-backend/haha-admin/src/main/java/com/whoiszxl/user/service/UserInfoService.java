package com.whoiszxl.user.service;

import com.whoiszxl.starter.crud.service.BaseService;
import com.whoiszxl.user.model.query.UserInfoQuery;
import com.whoiszxl.user.model.req.UserInfoReq;
import com.whoiszxl.user.model.resp.UserInfoDetailResp;
import com.whoiszxl.user.model.resp.UserInfoResp;

/**
 * 用户基础信息业务接口
 *
 * @author whoiszxl
 */
public interface UserInfoService extends BaseService<UserInfoResp, UserInfoDetailResp, UserInfoQuery, UserInfoReq> {}