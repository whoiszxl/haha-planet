package com.whoiszxl.planet.mapper;

import com.whoiszxl.starter.mybatis.base.BaseMapper;
import com.whoiszxl.planet.model.entity.PlanetCommentDO;
import org.apache.ibatis.annotations.Mapper;

/**
* 帖子评论 Mapper
*
* @author whoiszxl
*/
@Mapper
public interface PlanetCommentMapper extends BaseMapper<PlanetCommentDO> {}