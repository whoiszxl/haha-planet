package com.whoiszxl.user.model.resp;

import java.io.Serial;
import java.time.LocalDateTime;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

import com.whoiszxl.starter.crud.model.BaseResponse;

/**
 * 用户客户端信息
 *
 * @author whoiszxl
 */
@Data
@Schema(description = "用户客户端信息")
public class UserClientResp extends BaseResponse {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * key
     */
    @Schema(description = "key")
    private String clientKey;

    /**
     * secret
     */
    @Schema(description = "secret")
    private String clientSecret;

    /**
     * 授权类型
     */
    @Schema(description = "授权类型")
    private String authType;

    /**
     * 客户端类型
     */
    @Schema(description = "客户端类型")
    private String clientType;

    /**
     * Token最低活跃频率 单位:秒，-1:不限制，永不冻结）
     */
    @Schema(description = "Token最低活跃频率 单位:秒，-1:不限制，永不冻结）")
    private Long activeTimeout;

    /**
     * Token有效期 单位:秒，-1:永不过期）
     */
    @Schema(description = "Token有效期 单位:秒，-1:永不过期）")
    private Long timeout;

    /**
     * 状态(0:无效 1:有效)
     */
    @Schema(description = "状态(0:无效 1:有效)")
    private Integer status;
}