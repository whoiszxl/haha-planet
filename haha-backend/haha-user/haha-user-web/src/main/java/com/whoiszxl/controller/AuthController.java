package com.whoiszxl.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.whoiszxl.common.context.UserContext;
import com.whoiszxl.common.context.UserContextHolder;
import com.whoiszxl.model.command.LoginCommand;
import com.whoiszxl.model.response.LoginResponse;
import com.whoiszxl.model.response.SocialAuthAuthorizeResp;
import com.whoiszxl.model.response.UserInfoResponse;
import com.whoiszxl.service.AuthService;
import com.whoiszxl.service.UserInfoService;
import com.whoiszxl.service.UserTokenService;
import com.whoiszxl.starter.core.exception.BadRequestException;
import com.whoiszxl.starter.core.utils.validate.ValidationUtils;
import com.whoiszxl.starter.web.model.R;
import com.whoiszxl.user.model.entity.UserInfoDO;
import com.xkcoding.justauth.AuthRequestFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 认证 API 服务
 * @author whoiszxl
 */
@Tag(name = "认证 API")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthRequestFactory authRequestFactory;
    private final UserTokenService userTokenService;
    private final UserInfoService userInfoService;

    @SaIgnore
    @Operation(summary = "登录", description = "用户登录")
    @PostMapping("/login")
    public R<LoginResponse> login(@Validated @RequestBody LoginCommand loginCommand, HttpServletRequest request) {
        return R.ok(authService.login(loginCommand, request));
    }

    @Operation(summary = "登出", description = "用户登出")
    @PostMapping("/logout")
    public R<String> logout() {
        StpUtil.logout();
        return R.ok();
    }

    @SaIgnore
    @Operation(summary = "三方账号登录授权", description = "三方账号登录授权")
    @Parameter(name = "source", description = "来源", example = "github", in = ParameterIn.PATH)
    @GetMapping("/social/{source}")
    public R<SocialAuthAuthorizeResp> authorize(@PathVariable String source) {
        AuthRequest authRequest = this.getAuthRequest(source);
        SocialAuthAuthorizeResp resp = SocialAuthAuthorizeResp.builder()
                .authorizeUrl(authRequest.authorize(AuthStateUtils.createState()))
                .build();
        return R.ok(resp);
    }

    private AuthRequest getAuthRequest(String source) {
        try {
            return authRequestFactory.get(source);
        } catch (Exception e) {
            throw new BadRequestException("暂不支持 [%s] 平台账号登录".formatted(source));
        }
    }

    @Operation(summary = "绑定三方账号", description = "绑定三方账号")
    @Parameter(name = "source", description = "来源", example = "github", in = ParameterIn.PATH)
    @PostMapping("/social/bind/{source}")
    public void bindSocial(@PathVariable String source, @RequestBody AuthCallback callback) {
        AuthRequest authRequest = authRequestFactory.get(source);
        AuthResponse<AuthUser> response = authRequest.login(callback);
        ValidationUtils.throwIf(!response.ok(), response.getMsg());
        AuthUser authUser = response.getData();
        userTokenService.bind(authUser, UserContextHolder.getMemberId());
    }

    @Operation(summary = "获取用户个人信息", description = "获取登录用户的个人信息")
    @GetMapping("/member/info")
    public R<UserInfoResponse> memberInfo() {
        UserContext context = UserContextHolder.getContext();
        UserInfoDO userInfo = userInfoService.getById(context.getId());
        UserInfoResponse userInfoResponse = BeanUtil.copyProperties(userInfo, UserInfoResponse.class);
        return R.ok(userInfoResponse);
    }
}
