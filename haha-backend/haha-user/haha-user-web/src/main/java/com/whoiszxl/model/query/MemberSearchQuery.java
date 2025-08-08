package com.whoiszxl.model.query;

import com.whoiszxl.starter.crud.model.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户搜索查询实体
 * @author whoiszxl
 */
@Data
@Schema(description = "用户搜索查询实体")
public class MemberSearchQuery extends PageQuery {

    @Schema(description = "用户名")
    private String username;
}
