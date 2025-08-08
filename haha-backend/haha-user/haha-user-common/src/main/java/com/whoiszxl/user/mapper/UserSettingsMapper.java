package com.whoiszxl.user.mapper;

import com.whoiszxl.starter.mybatis.base.BaseMapper;
import com.whoiszxl.user.model.entity.UserSettingsDO;
import org.apache.ibatis.annotations.Mapper;

/**
* 用户设置 Mapper
*
* @author whoiszxl
*/
@Mapper
public interface UserSettingsMapper extends BaseMapper<UserSettingsDO> {}