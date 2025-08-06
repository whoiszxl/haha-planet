package com.whoiszxl.user.service;

import com.whoiszxl.starter.crud.service.BaseService;
import com.whoiszxl.user.model.query.UserSettingsQuery;
import com.whoiszxl.user.model.req.UserSettingsReq;
import com.whoiszxl.user.model.resp.UserSettingsDetailResp;
import com.whoiszxl.user.model.resp.UserSettingsResp;

/**
 * 用户设置业务接口
 *
 * @author whoiszxl
 */
public interface UserSettingsService extends BaseService<UserSettingsResp, UserSettingsDetailResp, UserSettingsQuery, UserSettingsReq> {}