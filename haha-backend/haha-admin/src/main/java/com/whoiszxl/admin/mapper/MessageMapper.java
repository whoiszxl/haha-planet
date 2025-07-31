package com.whoiszxl.admin.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.whoiszxl.admin.cqrs.response.message.MessageResp;
import com.whoiszxl.admin.entity.Message;
import com.whoiszxl.starter.mybatis.base.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 消息 Mapper
 *
 * @author Bull-BCLS
 * @since 2023/10/15 19:05
 */
@Mapper
public interface MessageMapper extends BaseMapper<Message> {

    /**
     * 分页查询列表
     *
     * @param page         分页查询条件
     * @param queryWrapper 查询条件
     * @return 分页信息
     */
    @Select("""
    SELECT
                t1.*,
                t2.admin_id,
                t2.is_read,
                t2.read_time
            FROM sys_message AS t1
                     LEFT JOIN sys_message_admin AS t2 ON t2.message_id = t1.id

    """)
    IPage<MessageResp> selectPageByUserId(@Param("page") IPage<Object> page,
                                          @Param(Constants.WRAPPER) QueryWrapper<Message> queryWrapper);
}