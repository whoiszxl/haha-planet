package com.whoiszxl.user.service.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.whoiszxl.starter.crud.service.impl.BaseServiceImpl;
import com.whoiszxl.user.mapper.UserTokenMapper;
import com.whoiszxl.user.model.entity.UserTokenDO;
import com.whoiszxl.user.model.query.UserTokenQuery;
import com.whoiszxl.user.model.req.UserTokenReq;
import com.whoiszxl.user.model.resp.UserTokenDetailResp;
import com.whoiszxl.user.model.resp.UserTokenResp;
import com.whoiszxl.user.service.UserTokenService;

/**
 * 用户令牌业务实现
 *
 * @author whoiszxl
 */
@Service
@RequiredArgsConstructor
public class UserTokenServiceImpl extends BaseServiceImpl<UserTokenMapper, UserTokenDO, UserTokenResp, UserTokenDetailResp, UserTokenQuery, UserTokenReq> implements UserTokenService {}