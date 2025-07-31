package com.whoiszxl.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.whoiszxl.common.enums.DisEnableStatusEnum;
import com.whoiszxl.common.mybatis.BCryptEncryptor;
import com.whoiszxl.starter.crud.model.entity.BaseDO;
import com.whoiszxl.starter.security.crypto.annotation.FieldEncrypt;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 管理员表
 * </p>
 *
 * @author whoiszxl
 * @since 2024-05-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_admin")
public class Admin extends BaseDO {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(name = "账号")
    private String username;

    @Schema(name = "昵称")
    private String nickname;

    @Schema(name = "密码")
    @FieldEncrypt(encryptor = BCryptEncryptor.class)
    private String password;

    @Schema(name = "头像")
    private String avatar;

    @Schema(name = "姓名")
    private String realName;

    @Schema(name = "性别: 0-未知 1-男 2-女")
    private Boolean gender;

    @Schema(name = "手机号")
    private String mobile;

    @Schema(name = "邮箱")
    private String email;

    @Schema(name = "谷歌验证码")
    private String googleCode;

    @Schema(name = "谷歌验证码是否开启，默认不开启 0-不开启； 1-开启")
    private Boolean googleStatus;

    @Schema(name = "最后登录时间")
    private LocalDateTime lastLoginTime;

    @Schema(name = "部门ID")
    private Long deptId;

    @Schema(name = "乐观锁")
    @Version
    private Long version;

    @Schema(description = "状态（1：启用；2：禁用）", type = "Integer", allowableValues = {"1", "2"}, example = "1")
    private DisEnableStatusEnum status;

    @Schema(name = "逻辑删除 1: 已删除， 0: 未删除")
    @TableLogic
    private Integer isDeleted;
}
