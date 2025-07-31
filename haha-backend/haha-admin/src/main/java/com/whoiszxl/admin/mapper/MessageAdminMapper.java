package com.whoiszxl.admin.mapper;

import com.whoiszxl.admin.entity.MessageAdmin;
import com.whoiszxl.starter.mybatis.base.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 消息和用户 Mapper 接口
 */
@Mapper
public interface MessageAdminMapper extends BaseMapper<MessageAdmin> {

    /**
     * 根据用户 ID 和消息类型查询未读消息数量
     *
     * @param userId 用户 ID
     * @param type   消息类型
     * @return 未读消息信息
     */
    Long selectUnreadCountByUserIdAndType(@Param("userId") Long userId, @Param("type") Integer type);
}