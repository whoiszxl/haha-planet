package com.whoiszxl.admin.controller.common;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import com.whoiszxl.admin.cqrs.query.OnlineUserQuery;
import com.whoiszxl.admin.cqrs.response.OnlineUserResp;
import com.whoiszxl.admin.service.OnlineAdminService;
import com.whoiszxl.starter.core.utils.validate.CheckUtils;
import com.whoiszxl.starter.crud.model.PageQuery;
import com.whoiszxl.starter.crud.model.PageResponse;
import com.whoiszxl.starter.web.model.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author whoiszxl
 */
@Tag(name = "在线用户 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/monitor/online")
public class OnlineAdminController {

    private final OnlineAdminService baseService;

    @Operation(summary = "分页查询列表", description = "分页查询列表")
    @SaCheckPermission("monitor:online:list")
    @GetMapping
    public R<PageResponse<OnlineUserResp>> page(OnlineUserQuery query, @Validated PageQuery pageQuery) {
        return R.ok(baseService.page(query, pageQuery));
    }

    @Operation(summary = "强退在线用户", description = "强退在线用户")
    @Parameter(name = "token", description = "令牌", example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsb2dpblR5cGUiOiJsb2dpbiIsImxvZ2luSWQiOjEsInJuU3RyIjoiTUd6djdyOVFoeHEwdVFqdFAzV3M5YjVJRzh4YjZPSEUifQ.7q7U3ouoN7WPhH2kUEM7vPe5KF3G_qavSG-vRgIxKvE", in = ParameterIn.PATH)
    @SaCheckPermission("monitor:online:kick-out")
    @DeleteMapping("/{token}")
    public R<Void> kickout(@PathVariable String token) {
        String currentToken = StpUtil.getTokenValue();
        CheckUtils.throwIfEqual(token, currentToken, "不能强退自己");
        StpUtil.kickoutByTokenValue(token);
        return R.ok("强退成功");
    }
}
