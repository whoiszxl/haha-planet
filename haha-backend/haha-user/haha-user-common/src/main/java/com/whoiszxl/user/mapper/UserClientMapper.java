package com.whoiszxl.user.mapper;

import com.whoiszxl.starter.mybatis.base.BaseMapper;
import com.whoiszxl.user.model.entity.UserClientDO;
import org.apache.ibatis.annotations.Mapper;

/**
* 用户客户端 Mapper
*
* @author whoiszxl
*/
@Mapper
public interface UserClientMapper extends BaseMapper<UserClientDO> {}