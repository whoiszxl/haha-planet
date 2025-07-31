package com.whoiszxl.admin.controller.sys;


import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ReflectUtil;
import com.whoiszxl.admin.cqrs.command.DeptRequest;
import com.whoiszxl.admin.cqrs.query.DeptQuery;
import com.whoiszxl.admin.cqrs.response.DeptResponse;
import com.whoiszxl.admin.entity.Dept;
import com.whoiszxl.admin.service.IDeptService;
import com.whoiszxl.starter.core.utils.ReflectUtils;
import com.whoiszxl.starter.crud.annotation.CrudRequestMapping;
import com.whoiszxl.starter.crud.controller.BaseController;
import com.whoiszxl.starter.crud.enums.Api;
import com.whoiszxl.starter.crud.utils.TreeUtils;
import com.whoiszxl.starter.web.model.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
import java.util.List;

/**
 * <p>
 * 系统部门 前端控制器
 * </p>
 *
 * @author whoiszxl
 * @since 2024-05-21
 */
@Tag(name = "系统部门 API")
@RestController
@CrudRequestMapping(value = "/system/dept", api = {Api.TREE, Api.GET, Api.ADD, Api.UPDATE, Api.DELETE, Api.EXPORT})
@RequiredArgsConstructor
public class DeptController extends BaseController<IDeptService, DeptResponse, DeptResponse, DeptQuery, DeptRequest> {

}

