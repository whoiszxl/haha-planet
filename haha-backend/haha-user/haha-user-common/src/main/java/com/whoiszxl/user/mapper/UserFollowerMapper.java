package com.whoiszxl.user.mapper;

import com.whoiszxl.starter.mybatis.base.BaseMapper;
import com.whoiszxl.user.model.entity.UserFollowerDO;
import org.apache.ibatis.annotations.Mapper;

/**
* 用户粉丝(关注我的人) Mapper
*
* @author whoiszxl
*/
@Mapper
public interface UserFollowerMapper extends BaseMapper<UserFollowerDO> {}