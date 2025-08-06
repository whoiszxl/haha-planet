package com.whoiszxl.user.controller;

import com.whoiszxl.starter.crud.enums.Api;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

import com.whoiszxl.starter.crud.annotation.CrudRequestMapping;
import com.whoiszxl.starter.crud.controller.BaseController;
import com.whoiszxl.user.model.query.PointsRecordQuery;
import com.whoiszxl.user.model.req.PointsRecordReq;
import com.whoiszxl.user.model.resp.PointsRecordDetailResp;
import com.whoiszxl.user.model.resp.PointsRecordResp;
import com.whoiszxl.user.service.PointsRecordService;

/**
 * 积分记录管理 API
 *
 * @author whoiszxl
 */
@Tag(name = "积分记录管理 API")
@RestController
@CrudRequestMapping(value = "/user/pointsRecord", api = {Api.PAGE, Api.GET, Api.ADD, Api.UPDATE, Api.DELETE, Api.EXPORT})
public class PointsRecordController extends BaseController<PointsRecordService, PointsRecordResp, PointsRecordDetailResp, PointsRecordQuery, PointsRecordReq> {}