package com.whoiszxl.admin.generator.service;

import com.whoiszxl.admin.generator.model.entity.FieldConfigDO;
import com.whoiszxl.admin.generator.model.entity.GenConfigDO;
import com.whoiszxl.admin.generator.model.query.TableQuery;
import com.whoiszxl.admin.generator.model.req.GenConfigReq;
import com.whoiszxl.admin.generator.model.resp.GeneratePreviewResp;
import com.whoiszxl.admin.generator.model.resp.TableResp;
import com.whoiszxl.starter.crud.model.PageQuery;
import com.whoiszxl.starter.crud.model.PageResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.SQLException;
import java.util.List;

/**
 * 代码生成业务接口
 * @author whoiszxl
 */
public interface GeneratorService {

    /**
     * 分页查询表信息列表
     *
     * @param query     查询条件
     * @param pageQuery 分页查询条件
     * @return 表信息分页列表
     * @throws SQLException /
     */
    PageResponse<TableResp> pageTable(TableQuery query, PageQuery pageQuery) throws SQLException;

    /**
     * 查询生成配置信息
     *
     * @param tableName 表名称
     * @return 生成配置信息
     * @throws SQLException /
     */
    GenConfigDO getGenConfig(String tableName) throws SQLException;

    /**
     * 查询字段配置列表
     *
     * @param tableName   表名称
     * @param requireSync 是否需要同步
     * @return 字段配置列表
     */
    List<FieldConfigDO> listFieldConfig(String tableName, Boolean requireSync);

    /**
     * 保存代码生成配置信息
     *
     * @param req       代码生成配置信息
     * @param tableName 表名称
     */
    void saveConfig(GenConfigReq req, String tableName);

    /**
     * 生成预览
     *
     * @param tableName 表名称
     * @return 预览信息
     */
    List<GeneratePreviewResp> preview(String tableName);

    /**
     * 生成代码
     *
     * @param tableNames 表明层
     * @param request    请求对象
     * @param response   响应对象
     */
    void generate(List<String> tableNames, HttpServletRequest request, HttpServletResponse response);
}
