package com.whoiszxl.admin.cqrs.command;

import cn.hutool.core.lang.RegexPool;
import com.whoiszxl.common.constants.RegexConstants;
import com.whoiszxl.common.enums.DisEnableStatusEnum;
import com.whoiszxl.starter.crud.model.BaseReq;
import com.whoiszxl.starter.crud.utils.ValidateGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import java.util.List;

/**
 * @author whoiszxl
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "管理员新增或修改命令")
public class AdminRequest extends BaseReq {

    @Schema(description = "账号")
    @NotEmpty(message = "账号不能为空")
    @Pattern(regexp = RegexConstants.USERNAME, message = "用户名长度为 4-64 个字符，支持大小写字母、数字、下划线，以字母开头")
    private String username;

    @Schema(description = "昵称")
    @NotEmpty(message = "昵称不能为空")
    @Pattern(regexp = RegexConstants.GENERAL_NAME, message = "昵称长度为 2-30 个字符，支持中文、字母、数字、下划线，短横线")
    private String nickname;

    @NotBlank(message = "密码不能为空")
    @Schema(description = "密码")
    @NotBlank(message = "密码不能为空", groups = ValidateGroup.Crud.Add.class)
    private String password;

    @Schema(description = "头像")
    private String avatar = "https://shopzz.oss-cn-guangzhou.aliyuncs.com/other/a1.jpg";

    @Schema(description = "姓名")
    @Pattern(regexp = RegexConstants.GENERAL_NAME, message = "姓名长度为 2-30 个字符，支持中文、字母、数字、下划线，短横线")
    private String realName;

    @Schema(description = "性别（0：未知；1：男；2：女）", type = "Integer", allowableValues = {"0", "1", "2"}, example = "1")
    @NotNull(message = "性别非法")
    private Boolean gender;

    @Schema(description = "所属部门", example = "5")
    @NotNull(message = "所属部门不能为空")
    private Long deptId;

    @Schema(description = "手机号")
    @Pattern(regexp = "^$|" + RegexPool.MOBILE, message = "手机号码格式错误")
    private String mobile;

    @Schema(description = "邮箱")
    @Pattern(regexp = "^$|" + RegexPool.EMAIL, message = "邮箱格式错误")
    @Length(max = 255, message = "邮箱长度不能超过 {max} 个字符")
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