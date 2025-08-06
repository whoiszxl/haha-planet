package com.whoiszxl.user.service.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.whoiszxl.starter.crud.service.impl.BaseServiceImpl;
import com.whoiszxl.user.mapper.UserClientMapper;
import com.whoiszxl.user.model.entity.UserClientDO;
import com.whoiszxl.user.model.query.UserClientQuery;
import com.whoiszxl.user.model.req.UserClientReq;
import com.whoiszxl.user.model.resp.UserClientDetailResp;
import com.whoiszxl.user.model.resp.UserClientResp;
import com.whoiszxl.user.service.UserClientService;

/**
 * 用户客户端业务实现
 *
 * @author whoiszxl
 */
@Service
@RequiredArgsConstructor
public class UserClientServiceImpl extends BaseServiceImpl<UserClientMapper, UserClientDO, UserClientResp, UserClientDetailResp, UserClientQuery, UserClientReq> implements UserClientService {}