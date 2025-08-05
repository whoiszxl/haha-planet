package com.whoiszxl.planet.model.req;

import com.whoiszxl.starter.crud.model.BaseReq;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;

/**
 * 创建或修改星球通知信息
 *
 * @author whoiszxl
 */
@Data
@Schema(description = "创建或修改星球通知信息")
public class PlanetNotificationReq extends BaseReq {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 星球ID
     */
    @Schema(description = "星球ID")
    @NotNull(message = "星球ID不能为空")
    private Long planetId;

    /**
     * 接收通知的用户ID
     */
    @Schema(description = "接收通知的用户ID")
    @NotNull(message = "接收通知的用户ID不能为空")
    private Long userId;

    /**
     * 通知类型: 1-新帖子 2-新评论 3-点赞 4-关注 5-系统通知 6-星球公告
     */
    @Schema(description = "通知类型: 1-新帖子 2-新评论 3-点赞 4-关注 5-系统通知 6-星球公告")
    @NotNull(message = "通知类型: 1-新帖子 2-新评论 3-点赞 4-关注 5-系统通知 6-星球公告不能为空")
    private Integer notificationType;

    /**
     * 通知标题
     */
    @Schema(description = "通知标题")
    @NotBlank(message = "通知标题不能为空")
    @Length(max = 100, message = "通知标题长度不能超过 {max} 个字符")
    private String title;

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