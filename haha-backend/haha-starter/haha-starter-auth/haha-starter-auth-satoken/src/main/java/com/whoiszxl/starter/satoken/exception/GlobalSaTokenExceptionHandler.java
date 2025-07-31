package com.whoiszxl.starter.satoken.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import com.whoiszxl.starter.web.model.R;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局 SaToken 异常处理器
 */
@RestControllerAdvice
public class GlobalSaTokenExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalSaTokenExceptionHandler.class);

    @ExceptionHandler(NotLoginException.class)
    public R<Void> handleNotLoginException(NotLoginException e, HttpServletRequest request) {
        log.error("地址 [{}] 认证失败，无法访问系统资源。", request.getRequestURI(), e);
        String errorMsg = switch (e.getType()) {
            case NotLoginException.KICK_OUT -> NotLoginException.KICK_OUT_MESSAGE;
            case NotLoginException.BE_REPLACED -> NotLoginException.BE_REPLACED_MESSAGE;
            default -> "登录状态已过期，请重新登录。";
        };
        return R.fail(HttpStatus.UNAUTHORIZED.value(), errorMsg);
    }

    @ExceptionHandler(NotPermissionException.class)
    public R<Void> handleNotPermissionException(NotPermissionException e, HttpServletRequest request) {
        log.error("地址 [{}]，权限码校验失败。", request.getRequestURI(), e);
        return R.fail(HttpStatus.FORBIDDEN.value(), "禁止访问");
    }

    @ExceptionHandler(NotRoleException.class)
    public R<Void> handleNotRoleException(NotRoleException e, HttpServletRequest request) {
        log.error("请求地址 [{}]，角色权限校验失败。", request.getRequestURI(), e);
        return R.fail(HttpStatus.FORBIDDEN.value(), "禁止访问");
    }

}
