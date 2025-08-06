package com.whoiszxl.user.model.req;

import com.whoiszxl.starter.crud.model.BaseReq;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;

/**
 * 创建或修改用户等级信息
 *
 * @author whoiszxl
 */
@Data
@Schema(description = "创建或修改用户等级信息")
public class UserLevelReq extends BaseReq {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 等级
     */
    @Schema(description = "等级")
    @NotNull(message = "等级不能为空")
    private Integer level;

    /**
     * 等级名称
     */
    @Schema(description = "等级名称")
    @NotBlank(message = "等级名称不能为空")
    @Length(max = 64, message = "等级名称长度不能超过 {max} 个字符")
    private String levelName;

    /**
     * 最小经验值
     */
    @Schema(description = "最小经验值")
    @NotNull(message = "最小经验值不能为空")
    private Long minExperience;

    /**
     * 最大经验值
     */
    @Schema(description = "最大经验值")
    @NotNull(message = "最大经验值不能为空")
    private Long maxExperience;

    /**
     * 乐观锁
     */
    @Schema(description = "乐观锁")
    @NotNull(message = "乐观锁不能为空")
    private Long version;
}