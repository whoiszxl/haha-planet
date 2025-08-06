package com.whoiszxl.user.service;

import com.whoiszxl.starter.crud.service.BaseService;
import com.whoiszxl.user.model.query.PointsRecordQuery;
import com.whoiszxl.user.model.req.PointsRecordReq;
import com.whoiszxl.user.model.resp.PointsRecordDetailResp;
import com.whoiszxl.user.model.resp.PointsRecordResp;

/**
 * 积分记录业务接口
 *
 * @author whoiszxl
 */
public interface PointsRecordService extends BaseService<PointsRecordResp, PointsRecordDetailResp, PointsRecordQuery, PointsRecordReq> {}