package com.whoiszxl.admin.cqrs.command;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户密码重置信息
 * @author whoiszxl
 */
@Data
@Schema(description = "用户密码重置信息")
public class AdminPasswordResetCommand implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 新密码（加密）
     */
    @Schema(description = "新密码（加密）", example = "Gzc78825P5baH190lRuZFb9KJxRt/psN2jiyOMPoc5WRcCvneCwqDm3Q33BZY56EzyyVy7vQu7jQwYTK4j1+5w==")
    @NotBlank(message = "新密码不能为空")
    private String newPassword;
}
