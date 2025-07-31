package com.whoiszxl.admin.service;


import com.whoiszxl.admin.cqrs.command.MessageReq;
import com.whoiszxl.admin.cqrs.query.MessageQuery;
import com.whoiszxl.admin.cqrs.response.message.MessageResp;
import com.whoiszxl.starter.crud.model.PageQuery;
import com.whoiszxl.starter.crud.model.PageResponse;

import java.util.List;

/**
 * 消息业务接口
 *
 * @author Bull-BCLS
 * @since 2023/10/15 19:05
 */
public interface MessageService {

    /**
     * 分页查询列表
     *
     * @param query     查询条件
     * @return 分页列表信息
     */
    PageResponse<MessageResp> page(MessageQuery query);

    /**
     * 新增
     *
     * @param req        新增信息
     * @param userIdList 接收人列表
     */
    void add(MessageReq req, List<Long> userIdList);

    /**
     * 删除
     *
     * @param ids ID 列表
     */
    void delete(List<Long> ids);
}