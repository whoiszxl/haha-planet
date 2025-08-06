package com.whoiszxl.user.service;

import com.whoiszxl.starter.crud.service.BaseService;
import com.whoiszxl.user.model.query.UserClientQuery;
import com.whoiszxl.user.model.req.UserClientReq;
import com.whoiszxl.user.model.resp.UserClientDetailResp;
import com.whoiszxl.user.model.resp.UserClientResp;

/**
 * 用户客户端业务接口
 *
 * @author whoiszxl
 */
public interface UserClientService extends BaseService<UserClientResp, UserClientDetailResp, UserClientQuery, UserClientReq> {}