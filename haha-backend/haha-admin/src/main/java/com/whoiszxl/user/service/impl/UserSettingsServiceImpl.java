package com.whoiszxl.user.service.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.whoiszxl.starter.crud.service.impl.BaseServiceImpl;
import com.whoiszxl.user.mapper.UserSettingsMapper;
import com.whoiszxl.user.model.entity.UserSettingsDO;
import com.whoiszxl.user.model.query.UserSettingsQuery;
import com.whoiszxl.user.model.req.UserSettingsReq;
import com.whoiszxl.user.model.resp.UserSettingsDetailResp;
import com.whoiszxl.user.model.resp.UserSettingsResp;
import com.whoiszxl.user.service.UserSettingsService;

/**
 * 用户设置业务实现
 *
 * @author whoiszxl
 */
@Service
@RequiredArgsConstructor
public class UserSettingsServiceImpl extends BaseServiceImpl<UserSettingsMapper, UserSettingsDO, UserSettingsResp, UserSettingsDetailResp, UserSettingsQuery, UserSettingsReq> implements UserSettingsService {}