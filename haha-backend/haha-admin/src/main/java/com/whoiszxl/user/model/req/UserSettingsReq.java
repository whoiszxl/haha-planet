package com.whoiszxl.user.model.req;

import com.whoiszxl.starter.crud.model.BaseReq;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;

/**
 * 创建或修改用户设置信息
 *
 * @author whoiszxl
 */
@Data
@Schema(description = "创建或修改用户设置信息")
public class UserSettingsReq extends BaseReq {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /**
     * 乐观锁
     */
    @Schema(description = "乐观锁")
    @NotNull(message = "乐观锁不能为空")
    private Long version;
}