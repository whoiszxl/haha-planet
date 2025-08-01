package com.whoiszxl.captcha.google.exception;

/**
 * 谷歌验证码异常
 *
 * @author whoiszxl
 */
public class GoogleCaptchaException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 错误代码
     */
    private String errorCode;

    public GoogleCaptchaException(String message) {
        super(message);
    }

    public GoogleCaptchaException(String message, Throwable cause) {
        super(message, cause);
    }

    public GoogleCaptchaException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public GoogleCaptchaException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
