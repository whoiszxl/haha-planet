package com.whoiszxl.admin.cqrs.query;

import com.whoiszxl.starter.mybatis.annotation.Query;
import com.whoiszxl.starter.mybatis.enums.QueryType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 系统菜单
 * </p>
 *
 * @author whoiszxl
 * @since 2024-05-21
 */
@Data
@NoArgsConstructor
@Schema(description = "菜单查询条件")
public class MenuQuery implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(name = "标题名称")
    @Query(type = QueryType.LIKE)
    private String title;

    @Schema(description = "状态（1：启用；2：禁用）", type = "Integer", allowableValues = {"1", "2"}, example = "1")
    private Integer status;

    public MenuQuery(Integer status) {
        this.status = status;
    }

}
