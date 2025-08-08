package com.whoiszxl.user.mapper;

import com.whoiszxl.starter.mybatis.base.BaseMapper;
import com.whoiszxl.user.model.entity.UserFollowingDO;
import org.apache.ibatis.annotations.Mapper;

/**
* 用户关注(我关注的人) Mapper
*
* @author whoiszxl
*/
@Mapper
public interface UserFollowingMapper extends BaseMapper<UserFollowingDO> {}