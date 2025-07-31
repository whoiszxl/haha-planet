package com.whoiszxl.admin.cqrs.command;

import com.whoiszxl.common.enums.DisEnableStatusEnum;
import com.whoiszxl.common.enums.MenuTypeEnum;
import com.whoiszxl.starter.crud.model.BaseReq;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;

/**
 * <p>
 * 系统菜单
 * </p>
 * @author whoiszxl
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "菜单新增命令")
public class MenuReq extends BaseReq {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "标题", example = "用户管理")
    @NotBlank(message = "标题不能为空")
    @Length(max = 30, message = "标题长度不能超过 {max} 个字符")
    private String title;

    @Schema(name = "上级菜单ID")
    private Long parentId;

    @Schema(description = "类型（1：目录；2：菜单；3：按钮）", type = "Integer", allowableValues = {"1", "2", "3"}, example = "2")
    @NotNull(message = "类型非法")
    private MenuTypeEnum type;

    @Schema(description = "路由地址", example = "/system/user")
    @Length(max = 255, message = "路由地址长度不能超过 {max} 个字符")
    private String path;

    @Schema(description = "组件名称", example = "User")
    @Length(max = 50, message = "组件名称长度不能超过 {max} 个字符")
    private String name;

    @Schema(description = "组件路径", example = "/system/user/index")
    @Length(max = 255, message = "组件路径长度不能超过 {max} 个字符")
    private String component;

    @Schema(name = "路由参数")
    private String query;

    @Schema(name = "重定向地址")
    private String redirect;

    @Schema(description = "图标", example = "user")
    @Length(max = 50, message = "图标长度不能超过 {max} 个字符")
    private String icon;

    @Schema(name = "是否外链 1-是 0-否")
    private Boolean isFrame;

    @Schema(name = "是否缓存 1-是 0-否")
    private Boolean isCache;

    @Schema(name = "是否可见 1-是 0-否")
    private Boolean isVisible;

    @Schema(description = "权限标识", example = "system:user:list")
    @Length(max = 100, message = "权限标识长度不能超过 {max} 个字符")
    private String permission;

    @Schema(description = "排序", example = "1")
    @NotNull(message = "排序不能为空")
    @Min(value = 1, message = "排序最小值为 {value}")
    private Integer sort;

    @Schema(description = "状态（1：启用；2：禁用）", type = "Integer", allowableValues = {"1", "2"}, example = "1")
    @NotNull(message = "状态非法")
    private DisEnableStatusEnum status;

}
