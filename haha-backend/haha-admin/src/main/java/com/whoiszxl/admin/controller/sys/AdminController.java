package com.whoiszxl.admin.controller.sys;


import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.util.ReUtil;
import com.whoiszxl.admin.cqrs.command.AdminRequest;
import com.whoiszxl.admin.cqrs.command.AdminPasswordResetCommand;
import com.whoiszxl.admin.cqrs.command.RoleUpdateCommand;
import com.whoiszxl.admin.cqrs.query.AdminQuery;
import com.whoiszxl.admin.cqrs.response.AdminDetailResponse;
import com.whoiszxl.admin.service.IAdminService;
import com.whoiszxl.common.constants.RegexConstants;
import com.whoiszxl.common.utils.SecureUtils;
import com.whoiszxl.starter.core.utils.ExceptionUtils;
import com.whoiszxl.starter.core.utils.validate.ValidationUtils;
import com.whoiszxl.starter.crud.annotation.CrudRequestMapping;
import com.whoiszxl.starter.crud.controller.BaseController;
import com.whoiszxl.starter.crud.enums.Api;
import com.whoiszxl.starter.crud.utils.ValidateGroup;
import com.whoiszxl.starter.web.model.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 管理员表 前端控制器
 * </p>
 *
 * @author whoiszxl
 * @since 2024-05-21
 */
@Tag(name = "管理员 API")
@Validated
@RestController
@CrudRequestMapping(value = "/system/admin", api = {Api.PAGE, Api.GET, Api.ADD, Api.UPDATE, Api.DELETE, Api.EXPORT})
@RequiredArgsConstructor
public class AdminController extends BaseController<IAdminService, AdminDetailResponse, AdminDetailResponse, AdminQuery, AdminRequest> {


    @Override
    public R<Long> add(@Validated(ValidateGroup.Crud.Add.class) @RequestBody AdminRequest req) {
        String rawPassword = ExceptionUtils.exToNull(() ->
                SecureUtils.decryptByRsaPrivateKey(req.getPassword()));

        ValidationUtils.throwIfNull(rawPassword, "密码解密失败");
        ValidationUtils.throwIf(!ReUtil
                .isMatch(RegexConstants.PASSWORD, rawPassword), "密码长度为 8-32 个字符，支持大小写字母、数字、特殊字符，至少包含字母和数字");
        req.setPassword(rawPassword);
        return super.add(req);
    }

    @Operation(summary = "重置密码", description = "重置用户登录密码")
    @Parameter(name = "id", description = "ID", example = "1", in = ParameterIn.PATH)
    @SaCheckPermission("system:user:reset-password")
    @PatchMapping("/{id}/password")
    public R<Void> resetPassword(@Validated @RequestBody AdminPasswordResetCommand command, @PathVariable Long id) {
        String rawNewPassword = ExceptionUtils.exToNull(() ->
                SecureUtils.decryptByRsaPrivateKey(command.getNewPassword()));
        ValidationUtils.throwIfNull(rawNewPassword, "新密码解密失败");
        ValidationUtils.throwIf(!ReUtil
                .isMatch(RegexConstants.PASSWORD, rawNewPassword), "密码长度为 8-32 个字符，支持大小写字母、数字、特殊字符，至少包含字母和数字");
        command.setNewPassword(rawNewPassword);
        baseService.resetPassword(command, id);
        return R.ok("重置密码成功");
    }

    @Operation(summary = "分配角色", description = "为用户新增或移除角色")
    @Parameter(name = "id", description = "ID", example = "1", in = ParameterIn.PATH)
    @SaCheckPermission("system:user:updateRole")
    @PatchMapping("/{id}/role")
    public R<Void> updateRole(@Validated @RequestBody RoleUpdateCommand roleUpdateCommand, @PathVariable Long id) {
        baseService.updateRole(roleUpdateCommand, id);
        return R.ok("分配成功");
    }
}

