package com.whoiszxl.admin.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.whoiszxl.common.enums.DisEnableStatusEnum;
import com.whoiszxl.starter.crud.model.entity.BaseDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * @author whoiszxl
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_message_admin")
@Schema(description = "消息与管理员关联表")
public class MessageAdmin extends BaseDO {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "消息ID")
    private Long messageId;

    @Schema(description = "管理员ID")
    private Long adminId;

    @Schema(description = "是否已读 （0：未读 1：已读）")
    private Integer isRead;

    @Schema(description = "读取时间")
    private LocalDateTime readTime;

    @Schema(description = "乐观锁")
    @Version
    private Long version;

    @Schema(description = "状态（1：启用；2：禁用）", type = "Integer", allowableValues = {"1", "2"}, example = "1")
    private DisEnableStatusEnum status;

    @Schema(description = "逻辑删除 1: 已删除， 0: 未删除")
    @TableLogic
    private Integer isDeleted;

}
