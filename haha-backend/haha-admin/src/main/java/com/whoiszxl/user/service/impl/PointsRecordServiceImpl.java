package com.whoiszxl.user.service.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.whoiszxl.starter.crud.service.impl.BaseServiceImpl;
import com.whoiszxl.user.mapper.PointsRecordMapper;
import com.whoiszxl.user.model.entity.PointsRecordDO;
import com.whoiszxl.user.model.query.PointsRecordQuery;
import com.whoiszxl.user.model.req.PointsRecordReq;
import com.whoiszxl.user.model.resp.PointsRecordDetailResp;
import com.whoiszxl.user.model.resp.PointsRecordResp;
import com.whoiszxl.user.service.PointsRecordService;

/**
 * 积分记录业务实现
 *
 * @author whoiszxl
 */
@Service
@RequiredArgsConstructor
public class PointsRecordServiceImpl extends BaseServiceImpl<PointsRecordMapper, PointsRecordDO, PointsRecordResp, PointsRecordDetailResp, PointsRecordQuery, PointsRecordReq> implements PointsRecordService {}