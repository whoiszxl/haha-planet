package com.whoiszxl.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.whoiszxl.admin.cqrs.response.message.MessageTypeUnreadResp;
import com.whoiszxl.admin.cqrs.response.message.MessageUnreadResp;
import com.whoiszxl.admin.entity.MessageAdmin;
import com.whoiszxl.admin.enums.MessageTypeEnum;
import com.whoiszxl.admin.mapper.MessageAdminMapper;
import com.whoiszxl.admin.service.MessageAdminService;
import com.whoiszxl.starter.core.utils.validate.CheckUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 消息和用户关联业务实现
 *
 * @author Bull-BCLS
 * @since 2023/10/15 19:05
 */
@Service
@RequiredArgsConstructor
public class MessageAdminServiceImpl implements MessageAdminService {

    private final MessageAdminMapper baseMapper;

    @Override
    public MessageUnreadResp countUnreadMessageByUserId(Long userId, Boolean isDetail) {
        MessageUnreadResp result = new MessageUnreadResp();
        Long total = 0L;
        if (Boolean.TRUE.equals(isDetail)) {
            List<MessageTypeUnreadResp> detailList = new ArrayList<>();
            for (MessageTypeEnum messageType : MessageTypeEnum.values()) {
                MessageTypeUnreadResp resp = new MessageTypeUnreadResp();
                resp.setType(messageType);
                Long count = baseMapper.selectUnreadCountByUserIdAndType(userId, messageType.getValue());
                resp.setCount(count);
                detailList.add(resp);
                total += count;
            }
            result.setDetails(detailList);
        } else {
            total = baseMapper.selectUnreadCountByUserIdAndType(userId, null);
        }
        result.setTotal(total);
        return result;
    }

    @Override
    public void add(Long messageId, List<Long> adminIdList) {
        CheckUtils.throwIfEmpty(adminIdList, "消息接收人不能为空");
        List<MessageAdmin> messageUserList = adminIdList.stream().map(adminId -> {
            MessageAdmin messageUser = new MessageAdmin();
            messageUser.setAdminId(adminId);
            messageUser.setMessageId(messageId);
            messageUser.setIsRead(0);
            return messageUser;
        }).toList();
        baseMapper.insertBatch(messageUserList);
    }

    @Override
    public void readMessage(List<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return;
        }
        baseMapper.lambdaUpdate()
            .set(MessageAdmin::getIsRead, true)
            .set(MessageAdmin::getReadTime, LocalDateTime.now())
            .eq(MessageAdmin::getIsRead, false)
            .in(CollUtil.isNotEmpty(ids), MessageAdmin::getMessageId, ids)
            .update();
    }

    @Override
    public void deleteByMessageIds(List<Long> messageIds) {
        if (CollUtil.isEmpty(messageIds)) {
            return;
        }
        baseMapper.lambdaUpdate().in(MessageAdmin::getMessageId, messageIds).remove();
    }
}