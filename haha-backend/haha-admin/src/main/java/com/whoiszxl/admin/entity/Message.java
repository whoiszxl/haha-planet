package com.whoiszxl.admin.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.whoiszxl.admin.enums.MessageTypeEnum;
import com.whoiszxl.common.enums.DisEnableStatusEnum;
import com.whoiszxl.starter.crud.model.entity.BaseDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @author whoiszxl
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_message")
@Schema(description = "消息表")
public class Message extends BaseDO {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "类型（1：系统消息）")
    private MessageTypeEnum type;

    @Schema(description = "乐观锁")
    @Version
    private Long version;

    @Schema(description = "状态（1：启用；2：禁用）", type = "Integer", allowableValues = {"1", "2"}, example = "1")
    private DisEnableStatusEnum status;

    @Schema(description = "逻辑删除 1: 已删除， 0: 未删除")
    @TableLogic
    private Integer isDeleted;

}
