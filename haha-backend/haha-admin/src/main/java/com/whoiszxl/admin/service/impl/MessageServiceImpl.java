package com.whoiszxl.admin.service.impl;

import cn.crane4j.annotation.AutoOperate;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whoiszxl.admin.cqrs.command.MessageReq;
import com.whoiszxl.admin.cqrs.query.MessageQuery;
import com.whoiszxl.admin.cqrs.response.message.MessageResp;
import com.whoiszxl.admin.entity.Message;
import com.whoiszxl.admin.mapper.MessageMapper;
import com.whoiszxl.admin.service.MessageAdminService;
import com.whoiszxl.admin.service.MessageService;
import com.whoiszxl.starter.core.utils.validate.CheckUtils;
import com.whoiszxl.starter.crud.model.PageResponse;
import com.whoiszxl.starter.mybatis.query.QueryWrapperHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 消息业务实现
 * @author whoiszxl
 */
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageMapper baseMapper;
    private final MessageAdminService messageAdminService;

    @Override
    @AutoOperate(type = MessageResp.class, on = "list")
    public PageResponse<MessageResp> page(MessageQuery query) {
        QueryWrapper<Message> queryWrapper = QueryWrapperHelper.build(query);
        queryWrapper.apply(null != query.getUserId(), "t2.user_id={0}", query.getUserId())
            .apply(null != query.getIsRead(), "t2.is_read={0}", query.getIsRead());
        IPage<MessageResp> page = baseMapper.selectPageByUserId(new Page<>(query.getPage(), query
            .getSize()), queryWrapper);
        return PageResponse.build(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(MessageReq req, List<Long> userIdList) {
        CheckUtils.throwIf(() -> CollUtil.isEmpty(userIdList), "消息接收人不能为空");
        Message message = BeanUtil.copyProperties(req, Message.class);
        baseMapper.insert(message);
        messageAdminService.add(message.getId(), userIdList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> ids) {
        baseMapper.deleteBatchIds(ids);
        messageAdminService.deleteByMessageIds(ids);
    }
}