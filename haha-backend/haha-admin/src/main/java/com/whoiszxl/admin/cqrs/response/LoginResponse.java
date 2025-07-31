package com.whoiszxl.admin.cqrs.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 令牌信息
 * @author whoiszxl
 */
@Data
@Builder
@Schema(description = "令牌信息")
public class LoginResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "令牌", example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsb2dpblR5cGUiOiJsb2dpbiIsImxvZ2luSWQiOjEsInJuU3RyIjoiTE5MamRmY2Z3NXU4NkVuZWhHc0VHY0YwWlJiNmFpTG8ifQ.khA-OlO4xuewBL_B2BLYK0_nxYGQ8qO8b06tmOld6cg")
    private String token;
}
