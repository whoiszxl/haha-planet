-- liquibase formatted sql

-- changeset whoiszxl:1
-- comment Google验证码相关表结构，适用于 haha-starter-captcha-google

-- 1. 用户密钥表
DROP TABLE IF EXISTS `google_captcha_secret`;
CREATE TABLE `google_captcha_secret` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id` varchar(64) NOT NULL COMMENT '用户标识',
    `secret_key` varchar(128) NOT NULL COMMENT '密钥（Base32编码）',
    `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
    `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_user_id` (`user_id`) USING BTREE,
    KEY `idx_expire_time` (`expire_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Google验证码密钥表';

-- 2. 已使用验证码表（防重放攻击）
DROP TABLE IF EXISTS `google_captcha_used_code`;
CREATE TABLE `google_captcha_used_code` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id` varchar(64) NOT NULL COMMENT '用户标识',
    `code` varchar(16) NOT NULL COMMENT '已使用的验证码',
    `expire_time` datetime NOT NULL COMMENT '过期时间',
    `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_user_code` (`user_id`, `code`) USING BTREE,
    KEY `idx_expire_time` (`expire_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Google验证码已使用记录表';

-- 3. 备用恢复码表
DROP TABLE IF EXISTS `google_captcha_backup_code`;
CREATE TABLE `google_captcha_backup_code` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id` varchar(64) NOT NULL COMMENT '用户标识',
    `backup_code` varchar(32) NOT NULL COMMENT '备用恢复码',
    `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
    `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_user_backup` (`user_id`, `backup_code`) USING BTREE,
    KEY `idx_user_id` (`user_id`) USING BTREE,
    KEY `idx_expire_time` (`expire_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Google验证码备用恢复码表';

-- 4. 验证失败记录表
DROP TABLE IF EXISTS `google_captcha_failure`;
CREATE TABLE `google_captcha_failure` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id` varchar(64) NOT NULL COMMENT '用户标识',
    `failure_count` bigint(20) NOT NULL DEFAULT '0' COMMENT '失败次数',
    `expire_time` datetime NOT NULL COMMENT '过期时间',
    `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_user_id` (`user_id`) USING BTREE,
    KEY `idx_expire_time` (`expire_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Google验证码失败记录表';