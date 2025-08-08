package com.whoiszxl.model.command;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 会员基本信息更新命令
 *
 * @author whoiszxl
 */
@Data
@Schema(description = "会员基本信息更新命令")
public class MemberUpdateCommand implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "个性化URL")
    private String customUrl;

    @Schema(description = "个性化昵称")
    private String personaName;

    @Schema(description = "真实姓名")
    private String realName;

    @Schema(description = "个人简介")
    private String summary;

    @Schema(description = "微信号")
    private String wxCode;

    @Schema(description = "生日")
    private LocalDateTime birthday;

    @Schema(description = "国家")
    private String country;

    @Schema(description = "省份")
    private String province;

    @Schema(description = "城市")
    private String city;

    @Schema(description = "区域")
    private String district;

    @Schema(description = "性别: 1-男 2-女")
    private Integer gender;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "头像框")
    private String avatarFrame;
}