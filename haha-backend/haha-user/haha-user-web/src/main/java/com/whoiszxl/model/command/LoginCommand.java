package com.whoiszxl.model.command;

import cn.hutool.core.lang.RegexPool;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.whoiszxl.enums.AuthTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;
import java.io.Serializable;

/**
 * 登录命令
 * @author whoiszxl
 */
@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "authType", visible = true)
@JsonSubTypes(
        {@JsonSubTypes.Type(value = LoginCommand.AccountLoginCommand.class, name = "ACCOUNT"),
        @JsonSubTypes.Type(value = LoginCommand.EmailLoginCommand.class, name = "EMAIL"),
        @JsonSubTypes.Type(value = LoginCommand.PhoneLoginCommand.class, name = "PHONE"),
        @JsonSubTypes.Type(value = LoginCommand.SocialLoginCommand.class, name = "SOCIAL")})
@Schema(description = "基础登录参数")
public class LoginCommand implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "客户端 key", example = "666")
    @NotBlank(message = "客户端 key 不能为空")
    private String clientKey;

    @Schema(description = "认证类型", example = "ACCOUNT")
    @NotNull(message = "认证类型非法")
    private AuthTypeEnum authType;

    @Data
    @Schema(description = "账号登录参数")
    public static class AccountLoginCommand extends LoginCommand {

        @Serial
        private static final long serialVersionUID = 1L;

        @Schema(description = "用户名", example = "zhangsan")
        @NotBlank(message = "用户名不能为空")
        private String username;

        @Schema(description = "密码（加密）", example = "123456")
        @NotBlank(message = "密码不能为空")
        private String password;

        @Schema(description = "验证码", example = "1234")
        private String captcha;

        @Schema(description = "验证码标识", example = "666")
        private String uuid;

        @Schema(description = "第三方绑定信息key", example = "666")
        private String bindKey;
    }

    @Data
    @Schema(description = "邮箱登录参数")
    public static class EmailLoginCommand extends LoginCommand {

        @Serial
        private static final long serialVersionUID = 1L;

        @Schema(description = "邮箱", example = "123456789@qq.com")
        @NotBlank(message = "邮箱不能为空")
        @Pattern(regexp = RegexPool.EMAIL, message = "邮箱格式错误")
        private String email;

        @Schema(description = "验证码", example = "888888")
        @NotBlank(message = "验证码不能为空")
        @Length(max = 6, message = "验证码非法")
        private String captcha;
    }

    @Data
    @Schema(description = "手机号登录参数")
    public static class PhoneLoginCommand extends LoginCommand {

        @Serial
        private static final long serialVersionUID = 1L;

        @Schema(description = "手机号", example = "13811111111")
        @NotBlank(message = "手机号不能为空")
        @Pattern(regexp = RegexPool.MOBILE, message = "手机号格式错误")
        private String phone;

        @Schema(description = "验证码", example = "8888")
        @NotBlank(message = "验证码不能为空")
        @Length(max = 4, message = "验证码非法")
        private String captcha;

        @Schema(description = "第三方绑定信息key", example = "666")
        private String bindKey;
    }


    @Data
    @Schema(description = "第三方账号登录参数")
    public static class SocialLoginCommand extends LoginCommand {

        @Serial
        private static final long serialVersionUID = 1L;

        @Schema(description = "第三方登录平台", example = "gitee")
        @NotBlank(message = "第三方登录平台不能为空")
        private String source;

        @Schema(description = "授权码", example = "666")
        @NotBlank(message = "授权码不能为空")
        private String code;

        @Schema(description = "状态码", example = "666")
        @NotBlank(message = "状态码不能为空")
        private String state;
    }

}
