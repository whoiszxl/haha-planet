package com.whoiszxl.model.response;

import com.whoiszxl.common.enums.DisEnableStatusEnum;
import com.whoiszxl.starter.crud.model.BaseResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;

/**
 * 用户客户端信息
 * @author whoiszxl
 */
@Data
@Schema(description = "用户客户端信息")
public class UserClientResponse extends BaseResponse {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "客户端 Key", example = "666")
    private String clientKey;

    @Schema(description = "客户端秘钥", example = "666")
    private String clientSecret;

    @Schema(description = "认证类型", example = "ACCOUNT")
    private String authType;

    @Schema(description = "客户端类型", example = "PC")
    private String clientType;

    @Schema(description = "Token 最低活跃频率（单位：秒，-1：不限制，永不冻结）", example = "1800")
    private Long activeTimeout;

    @Schema(description = "Token 有效期（单位：秒，-1：永不过期）", example = "86400")
    private Long timeout;

    @Schema(description = "状态", example = "1")
    private Integer status;
}
