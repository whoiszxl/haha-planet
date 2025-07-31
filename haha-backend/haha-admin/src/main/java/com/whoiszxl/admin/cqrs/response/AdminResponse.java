package com.whoiszxl.admin.cqrs.response;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.whoiszxl.starter.security.mask.annotation.JsonMask;
import com.whoiszxl.starter.security.mask.enums.MaskType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 管理员表
 * </p>
 *
 * @author whoiszxl
 * @since 2024-05-21
 */
@Data
@TableName("sys_admin")
public class AdminResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(name = "账号")
    private String username;

    @Schema(name = "昵称")
    private String nickname;

    @Schema(name = "密码")
    private String password;

    @Schema(name = "头像")
    private String avatar;

    @Schema(name = "姓名")
    private String realName;

    @Schema(name = "性别: 0-未知 1-男 2-女")
    private Integer gender;

    @Schema(name = "手机号")
    @JsonMask(MaskType.MOBILE_PHONE)
    private String mobile;

    @Schema(name = "邮箱")
    @JsonMask(MaskType.EMAIL)
    private String email;

    @Schema(name = "谷歌验证码")
    private String googleCode;

    @Schema(name = "谷歌验证码是否开启，默认不开启 0-不开启； 1-开启")
    private Boolean googleStatus;

    @Schema(name = "最后登录时间")
    private LocalDateTime lastLoginTime;

    @Schema(name = "部门ID")
    private Long deptId;

    @Schema(description = "所属部门", example = "霍格沃兹总部")
    private String deptName;

    @Schema(description = "角色ID集合", example = "2")
    private List<Long> roleIds;

    @Schema(description = "角色名称", example = "开发人员")
    private String roleNames;
}
