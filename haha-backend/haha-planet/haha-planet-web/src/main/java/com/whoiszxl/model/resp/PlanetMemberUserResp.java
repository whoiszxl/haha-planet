package com.whoiszxl.model.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 星球成员用户信息响应类
 *
 * @author whoiszxl
 */
@Data
@Schema(description = "星球成员用户信息响应")
public class PlanetMemberUserResp implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "用户编码")
    private String userCode;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "头像URL")
    private String avatar;

    @Schema(description = "成员类型: 1-普通成员 2-管理员 3-星球主")
    private Integer memberType;

    @Schema(description = "成员类型名称")
    private String memberTypeName;

    @Schema(description = "在星球中的昵称")
    private String planetNickname;

    /**
     * 获取成员类型名称
     */
    public String getMemberTypeName() {
        if (memberType == null) {
            return "未知";
        }
        return switch (memberType) {
            case 1 -> "普通成员";
            case 2 -> "管理员";
            case 3 -> "星球主";
            default -> "未知";
        };
    }
}
