package com.whoiszxl.planet.mapper;

import com.whoiszxl.starter.mybatis.base.BaseMapper;
import com.whoiszxl.planet.model.entity.PlanetPostDO;
import org.apache.ibatis.annotations.Mapper;

/**
* 星球帖子 Mapper
*
* @author whoiszxl
*/
@Mapper
public interface PlanetPostMapper extends BaseMapper<PlanetPostDO> {}