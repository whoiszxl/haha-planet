package com.whoiszxl.user.model.req;

import com.whoiszxl.starter.crud.model.BaseReq;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;

/**
 * 创建或修改积分记录信息
 *
 * @author whoiszxl
 */
@Data
@Schema(description = "创建或修改积分记录信息")
public class PointsRecordReq extends BaseReq {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /**
     * 积分变化(正数增加，负数减少)
     */
    @Schema(description = "积分变化(正数增加，负数减少)")
    @NotNull(message = "积分变化(正数增加，负数减少)不能为空")
    private Integer pointsChange;

    /**
     * 变化前积分
     */
    @Schema(description = "变化前积分")
    @NotNull(message = "变化前积分不能为空")
    private Long pointsBefore;

    /**
     * 变化后积分
     */
    @Schema(description = "变化后积分")
    @NotNull(message = "变化后积分不能为空")
    private Long pointsAfter;

    /**
     * 变化类型(sign:签到 post:发帖 like:点赞 comment:评论)
     */
    @Schema(description = "变化类型(sign:签到 post:发帖 like:点赞 comment:评论)")
    @NotBlank(message = "变化类型(sign:签到 post:发帖 like:点赞 comment:评论)不能为空")
    @Length(max = 32, message = "变化类型(sign:签到 post:发帖 like:点赞 comment:评论)长度不能超过 {max} 个字符")
    private String changeType;

    /**
     * 乐观锁
     */
    @Schema(description = "乐观锁")
    @NotNull(message = "乐观锁不能为空")
    private Long version;
}