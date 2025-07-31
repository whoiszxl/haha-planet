package com.whoiszxl.admin.cqrs.query;

import com.whoiszxl.admin.enums.MessageTypeEnum;
import com.whoiszxl.starter.crud.model.PageQuery;
import com.whoiszxl.starter.mybatis.annotation.Query;
import com.whoiszxl.starter.mybatis.annotation.QueryIgnore;
import com.whoiszxl.starter.mybatis.enums.QueryType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 消息查询条件
 * @author whoiszxl
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "消息查询条件")
public class MessageQuery extends PageQuery {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Schema(description = "ID", example = "1")
    private Long id;

    /**
     * 标题
     */
    @Schema(description = "标题", example = "欢迎注册 xxx")
    @Query(type = QueryType.LIKE)
    private String title;

    /**
     * 类型
     */
    @Schema(description = "类型", example = "1")
    private Integer type;

    /**
     * 是否已读
     */
    @Schema(description = "是否已读", example = "true")
    @QueryIgnore
    private Boolean isRead;

    /**
     * 用户 ID
     */
    @Schema(hidden = true)
    @QueryIgnore
    private Long userId;
}