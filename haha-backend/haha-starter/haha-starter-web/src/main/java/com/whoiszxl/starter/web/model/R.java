package com.whoiszxl.starter.web.model;

import com.whoiszxl.starter.core.constants.StatusCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * 返回前端的响应信息
 */
@Setter
@Getter
@Schema(description = "响应信息")
public class R<T> implements Serializable {

    private R() {}

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "是否成功", example = "true")
    private boolean success;

    @Schema(description = "业务状态码", example = "200")
    private int code;

    @Schema(description = "业务状态信息", example = "操作成功")
    private String msg;

    @Schema(description = "响应数据")
    private T data;

    private R(boolean success, int code, String msg, T data) {
        this.success = success;
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> R<T> ok() {
        return new R<>(true, StatusCode.OK, StatusCode.SUCCESS_MSG, null);
    }

    public static <T> R<T> ok(T data) {
        return new R<>(true, StatusCode.OK, StatusCode.SUCCESS_MSG, data);
    }

    public static <T> R<T> ok(String msg) {
        return new R<>(true, StatusCode.OK, msg, null);
    }

    public static <T> R<T> ok(String msg, T data) {
        return new R<>(true, StatusCode.OK, msg, data);
    }

    public static <T> R<T> fail() {
        return new R<>(false, StatusCode.FAIL, StatusCode.FAIL_MSG, null);
    }

    public static <T> R<T> fail(String msg) {
        return new R<>(false, StatusCode.FAIL, msg, null);
    }

    public static <T> R<T> fail(int statusCode, String message) {
        return new R<>(false, statusCode, message, null);
    }

    public static R<Boolean> flag(boolean flag) {
        return flag ? ok() : fail();
    }


}
