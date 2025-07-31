package com.whoiszxl.starter.web.config.exception;

import com.whoiszxl.starter.core.constants.StatusCode;
import com.whoiszxl.starter.core.exception.BadRequestException;
import com.whoiszxl.starter.core.exception.ServiceException;
import com.whoiszxl.starter.web.model.R;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理服务层异常的控制器异常处理方法。
     * 当发生 ServiceException 时，此方法将被调用，用于统一处理业务异常。
     *
     * @param e ServiceException 异常对象，封装了具体的业务错误信息。
     * @param request HttpServletRequest 请求对象，用于获取请求信息。
     * @return R<Void> 返回一个结果对象，其中包含错误状态码和错误信息。
     */
    @ExceptionHandler(ServiceException.class)
    public R<Void> handleServiceException(ServiceException e, HttpServletRequest request) {
        log.error("地址 [{}] 调用发生业务异常", request.getRequestURI(), e);
        return R.fail(StatusCode.FAIL, e.getMessage());
    }

    /**
     * 处理客户端发起的恶意或不合法请求异常。
     *
     * @param e 异常对象，表示客户端请求过程中发生的错误。
     * @param request 客户端的HTTP请求对象，用于获取请求信息。
     * @return 返回一个包含错误码和错误信息的响应对象。
     */
    @ExceptionHandler(BadRequestException.class)
    public R<Void> handleRequestException(BadRequestException e, HttpServletRequest request) {
        log.warn("地址 [{}] 请求失败", request.getRequestURI(), e);
        return R.fail(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    /**
     * 处理未知运行时异常的异常处理器。
     *
     * @param e 发生的运行时异常
     * @param request 客户端的HTTP请求
     * @return 返回一个包含异常信息的响应对象
     */
    @ExceptionHandler(RuntimeException.class)
    public R<Void> handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        log.error("地址 [{}] 发生系统异常", request.getRequestURI(), e);
        return R.fail(e.getMessage());
    }

    /**
     * 处理所有未知异常的异常处理器。
     *
     * @param e 抛出的异常对象。
     * @param request 客户端的HTTP请求对象，用于获取请求信息。
     * @return 返回一个表示操作失败的结果对象，其中包含异常信息。
     */
    @ExceptionHandler(Throwable.class)
    public R<Void> handleException(Throwable e, HttpServletRequest request) {
        log.error("地址 [{}] 发生未知异常", request.getRequestURI(), e);
        return R.fail(e.getMessage());
    }



    /**
     * 处理参数验证失败的异常
     *
     * @param e 参数验证异常
     * @param request HTTP请求
     * @return 返回友好的错误信息
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<Map<String, String>> handleValidationException(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.warn("地址 [{}] 请求参数验证失败", request.getRequestURI());

        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        Map<String, String> errorMap = new HashMap<>(fieldErrors.size());
        StringBuilder errorMsg = new StringBuilder();

        for (FieldError fieldError : fieldErrors) {
            String field = fieldError.getField();
            String defaultMessage = fieldError.getDefaultMessage();
            errorMap.put(field, defaultMessage);

            // 构建友好的错误信息
            errorMsg.append(defaultMessage).append("；");
        }

        // 移除最后一个分号
        if (!errorMsg.isEmpty()) {
            errorMsg.deleteCharAt(errorMsg.length() - 1);
        }

        return R.fail(HttpStatus.BAD_REQUEST.value(), errorMsg.toString());
    }



}
