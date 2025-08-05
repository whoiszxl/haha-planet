package com.whoiszxl.planet.mapper;

import com.whoiszxl.starter.mybatis.base.BaseMapper;
import com.whoiszxl.planet.model.entity.PlanetLikeDO;
import org.apache.ibatis.annotations.Mapper;

/**
* 点赞记录 Mapper
*
* @author whoiszxl
*/
@Mapper
public interface PlanetLikeMapper extends BaseMapper<PlanetLikeDO> {}