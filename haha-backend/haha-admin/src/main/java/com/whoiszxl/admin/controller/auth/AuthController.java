package com.whoiszxl.admin.controller.auth;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import com.whoiszxl.admin.cqrs.command.PasswordLoginCommand;
import com.whoiszxl.admin.cqrs.response.AdminLoginResponse;
import com.whoiszxl.admin.cqrs.response.LoginResponse;
import com.whoiszxl.admin.cqrs.response.RouteResponse;
import com.whoiszxl.admin.entity.Admin;
import com.whoiszxl.admin.service.IAdminService;
import com.whoiszxl.admin.service.ILoginService;
import com.whoiszxl.cache.redisson.util.RedissonUtil;
import com.whoiszxl.captcha.graphic.service.GraphicCaptchaService;
import com.whoiszxl.common.model.LoginAdmin;
import com.whoiszxl.common.utils.LoginHelper;
import com.whoiszxl.common.utils.SecureUtils;
import com.whoiszxl.starter.core.utils.HahaBeanUtil;
import com.whoiszxl.starter.core.utils.ExceptionUtils;
import com.whoiszxl.starter.core.utils.validate.CheckUtils;
import com.whoiszxl.starter.log.core.annotation.Log;
import com.whoiszxl.starter.web.model.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author whoiszxl
 */
@Log(module = "登录")
@Tag(name = "管理员认证 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final ILoginService loginService;
    private final IAdminService adminService;
    private final RedissonUtil redissonUtil;
    private final GraphicCaptchaService graphicCaptchaService;

    @SaIgnore
    @Operation(summary = "账号密码登录", description = "根据账号密码进行登录")
    @PostMapping("/account")
    public R<LoginResponse> passwordLogin(@Validated @RequestBody PasswordLoginCommand command, HttpServletRequest request) {
        // 1.验证码校验
        boolean validate = graphicCaptchaService.validate(command.getUuid(), command.getCaptcha());
        CheckUtils.throwIf(!validate, "验证码错误");

        String truePassword = ExceptionUtils.exToNull(() -> SecureUtils.decryptByRsaPrivateKey(command.getPassword()));

        String token = loginService.login(command.getUsername(), truePassword, command.getGoogleCode());
        return R.ok(LoginResponse.builder().token(token).build());
    }

    @Operation(summary = "用户退出", description = "注销用户的当前登录")
    @Parameter(name = "Authorization", description = "令牌", required = true, example = "Bearer xxxx-xxxx-xxxx-xxxx", in = ParameterIn.HEADER)
    @PostMapping("/logout")
    public R<Object> logout() {
        Object loginId = StpUtil.getLoginId(-1L);
        StpUtil.logout();
        return R.ok(loginId);
    }

    @Log(ignore = true)
    @SaIgnore
    @Operation(summary = "获取管理员信息", description = "获取管理员信息")
    @GetMapping("/user/info")
    public R<AdminLoginResponse> getAdminInfo() {
        LoginAdmin loginAdmin = LoginHelper.getLoginAdmin();
        Admin admin = adminService.getById(loginAdmin.getId());
        AdminLoginResponse adminLoginResponse = HahaBeanUtil.copyProperties(admin, AdminLoginResponse.class);
        adminLoginResponse.setPermissions(loginAdmin.getPermissions());
        adminLoginResponse.setRoles(loginAdmin.getRoleCodes());
        return R.ok(adminLoginResponse);
    }

    @Log(ignore = true)
    @Operation(summary = "获取路由信息", description = "获取登录用户的路由信息")
    @GetMapping("/route")
    public R<List<RouteResponse>> listRoute() {
        return R.ok(loginService.buildRouteTree(LoginHelper.getAdminId()));
    }
}
