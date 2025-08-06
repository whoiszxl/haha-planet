package com.whoiszxl.user.model.resp;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.whoiszxl.starter.crud.model.BaseResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户基础信息详情信息
 *
 * @author whoiszxl
 */
@Data
@ExcelIgnoreUnannotated
@Schema(description = "用户基础信息详情信息")
public class UserInfoDetailResp extends BaseResponse {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户唯一标识
     */
    @Schema(description = "用户唯一标识")
    @ExcelProperty(value = "用户唯一标识")
    private String userCode;

    /**
     * 用户名
     */
    @Schema(description = "用户名")
    @ExcelProperty(value = "用户名")
    private String username;

    /**
     * 昵称
     */
    @Schema(description = "昵称")
    @ExcelProperty(value = "昵称")
    private String nickname;

    /**
     * 登录密码
     */
    @Schema(description = "登录密码")
    @ExcelProperty(value = "登录密码")
    private String password;

    /**
     * 头像URL
     */
    @Schema(description = "头像URL")
    @ExcelProperty(value = "头像URL")
    private String avatar;

    /**
     * 性别(0:未知 1:男 2:女)
     */
    @Schema(description = "性别(0:未知 1:男 2:女)")
    @ExcelProperty(value = "性别(0:未知 1:男 2:女)")
    private Boolean gender;

    /**
     * 生日
     */
    @Schema(description = "生日")
    @ExcelProperty(value = "生日")
    private LocalDateTime birthday;

    /**
     * 手机号
     */
    @Schema(description = "手机号")
    @ExcelProperty(value = "手机号")
    private String phone;

    /**
     * 邮箱
     */
    @Schema(description = "邮箱")
    @ExcelProperty(value = "邮箱")
    private String email;

    /**
     * 真实姓名
     */
    @Schema(description = "真实姓名")
    @ExcelProperty(value = "真实姓名")
    private String realName;

    /**
     * 身份证号(加密存储)
     */
    @Schema(description = "身份证号(加密存储)")
    @ExcelProperty(value = "身份证号(加密存储)")
    private String idCard;

    /**
     * 省份
     */
    @Schema(description = "省份")
    @ExcelProperty(value = "省份")
    private String province;

    /**
     * 城市
     */
    @Schema(description = "城市")
    @ExcelProperty(value = "城市")
    private String city;

    /**
     * 区县
     */
    @Schema(description = "区县")
    @ExcelProperty(value = "区县")
    private String district;

    /**
     * 详细地址
     */
    @Schema(description = "详细地址")
    @ExcelProperty(value = "详细地址")
    private String address;

    /**
     * 个人简介
     */
    @Schema(description = "个人简介")
    @ExcelProperty(value = "个人简介")
    private String bio;

    /**
     * 职业
     */
    @Schema(description = "职业")
    @ExcelProperty(value = "职业")
    private String profession;

    /**
     * 公司
     */
    @Schema(description = "公司")
    @ExcelProperty(value = "公司")
    private String company;

    /**
     * 学历
     */
    @Schema(description = "学历")
    @ExcelProperty(value = "学历")
    private String education;

    /**
     * 学校
     */
    @Schema(description = "学校")
    @ExcelProperty(value = "学校")
    private String school;

    /**
     * 用户类型(1:普通用户 2:认证用户 3:VIP用户 4:企业用户)
     */
    @Schema(description = "用户类型(1:普通用户 2:认证用户 3:VIP用户 4:企业用户)")
    @ExcelProperty(value = "用户类型(1:普通用户 2:认证用户 3:VIP用户 4:企业用户)")
    private Integer userType;

    /**
     * 用户等级
     */
    @Schema(description = "用户等级")
    @ExcelProperty(value = "用户等级")
    private Integer level;

    /**
     * 经验值
     */
    @Schema(description = "经验值")
    @ExcelProperty(value = "经验值")
    private Long experience;

    /**
     * 积分
     */
    @Schema(description = "积分")
    @ExcelProperty(value = "积分")
    private Long points;

    /**
     * 账户余额
     */
    @Schema(description = "账户余额")
    @ExcelProperty(value = "账户余额")
    private BigDecimal balance;

    /**
     * 粉丝数
     */
    @Schema(description = "粉丝数")
    @ExcelProperty(value = "粉丝数")
    private Integer followerCount;

    /**
     * 关注数
     */
    @Schema(description = "关注数")
    @ExcelProperty(value = "关注数")
    private Integer followingCount;

    /**
     * 发帖数
     */
    @Schema(description = "发帖数")
    @ExcelProperty(value = "发帖数")
    private Integer postCount;

    /**
     * 获赞数
     */
    @Schema(description = "获赞数")
    @ExcelProperty(value = "获赞数")
    private Integer likeCount;

    /**
     * 最后登录时间
     */
    @Schema(description = "最后登录时间")
    @ExcelProperty(value = "最后登录时间")
    private LocalDateTime lastLoginTime;

    /**
     * 最后登录IP
     */
    @Schema(description = "最后登录IP")
    @ExcelProperty(value = "最后登录IP")
    private String lastLoginIp;

    /**
     * 登录次数
     */
    @Schema(description = "登录次数")
    @ExcelProperty(value = "登录次数")
    private Integer loginCount;

    /**
     * 注册来源
     */
    @Schema(description = "注册来源")
    @ExcelProperty(value = "注册来源")
    private String registerSource;

    /**
     * 注册IP
     */
    @Schema(description = "注册IP")
    @ExcelProperty(value = "注册IP")
    private String registerIp;

    /**
     * 是否实名认证(0:未认证 1:已认证)
     */
    @Schema(description = "是否实名认证(0:未认证 1:已认证)")
    @ExcelProperty(value = "是否实名认证(0:未认证 1:已认证)")
    private Boolean isVerified;

    /**
     * 认证时间
     */
    @Schema(description = "认证时间")
    @ExcelProperty(value = "认证时间")
    private LocalDateTime verifiedTime;

    /**
     * 状态(0:无效 1:有效)
     */
    @Schema(description = "状态(0:无效 1:有效)")
    @ExcelProperty(value = "状态(0:无效 1:有效)")
    private Integer status;
}