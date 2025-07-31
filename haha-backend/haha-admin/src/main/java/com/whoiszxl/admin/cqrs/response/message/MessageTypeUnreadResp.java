package com.whoiszxl.admin.cqrs.response.message;

import com.whoiszxl.admin.enums.MessageTypeEnum;
import com.whoiszxl.starter.crud.model.BaseResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 各类型未读消息信息
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "各类型未读消息信息")
public class MessageTypeUnreadResp extends BaseResponse {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 类型
     */
    @Schema(description = "类型（1：系统消息）", example = "1")
    private MessageTypeEnum type;

    /**
     * 数量
     */
    @Schema(description = "数量", example = "10")
    private Long count;
}