package com.whoiszxl.user.model.req;

import com.whoiszxl.starter.crud.model.BaseReq;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;

/**
 * 创建或修改用户客户端信息
 *
 * @author whoiszxl
 */
@Data
@Schema(description = "创建或修改用户客户端信息")
public class UserClientReq extends BaseReq {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * key
     */
    @Schema(description = "key")
    @NotBlank(message = "key不能为空")
    @Length(max = 255, message = "key长度不能超过 {max} 个字符")
    private String clientKey;

    /**
     * secret
     */
    @Schema(description = "secret")
    @NotBlank(message = "secret不能为空")
    @Length(max = 255, message = "secret长度不能超过 {max} 个字符")
    private String clientSecret;

    /**
     * 授权类型
     */
    @Schema(description = "授权类型")
    @NotBlank(message = "授权类型不能为空")
    @Length(max = 1073741824, message = "授权类型长度不能超过 {max} 个字符")
    private String authType;

    /**
     * 客户端类型
     */
    @Schema(description = "客户端类型")
    @NotBlank(message = "客户端类型不能为空")
    @Length(max = 32, message = "客户端类型长度不能超过 {max} 个字符")
    private String clientType;

    /**
     * 乐观锁
     */
    @Schema(description = "乐观锁")
    @NotNull(message = "乐观锁不能为空")
    private Long version;
}