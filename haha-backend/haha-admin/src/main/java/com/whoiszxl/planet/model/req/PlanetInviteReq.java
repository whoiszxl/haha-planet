package com.whoiszxl.planet.model.req;

import java.io.Serial;
import java.time.LocalDateTime;

import jakarta.validation.constraints.*;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

import org.hibernate.validator.constraints.Length;

import com.whoiszxl.starter.crud.model.BaseReq;

/**
 * 创建或修改星球邀请信息
 *
 * @author whoiszxl
 */
@Data
@Schema(description = "创建或修改星球邀请信息")
public class PlanetInviteReq extends BaseReq {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 星球ID
     */
    @Schema(description = "星球ID")
    @NotNull(message = "星球ID不能为空")
    private Long planetId;

    /**
     * 邀请人ID
     */
    @Schema(description = "邀请人ID")
    @NotNull(message = "邀请人ID不能为空")
    private Long inviterId;

    /**
     * 邀请码
     */
    @Schema(description = "邀请码")
    @NotBlank(message = "邀请码不能为空")
    @Length(max = 32, message = "邀请码长度不能超过 {max} 个字符")
    private String inviteCode;

    /**
     * 乐观锁
     */
    @Schema(description = "乐观锁")
    @NotNull(message = "乐观锁不能为空")
    private Long version;

    /**
     * 创建者
     */
    @Schema(description = "创建者")
    @NotNull(message = "创建者不能为空")
    private Long createdBy;
}