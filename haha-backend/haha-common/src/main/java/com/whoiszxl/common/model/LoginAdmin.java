package com.whoiszxl.common.model;

import cn.hutool.core.collection.CollUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class LoginAdmin implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "账号")
    private String username;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "姓名")
    private String realName;

    @Schema(description = "性别（0未知 1男 2女）")
    private Integer gender;

    @Schema(description = "手机号")
    private String mobile;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "谷歌验证码是否开启，默认不开启 0-不开启； 1-开启")
    private Integer googleStatus;

    @Schema(description = "最后登录时间")
    private LocalDateTime lastLoginTime;

    @Schema(description = "令牌")
    private String token;

    @Schema(description = "登录ip地址")
    private String ip;

    @Schema(description = "登录地点")
    private String address;

    @Schema(description = "浏览器信息")
    private String browser;

    @Schema(description = "操作系统")
    private String os;

    @Schema(description = "部门ID")
    private Long deptId;

    @Schema(description = "权限集合")
    private Set<String> permissions;

    @Schema(description = "角色集合")
    private Set<String> roleCodes;

    @Schema(description = "角色集合")
    private Set<RoleDTO> roles;

    @Schema(description = "登录时间")
    private LocalDateTime loginTime;

    public boolean isAdmin() {
        if(CollUtil.isEmpty(roles)) {
            return false;
        }
        return roles.stream().map(RoleDTO::getCode).anyMatch("admin"::equals);
    }

}
