package com.whoiszxl.admin.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.whoiszxl.common.enums.DisEnableStatusEnum;
import com.whoiszxl.common.enums.MenuTypeEnum;
import com.whoiszxl.starter.crud.model.entity.BaseDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * <p>
 * 系统菜单
 * </p>
 *
 * @author whoiszxl
 * @since 2024-05-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_menu")
public class Menu extends BaseDO {

    @Serial
    private static final long serialVersionUID = 1L;


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

    @Schema(name = "乐观锁")
    @Version
    private Long version;

    @Schema(description = "状态（1：启用；2：禁用）", type = "Integer", allowableValues = {"1", "2"}, example = "1")
    private DisEnableStatusEnum status;

    @Schema(name = "逻辑删除 1: 已删除， 0: 未删除")
    @TableLogic
    private Integer isDeleted;

}
