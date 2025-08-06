package com.whoiszxl.user.service.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.whoiszxl.starter.crud.service.impl.BaseServiceImpl;
import com.whoiszxl.user.mapper.UserInfoMapper;
import com.whoiszxl.user.model.entity.UserInfoDO;
import com.whoiszxl.user.model.query.UserInfoQuery;
import com.whoiszxl.user.model.req.UserInfoReq;
import com.whoiszxl.user.model.resp.UserInfoDetailResp;
import com.whoiszxl.user.model.resp.UserInfoResp;
import com.whoiszxl.user.service.UserInfoService;

/**
 * 用户基础信息业务实现
 *
 * @author whoiszxl
 */
@Service
@RequiredArgsConstructor
public class UserInfoServiceImpl extends BaseServiceImpl<UserInfoMapper, UserInfoDO, UserInfoResp, UserInfoDetailResp, UserInfoQuery, UserInfoReq> implements UserInfoService {}