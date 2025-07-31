package com.whoiszxl.admin.controller.sys;

import com.whoiszxl.admin.cqrs.query.MessageQuery;
import com.whoiszxl.admin.cqrs.response.message.MessageResp;
import com.whoiszxl.admin.cqrs.response.message.MessageUnreadResp;
import com.whoiszxl.admin.service.MessageAdminService;
import com.whoiszxl.admin.service.MessageService;
import com.whoiszxl.common.utils.LoginHelper;
import com.whoiszxl.starter.crud.model.PageResponse;
import com.whoiszxl.starter.log.core.annotation.Log;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 消息管理 API
 * @author whoiszxl
 */
@Tag(name = "消息管理 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/message")
public class MessageController {

    private final MessageService messageService;
    private final MessageAdminService messageUserService;

    @Operation(summary = "分页查询列表", description = "分页查询列表")
    @GetMapping
    public PageResponse<MessageResp> page(MessageQuery query) {
        query.setUserId(LoginHelper.getAdminId());
        return messageService.page(query);
    }

    @Operation(summary = "删除数据", description = "删除数据")
    @Parameter(name = "ids", description = "ID 列表", example = "1,2", in = ParameterIn.PATH)
    @DeleteMapping("/{ids}")
    public void delete(@PathVariable List<Long> ids) {
        messageService.delete(ids);
    }

    @Operation(summary = "标记已读", description = "将消息标记为已读状态")
    @Parameter(name = "ids", description = "消息ID列表", example = "1,2", in = ParameterIn.QUERY)
    @PatchMapping("/read")
    public void readMessage(@RequestParam(required = false) List<Long> ids) {
        messageUserService.readMessage(ids);
    }

    @Log(ignore = true)
    @Operation(summary = "查询未读消息数量", description = "查询当前用户的未读消息数量")
    @Parameter(name = "isDetail", description = "是否查询详情", example = "true", in = ParameterIn.QUERY)
    @GetMapping("/unread")
    public MessageUnreadResp countUnreadMessage(@RequestParam(required = false) Boolean detail) {
        return messageUserService.countUnreadMessageByUserId(LoginHelper.getAdminId(), detail);
    }
}
