package com.whoiszxl.dto;

import lombok.Data;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户 Feign DTO
 * @author whoiszxl
 */
@Data
public class UserFeignDTO {

    /**
     * 用户唯一标识
     */
    private String userCode;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 性别(0:未知 1:男 2:女)
     */
    private Boolean gender;

    /**
     * 生日
     */
    private LocalDateTime birthday;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 身份证号(加密存储)
     */
    private String idCard;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 区县
     */
    private String district;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 个人简介
     */
    private String bio;

    /**
     * 职业
     */
    private String profession;

    /**
     * 公司
     */
    private String company;

    /**
     * 学历
     */
    private String education;

    /**
     * 学校
     */
    private String school;

    /**
     * 用户类型(1:普通用户 2:认证用户 3:VIP用户 4:企业用户)
     */
    private Integer userType;

    /**
     * 用户等级
     */
    private Integer level;

    /**
     * 经验值
     */
    private Long experience;

    /**
     * 积分
     */
    private Long points;

    /**
     * 账户余额
     */
    private BigDecimal balance;

    /**
     * 粉丝数
     */
    private Integer followerCount;

    /**
     * 关注数
     */
    private Integer followingCount;

    /**
     * 发帖数
     */
    private Integer postCount;

    /**
     * 获赞数
     */
    private Integer likeCount;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * 最后登录IP
     */
    private String lastLoginIp;

    /**
     * 登录次数
     */
    private Integer loginCount;

    /**
     * 注册来源
     */
    private String registerSource;

    /**
     * 注册IP
     */
    private String registerIp;

    /**
     * 是否实名认证(0:未认证 1:已认证)
     */
    private Boolean isVerified;

    /**
     * 认证时间
     */
    private LocalDateTime verifiedTime;
}
