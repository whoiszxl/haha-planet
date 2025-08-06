package com.whoiszxl.user.service;

import com.whoiszxl.starter.crud.service.BaseService;
import com.whoiszxl.user.model.query.UserLevelQuery;
import com.whoiszxl.user.model.req.UserLevelReq;
import com.whoiszxl.user.model.resp.UserLevelDetailResp;
import com.whoiszxl.user.model.resp.UserLevelResp;

/**
 * 用户等级业务接口
 *
 * @author whoiszxl
 */
public interface UserLevelService extends BaseService<UserLevelResp, UserLevelDetailResp, UserLevelQuery, UserLevelReq> {}