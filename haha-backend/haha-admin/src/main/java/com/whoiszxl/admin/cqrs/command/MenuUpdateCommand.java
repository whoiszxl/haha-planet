package com.whoiszxl.admin.cqrs.command;

import com.whoiszxl.common.enums.MenuTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 系统菜单
 * </p>
 * @author whoiszxl
 */
@Data
@Schema(description = "菜单更新命令")
public class MenuUpdateCommand implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(name = "主键")
    private Long id;

    @Schema(name = "标题名称")
    private String title;

    @Schema(name = "上级菜单ID")
    private Long parentId;

    @Schema(description = "类型（1：目录；2：菜单；3：按钮）", type = "Integer", allowableValues = {"1", "2", "3"}, example = "2")
    private MenuTypeEnum type;

    @Schema(name = "路由地址")
    private String path;

    @Schema(name = "组件名称")
    private String name;

    @Schema(name = "组件路径")
    private String component;

    @Schema(name = "路由参数")
    private String query;

    @Schema(name = "重定向地址")
    private String redirect;

    @Schema(name = "菜单图标")
    private String icon;

    @Schema(name = "是否外链 1-是 0-否")
    private Boolean isFrame;

    @Schema(name = "是否缓存 1-是 0-否")
    private Boolean isCache;

    @Schema(name = "是否可见 1-是 0-否")
    private Boolean isVisible;

    @Schema(name = "权限标识")
    private String permission;

    @Schema(name = "排序索引")
    private Integer sort;

}
