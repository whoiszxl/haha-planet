package com.whoiszxl.user.service.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.whoiszxl.starter.crud.service.impl.BaseServiceImpl;
import com.whoiszxl.user.mapper.UserLevelMapper;
import com.whoiszxl.user.model.entity.UserLevelDO;
import com.whoiszxl.user.model.query.UserLevelQuery;
import com.whoiszxl.user.model.req.UserLevelReq;
import com.whoiszxl.user.model.resp.UserLevelDetailResp;
import com.whoiszxl.user.model.resp.UserLevelResp;
import com.whoiszxl.user.service.UserLevelService;

/**
 * 用户等级业务实现
 *
 * @author whoiszxl
 */
@Service
@RequiredArgsConstructor
public class UserLevelServiceImpl extends BaseServiceImpl<UserLevelMapper, UserLevelDO, UserLevelResp, UserLevelDetailResp, UserLevelQuery, UserLevelReq> implements UserLevelService {}