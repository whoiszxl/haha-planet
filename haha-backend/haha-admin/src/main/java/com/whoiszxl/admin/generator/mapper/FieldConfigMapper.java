package com.whoiszxl.admin.generator.mapper;

import com.whoiszxl.admin.generator.model.entity.FieldConfigDO;
import com.whoiszxl.starter.mybatis.base.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 字段配置 Mapper
 * @author whoiszxl
 */
@Mapper
public interface FieldConfigMapper extends BaseMapper<FieldConfigDO> {

    /**
     * 根据表名称查询
     *
     * @param tableName 表名称
     * @return 字段配置信息
     */
    @Select("SELECT * FROM gen_field_config WHERE table_name = #{tableName} ORDER BY field_sort ASC")
    List<FieldConfigDO> selectListByTableName(@Param("tableName") String tableName);
}
