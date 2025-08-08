package com.whoiszxl.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户信息响应实体
 * @author whoiszxl
 */
@Data
public class UserInfoResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "用户ID", example = "123456")
    private Long id;

    @Schema(description = "用户唯一标识", example = "user_001")
    private String userCode;

    @Schema(description = "用户名", example = "exampleUser")
    private String username;

    @Schema(description = "昵称", example = "小明")
    private String nickname;

    @Schema(description = "头像URL", example = "https://example.com/avatar.jpg")
    private String avatar;

    @Schema(description = "性别(0:未知 1:男 2:女)", example = "1")
    private Boolean gender;

    @Schema(description = "生日", example = "2000-01-01T00:00:00")
    private LocalDateTime birthday;

    @Schema(description = "手机号", example = "13800138000")
    private String phone;

    @Schema(description = "邮箱", example = "example@example.com")
    private String email;

    @Schema(description = "真实姓名", example = "张三")
    private String realName;

    @Schema(description = "省份", example = "广东省")
    private String province;

    @Schema(description = "城市", example = "广州市")
    private String city;

    @Schema(description = "区县", example = "天河区")
    private String district;

    @Schema(description = "详细地址", example = "天河路123号")
    private String address;

    @Schema(description = "个人简介", example = "这是一个示例简介")
    private String bio;

    @Schema(description = "职业", example = "软件工程师")
    private String profession;

    @Schema(description = "公司", example = "某某科技有限公司")
    private String company;

    @Schema(description = "学历", example = "本科")
    private String education;

    @Schema(description = "学校", example = "某某大学")
    private String school;

    @Schema(description = "用户类型(1:普通用户 2:认证用户 3:VIP用户 4:企业用户)", example = "1")
    private Integer userType;

    @Schema(description = "用户等级", example = "1")
    private Integer level;

    @Schema(description = "经验值", example = "1000")
    private Long experience;

    @Schema(description = "积分", example = "1000")
    private Long points;

    @Schema(description = "账户余额", example = "100.00")
    private BigDecimal balance;

    @Schema(description = "粉丝数", example = "100")
    private Integer followerCount;

    @Schema(description = "关注数", example = "50")
    private Integer followingCount;

    @Schema(description = "发帖数", example = "20")
    private Integer postCount;

    @Schema(description = "获赞数", example = "200")
    private Integer likeCount;

    @Schema(description = "最后登录时间", example = "2023-10-01T12:34:56")
    private LocalDateTime lastLoginTime;

    @Schema(description = "最后登录IP", example = "192.168.1.2")
    private String lastLoginIp;

    @Schema(description = "登录次数", example = "10")
    private Integer loginCount;

    @Schema(description = "注册来源", example = "web")
    private String registerSource;

    @Schema(description = "注册IP", example = "192.168.1.1")
    private String registerIp;

    @Schema(description = "是否实名认证(0:未认证 1:已认证)", example = "1")
    private Boolean isVerified;

    @Schema(description = "认证时间", example = "2023-09-01T10:00:00")
    private LocalDateTime verifiedTime;

    @Schema(description = "状态(0:无效 1:有效)", example = "1")
    private Integer status;

}
