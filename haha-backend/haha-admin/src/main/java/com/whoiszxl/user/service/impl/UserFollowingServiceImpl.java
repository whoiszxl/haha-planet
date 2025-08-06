package com.whoiszxl.user.service.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.whoiszxl.starter.crud.service.impl.BaseServiceImpl;
import com.whoiszxl.user.mapper.UserFollowingMapper;
import com.whoiszxl.user.model.entity.UserFollowingDO;
import com.whoiszxl.user.model.query.UserFollowingQuery;
import com.whoiszxl.user.model.req.UserFollowingReq;
import com.whoiszxl.user.model.resp.UserFollowingDetailResp;
import com.whoiszxl.user.model.resp.UserFollowingResp;
import com.whoiszxl.user.service.UserFollowingService;

/**
 * 用户关注(我关注的人)业务实现
 *
 * @author whoiszxl
 */
@Service
@RequiredArgsConstructor
public class UserFollowingServiceImpl extends BaseServiceImpl<UserFollowingMapper, UserFollowingDO, UserFollowingResp, UserFollowingDetailResp, UserFollowingQuery, UserFollowingReq> implements UserFollowingService {}