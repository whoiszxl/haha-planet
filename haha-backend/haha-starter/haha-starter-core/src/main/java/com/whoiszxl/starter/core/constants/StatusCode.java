package com.whoiszxl.starter.core.constants;

import cn.hutool.http.HttpStatus;

/**
 * 状态码
 */
public interface StatusCode {
    int OK = HttpStatus.HTTP_OK;
    int FAIL = HttpStatus.HTTP_INTERNAL_ERROR;

    String SUCCESS_MSG = "success";
    String FAIL_MSG = "fail";
}
