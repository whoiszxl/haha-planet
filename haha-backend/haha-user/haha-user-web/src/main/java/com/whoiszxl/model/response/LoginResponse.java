package com.whoiszxl.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 登录响应参数
 * @author whoiszxl
 */
@Data
@Builder
@Schema(description = "登录响应参数")
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "令牌", example = "666666666")
    private String token;

    @Schema(description = "登录类型: [login, bind]", example = "bind")
    private String type;
}
