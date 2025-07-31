package com.whoiszxl.admin.controller.sys;


import com.whoiszxl.admin.cqrs.command.RoleReq;
import com.whoiszxl.admin.cqrs.query.RoleQuery;
import com.whoiszxl.admin.cqrs.response.RoleDetailResponse;
import com.whoiszxl.admin.cqrs.response.RoleResponse;
import com.whoiszxl.admin.service.IRoleService;
import com.whoiszxl.starter.crud.annotation.CrudRequestMapping;
import com.whoiszxl.starter.crud.controller.BaseController;
import com.whoiszxl.starter.crud.enums.Api;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 角色 前端控制器
 * </p>
 *
 * @author whoiszxl
 * @since 2024-05-21
 */
@Tag(name = "角色 API")
@RestController
@RequiredArgsConstructor
@CrudRequestMapping(value = "/system/role", api = {Api.PAGE, Api.GET, Api.ADD, Api.UPDATE, Api.DELETE})
public class RoleController extends BaseController<IRoleService, RoleResponse, RoleDetailResponse, RoleQuery, RoleReq> {

}

