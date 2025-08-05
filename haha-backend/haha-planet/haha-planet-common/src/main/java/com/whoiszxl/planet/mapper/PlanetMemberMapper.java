package com.whoiszxl.planet.mapper;

import com.whoiszxl.starter.mybatis.base.BaseMapper;
import com.whoiszxl.planet.model.entity.PlanetMemberDO;
import org.apache.ibatis.annotations.Mapper;

/**
* 星球成员 Mapper
*
* @author whoiszxl
*/
@Mapper
public interface PlanetMemberMapper extends BaseMapper<PlanetMemberDO> {}