package com.whoiszxl.admin.cqrs.response.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.whoiszxl.starter.crud.model.BaseResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.List;

/**
 * 未读消息信息
 * @author whoiszxl
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "未读消息信息")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MessageUnreadResp extends BaseResponse {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 未读消息数量
     */
    @Schema(description = "未读消息数量", example = "20")
    private Long total;

    /**
     * 各类型未读消息数量
     */
    @Schema(description = "各类型未读消息数量")
    private List<MessageTypeUnreadResp> details;
}