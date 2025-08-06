package com.whoiszxl.user.model.req;

import com.whoiszxl.starter.crud.model.BaseReq;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;

/**
 * 创建或修改用户基础信息信息
 *
 * @author whoiszxl
 */
@Data
@Schema(description = "创建或修改用户基础信息信息")
public class UserInfoReq extends BaseReq {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户唯一标识
     */
    @Schema(description = "用户唯一标识")
    @NotBlank(message = "用户唯一标识不能为空")
    @Length(max = 64, message = "用户唯一标识长度不能超过 {max} 个字符")
    private String userCode;

    /**
     * 用户名
     */
    @Schema(description = "用户名")
    @NotBlank(message = "用户名不能为空")
    @Length(max = 64, message = "用户名长度不能超过 {max} 个字符")
    private String username;

    /**
     * 乐观锁
     */
    @Schema(description = "乐观锁")
    @NotNull(message = "乐观锁不能为空")
    private Long version;
}