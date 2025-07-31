package com.whoiszxl.admin.cqrs.command;

import com.whoiszxl.common.enums.DisEnableStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @author whoiszxl
 */
@Data
@Schema(description = "管理员新增命令")
public class AdminUpdateCommand {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "账号")
    @NotEmpty(message = "账号不能为空")
    private String username;

    @Schema(description = "昵称")
    @NotEmpty(message = "昵称不能为空")
    private String nickname;

    @NotBlank(message = "密码不能为空")
    @Schema(description = "密码")
    private String password;

    @Schema(description = "头像")
    private String avatar = "https://shopzz.oss-cn-guangzhou.aliyuncs.com/other/a1.jpg";

    @Schema(description = "姓名")
    private String realName;

    @Schema(description = "性别（0未知 1男 2女）")
    private Boolean gender;

    @Schema(description = "所属部门", example = "5")
    @NotNull(message = "所属部门不能为空")
    private Long deptId;

    @Schema(description = "手机号")
    private String mobile;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "谷歌验证码")
    private String googleCode;

    @Schema(description = "谷歌验证码是否开启，默认不开启 0-不开启； 1-开启")
    private Boolean googleStatus;

    @Schema(description = "所属角色")
    @NotEmpty(message = "所属角色不能为空")
    private List<Long> roleIds;

    @Schema(description = "状态（1：启用；2：禁用）", type = "Integer", allowableValues = {"1", "2"}, example = "1")
    private DisEnableStatusEnum status;
}