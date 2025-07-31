package com.whoiszxl.admin.service;

import com.whoiszxl.admin.cqrs.command.DeptRequest;
import com.whoiszxl.admin.cqrs.query.DeptQuery;
import com.whoiszxl.admin.cqrs.response.DeptResponse;
import com.whoiszxl.admin.entity.Dept;
import com.whoiszxl.starter.crud.service.BaseService;
import com.whoiszxl.starter.mybatis.service.IService;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 系统菜单 服务类
 * </p>
 *
 * @author whoiszxl
 * @since 2024-05-21
 */
public interface IDeptService extends BaseService<DeptResponse, DeptResponse, DeptQuery, DeptRequest>, IService<Dept> {


    List<Dept> listChildren(Long id);
}
