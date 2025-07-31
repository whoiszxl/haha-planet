package com.whoiszxl.admin.cqrs.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 验证码信息
 * @author whoiszxl
 */
@Data
@Builder
@Schema(description = "验证码信息")
public class CaptchaResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "验证码标识", example = "666")
    private String uuid;

    @Schema(description = "验证码图片（Base64编码，带图片格式：data:image/gif;base64）", example = "data:image/png;base64,iVBORw0KGgoAAAAN...")
    private String img;

    @Schema(description = "过期时间戳", example = "1714376969409")
    private Long expireTime;
}
