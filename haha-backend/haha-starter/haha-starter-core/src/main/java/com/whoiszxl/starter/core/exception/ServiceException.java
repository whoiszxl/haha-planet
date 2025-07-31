package com.whoiszxl.starter.core.exception;

public class ServiceException extends BaseException {
    public ServiceException(String message) {
        super(message);
    }

    public ServiceException() {
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
