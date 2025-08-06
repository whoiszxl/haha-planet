package com.whoiszxl.user.service.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.whoiszxl.starter.crud.service.impl.BaseServiceImpl;
import com.whoiszxl.user.mapper.UserFollowerMapper;
import com.whoiszxl.user.model.entity.UserFollowerDO;
import com.whoiszxl.user.model.query.UserFollowerQuery;
import com.whoiszxl.user.model.req.UserFollowerReq;
import com.whoiszxl.user.model.resp.UserFollowerDetailResp;
import com.whoiszxl.user.model.resp.UserFollowerResp;
import com.whoiszxl.user.service.UserFollowerService;

/**
 * 用户粉丝(关注我的人)业务实现
 *
 * @author whoiszxl
 */
@Service
@RequiredArgsConstructor
public class UserFollowerServiceImpl extends BaseServiceImpl<UserFollowerMapper, UserFollowerDO, UserFollowerResp, UserFollowerDetailResp, UserFollowerQuery, UserFollowerReq> implements UserFollowerService {}