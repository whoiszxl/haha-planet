package com.whoiszxl.planet.mapper;

import com.whoiszxl.starter.mybatis.base.BaseMapper;
import com.whoiszxl.planet.model.entity.PlanetPostArticleDO;
import org.apache.ibatis.annotations.Mapper;

/**
* 星球帖子文章扩展 Mapper
*
* @author whoiszxl
*/
@Mapper
public interface PlanetPostArticleMapper extends BaseMapper<PlanetPostArticleDO> {}