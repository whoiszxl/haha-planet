package com.whoiszxl.planet.mapper;

import com.whoiszxl.starter.mybatis.base.BaseMapper;
import com.whoiszxl.planet.model.entity.PlanetDO;
import org.apache.ibatis.annotations.Mapper;

/**
* 星球 Mapper
*
* @author whoiszxl
*/
@Mapper
public interface PlanetMapper extends BaseMapper<PlanetDO> {}