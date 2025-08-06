package com.whoiszxl.user.model.resp;

import java.io.Serial;
import java.time.LocalDateTime;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;

import com.whoiszxl.starter.crud.model.BaseResponse;

/**
 * 用户令牌详情信息
 *
 * @author whoiszxl
 */
@Data
@ExcelIgnoreUnannotated
@Schema(description = "用户令牌详情信息")
public class UserTokenDetailResp extends BaseResponse {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    @ExcelProperty(value = "用户ID")
    private Long userId;

    /**
     * token
     */
    @Schema(description = "token")
    @ExcelProperty(value = "token")
    private String token;

    /**
     * token类型(access:访问令牌 refresh:刷新令牌)
     */
    @Schema(description = "token类型(access:访问令牌 refresh:刷新令牌)")
    @ExcelProperty(value = "token类型(access:访问令牌 refresh:刷新令牌)")
    private String tokenType;

    /**
     * 来源平台(web:网页 app:移动端 api:接口)
     */
    @Schema(description = "来源平台(web:网页 app:移动端 api:接口)")
    @ExcelProperty(value = "来源平台(web:网页 app:移动端 api:接口)")
    private String source;

    /**
     * 过期时间
     */
    @Schema(description = "过期时间")
    @ExcelProperty(value = "过期时间")
    private LocalDateTime expiresTime;

    /**
     * 登录IP
     */
    @Schema(description = "登录IP")
    @ExcelProperty(value = "登录IP")
    private String loginIp;

    /**
     * 登录时间
     */
    @Schema(description = "登录时间")
    @ExcelProperty(value = "登录时间")
    private LocalDateTime loginTime;

    /**
     * 用户代理
     */
    @Schema(description = "用户代理")
    @ExcelProperty(value = "用户代理")
    private String userAgent;

    /**
     * 附加信息
     */
    @Schema(description = "附加信息")
    @ExcelProperty(value = "附加信息")
    private String metaJson;

    /**
     * 状态(0:无效 1:有效)
     */
    @Schema(description = "状态(0:无效 1:有效)")
    @ExcelProperty(value = "状态(0:无效 1:有效)")
    private Integer status;
}