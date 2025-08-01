package com.whoiszxl.admin.cqrs.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author whoiszxl
 */
@Data
@Schema(description = "账号密码登录命令")
public class PasswordLoginCommand implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "用户名", example = "zhangsan")
    @NotBlank(message = "用户名不能为空")
    private String username;

    @Schema(description = "密码（加密）", example = "qwertyuiop")
    @NotBlank(message = "密码不能为空")
    private String password;

    @Schema(description = "验证码", example = "ABCD")
    @NotBlank(message = "验证码不能为空")
    private String captcha;

    @Schema(description = "验证码标识", example = "666")
    @NotBlank(message = "验证码标识不能为空")
    private String uuid;

    @Schema(description = "Google验证码（可选）", example = "123456")
    private String googleCode;
}