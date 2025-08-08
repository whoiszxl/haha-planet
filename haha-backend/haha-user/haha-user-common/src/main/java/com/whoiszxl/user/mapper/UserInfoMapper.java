package com.whoiszxl.user.mapper;

import com.whoiszxl.starter.mybatis.base.BaseMapper;
import com.whoiszxl.user.model.entity.UserInfoDO;
import org.apache.ibatis.annotations.Mapper;

/**
* 用户基础信息 Mapper
*
* @author whoiszxl
*/
@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfoDO> {}