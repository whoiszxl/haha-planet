package com.whoiszxl.user.mapper;

import com.whoiszxl.starter.mybatis.base.BaseMapper;
import com.whoiszxl.user.model.entity.UserTokenDO;
import org.apache.ibatis.annotations.Mapper;

/**
* 用户令牌 Mapper
*
* @author whoiszxl
*/
@Mapper
public interface UserTokenMapper extends BaseMapper<UserTokenDO> {}