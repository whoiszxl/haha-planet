package com.whoiszxl.user.model.req;

import com.whoiszxl.starter.crud.model.BaseReq;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;

/**
 * 创建或修改用户令牌信息
 *
 * @author whoiszxl
 */
@Data
@Schema(description = "创建或修改用户令牌信息")
public class UserTokenReq extends BaseReq {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /**
     * token
     */
    @Schema(description = "token")
    @NotBlank(message = "token不能为空")
    @Length(max = 512, message = "token长度不能超过 {max} 个字符")
    private String token;

    /**
     * 乐观锁
     */
    @Schema(description = "乐观锁")
    @NotNull(message = "乐观锁不能为空")
    private Long version;
}