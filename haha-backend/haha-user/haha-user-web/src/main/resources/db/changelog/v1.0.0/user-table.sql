-- liquibase formatted sql

-- changeset whoiszxl:1
-- comment 用户模块: 初始化模块表结构

-- 用户基础信息表
DROP TABLE IF EXISTS `uc_user_info`;
CREATE TABLE `uc_user_info` (
    `id`                                bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_code`                         varchar(64) NOT NULL COMMENT '用户唯一标识',
    `username`                          varchar(64) NOT NULL COMMENT '用户名',
    `nickname`                          varchar(128) DEFAULT NULL COMMENT '昵称',
    `password`                          varchar(256) DEFAULT '' COMMENT '登录密码',
    `avatar`                            varchar(512) DEFAULT NULL COMMENT '头像URL',
    `gender`                            tinyint(1) DEFAULT 0 COMMENT '性别(0:未知 1:男 2:女)',
    `birthday`                          date DEFAULT NULL COMMENT '生日',
    `phone`                             varchar(32) DEFAULT NULL COMMENT '手机号',
    `email`                             varchar(128) DEFAULT NULL COMMENT '邮箱',
    `real_name`                         varchar(64) DEFAULT NULL COMMENT '真实姓名',
    `id_card`                           varchar(256) DEFAULT NULL COMMENT '身份证号(加密存储)',
    `province`                          varchar(32) DEFAULT NULL COMMENT '省份',
    `city`                              varchar(32) DEFAULT NULL COMMENT '城市',
    `district`                          varchar(32) DEFAULT NULL COMMENT '区县',
    `address`                           varchar(128) DEFAULT NULL COMMENT '详细地址',
    `bio`                               varchar(512) DEFAULT NULL COMMENT '个人简介',
    `profession`                        varchar(64) DEFAULT NULL COMMENT '职业',
    `company`                           varchar(64) DEFAULT NULL COMMENT '公司',
    `education`                         varchar(32) DEFAULT NULL COMMENT '学历',
    `school`                            varchar(64) DEFAULT NULL COMMENT '学校',
    `user_type`                         tinyint(2) DEFAULT 1 COMMENT '用户类型(1:普通用户 2:认证用户 3:VIP用户 4:企业用户)',
    `level`                             int(11) DEFAULT 1 COMMENT '用户等级',
    `experience`                        bigint(20) DEFAULT 0 COMMENT '经验值',
    `points`                            bigint(20) DEFAULT 0 COMMENT '积分',
    `balance`                           decimal(10,2) DEFAULT 0.00 COMMENT '账户余额',
    `follower_count`                    int(11) DEFAULT 0 COMMENT '粉丝数',
    `following_count`                   int(11) DEFAULT 0 COMMENT '关注数',
    `post_count`                        int(11) DEFAULT 0 COMMENT '发帖数',
    `like_count`                        int(11) DEFAULT 0 COMMENT '获赞数',
    `last_login_time`                   datetime DEFAULT NULL COMMENT '最后登录时间',
    `last_login_ip`                     varchar(64) DEFAULT NULL COMMENT '最后登录IP',
    `login_count`                       int(11) DEFAULT 0 COMMENT '登录次数',
    `register_source`                   varchar(64) DEFAULT NULL COMMENT '注册来源',
    `register_ip`                       varchar(64) DEFAULT NULL COMMENT '注册IP',
    `is_verified`                       tinyint(1) DEFAULT 0 COMMENT '是否实名认证(0:未认证 1:已认证)',
    `verified_time`                     datetime DEFAULT NULL COMMENT '认证时间',
    `status`                            tinyint(2) DEFAULT 1 COMMENT '状态(0:无效 1:有效)',
    `version`                           bigint(11) unsigned NOT NULL DEFAULT '1' COMMENT '乐观锁',
    `is_deleted`                        tinyint(3) DEFAULT 0 COMMENT '逻辑删除 1: 已删除, 0: 未删除',
    `created_by`                        varchar(64) NULL COMMENT '创建者',
    `updated_by`                        varchar(64) NULL COMMENT '更新者',
    `created_at`                        datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`                        datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_code` (`user_code`),
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_phone` (`phone`),
    UNIQUE KEY `uk_email` (`email`),
    KEY `idx_follower_count` (`follower_count`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户基础信息表';

-- 用户令牌表
DROP TABLE IF EXISTS `uc_user_token`;
CREATE TABLE `uc_user_token` (
    `id`                                bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id`                           bigint(20) NOT NULL COMMENT '用户ID',
    `token`                             varchar(512) NOT NULL DEFAULT '' COMMENT 'token',
    `token_type`                        varchar(32) DEFAULT 'access' COMMENT 'token类型(access:访问令牌 refresh:刷新令牌)',
    `source`                            varchar(64) DEFAULT 'web' COMMENT '来源平台(web:网页 app:移动端 api:接口)',
    `expires_time`                      datetime DEFAULT NULL COMMENT '过期时间',
    `login_ip`                          varchar(64) DEFAULT '' COMMENT '登录IP',
    `login_time`                        datetime DEFAULT NULL COMMENT '登录时间',
    `user_agent`                        varchar(512) DEFAULT NULL COMMENT '用户代理',
    `meta_json`                         text DEFAULT NULL COMMENT '附加信息',
    `status`                            tinyint(2) DEFAULT 1 COMMENT '状态(0:无效 1:有效)',
    `version`                           bigint(11) unsigned NOT NULL DEFAULT '1' COMMENT '乐观锁',
    `is_deleted`                        tinyint(3) DEFAULT 0 COMMENT '逻辑删除 1: 已删除, 0: 未删除',
    `created_by`                        varchar(64) NULL COMMENT '创建者',
    `updated_by`                        varchar(64) NULL COMMENT '更新者',
    `created_at`                        datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`                        datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_token` (`token`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_expires_time` (`expires_time`)
) ENGINE = InnoDB CHARSET = utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '用户令牌表';

-- 用户客户端表
DROP TABLE IF EXISTS `uc_user_client`;
CREATE TABLE `uc_user_client` (
    `id`                                bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `client_key`                        varchar(255) NOT NULL COMMENT 'key',
    `client_secret`                     varchar(255) NOT NULL COMMENT 'secret',
    `auth_type`                         json NOT NULL COMMENT '授权类型',
    `client_type`                       varchar(32) NOT NULL COMMENT '客户端类型',
    `active_timeout`                    bigint(20) DEFAULT -1 COMMENT 'Token最低活跃频率 单位:秒，-1:不限制，永不冻结）',
    `timeout`                           bigint(20) DEFAULT 2592000 COMMENT 'Token有效期 单位:秒，-1:永不过期）',
    `status`                            tinyint(2) DEFAULT 1 COMMENT '状态(0:无效 1:有效)',
    `version`                           bigint(11) unsigned NOT NULL DEFAULT '1' COMMENT '乐观锁',
    `is_deleted`                        tinyint(3) DEFAULT 0 COMMENT '逻辑删除 1: 已删除, 0: 未删除',
    `created_by`                        varchar(64) NULL COMMENT '创建者',
    `updated_by`                        varchar(64) NULL COMMENT '更新者',
    `created_at`                        datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`                        datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `uk_key`(`client_key`)
) ENGINE = InnoDB CHARSET = utf8mb4 COMMENT '用户客户端表';

-- 用户设置表
DROP TABLE IF EXISTS `uc_user_settings`;
CREATE TABLE `uc_user_settings` (
    `id`                                bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id`                           bigint(20) NOT NULL COMMENT '用户ID',
    `privacy_level`                     tinyint(2) DEFAULT 1 COMMENT '隐私级别(1:公开 2:好友可见 3:仅自己)',
    `allow_search`                      tinyint(1) DEFAULT 1 COMMENT '允许被搜索(0:不允许 1:允许)',
    `allow_friend_request`              tinyint(1) DEFAULT 1 COMMENT '允许好友申请(0:不允许 1:允许)',
    `allow_message`                     tinyint(1) DEFAULT 1 COMMENT '允许私信(0:不允许 1:允许)',
    `email_notification`                tinyint(1) DEFAULT 1 COMMENT '邮件通知(0:关闭 1:开启)',
    `sms_notification`                  tinyint(1) DEFAULT 1 COMMENT '短信通知(0:关闭 1:开启)',
    `push_notification`                 tinyint(1) DEFAULT 1 COMMENT '推送通知(0:关闭 1:开启)',
    `theme`                             varchar(20) DEFAULT 'default' COMMENT '主题设置',
    `language`                          varchar(10) DEFAULT 'zh-CN' COMMENT '语言设置',
    `timezone`                          varchar(50) DEFAULT 'Asia/Shanghai' COMMENT '时区设置',
    `status`                            tinyint(2) DEFAULT 1 COMMENT '状态(0:无效 1:有效)',
    `version`                           bigint(11) unsigned NOT NULL DEFAULT '1' COMMENT '乐观锁',
    `is_deleted`                        tinyint(3) DEFAULT 0 COMMENT '逻辑删除 1: 已删除, 0: 未删除',
    `created_by`                        varchar(64) NULL COMMENT '创建者',
    `updated_by`                        varchar(64) NULL COMMENT '更新者',
    `created_at`                        datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`                        datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户设置表';

-- 用户关注表(我关注的人)
DROP TABLE IF EXISTS `uc_user_following`;
CREATE TABLE `uc_user_following` (
    `id`                                bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id`                           bigint(20) NOT NULL COMMENT '用户ID(关注者)',
    `following_id`                      bigint(20) NOT NULL COMMENT '被关注者ID',
    `follow_type`                       tinyint(2) DEFAULT 1 COMMENT '关注类型(1:普通关注 2:特别关注)',
    `is_mutual`                         tinyint(1) DEFAULT 0 COMMENT '是否互相关注(0:否 1:是)',
    `is_cancelled`                      tinyint(1) DEFAULT 0 COMMENT '是否取消关注(0:未取消 1:已取消)',
    `status`                            tinyint(2) DEFAULT 1 COMMENT '状态(0:无效 1:有效)',
    `version`                           bigint(11) unsigned NOT NULL DEFAULT '1' COMMENT '乐观锁',
    `is_deleted`                        tinyint(3) DEFAULT 0 COMMENT '逻辑删除 1: 已删除, 0: 未删除',
    `created_by`                        varchar(64) NULL COMMENT '创建者',
    `updated_by`                        varchar(64) NULL COMMENT '更新者',
    `created_at`                        datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`                        datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_following` (`user_id`, `following_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_following_id` (`following_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户关注表(我关注的人)';

-- 用户粉丝表(关注我的人)
DROP TABLE IF EXISTS `uc_user_follower`;
CREATE TABLE `uc_user_follower` (
    `id`                                bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id`                           bigint(20) NOT NULL COMMENT '用户ID(被关注者)',
    `follower_id`                       bigint(20) NOT NULL COMMENT '关注者ID',
    `follow_type`                       tinyint(2) DEFAULT 1 COMMENT '关注类型(1:普通关注 2:特别关注)',
    `is_mutual`                         tinyint(1) DEFAULT 0 COMMENT '是否互相关注(0:否 1:是)',
    `is_cancelled`                      tinyint(1) DEFAULT 0 COMMENT '是否取消关注(0:未取消 1:已取消)',
    `status`                            tinyint(2) DEFAULT 1 COMMENT '状态(0:无效 1:有效)',
    `version`                           bigint(11) unsigned NOT NULL DEFAULT '1' COMMENT '乐观锁',
    `is_deleted`                        tinyint(3) DEFAULT 0 COMMENT '逻辑删除 1: 已删除, 0: 未删除',
    `created_by`                        varchar(64) NULL COMMENT '创建者',
    `updated_by`                        varchar(64) NULL COMMENT '更新者',
    `created_at`                        datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`                        datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_follower` (`user_id`, `follower_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_follower_id` (`follower_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户粉丝表(关注我的人)';

-- 用户等级表
DROP TABLE IF EXISTS `uc_user_level`;
CREATE TABLE `uc_user_level` (
    `id`                                bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `level`                             int(11) NOT NULL COMMENT '等级',
    `level_name`                        varchar(64) NOT NULL COMMENT '等级名称',
    `min_experience`                    bigint(20) NOT NULL COMMENT '最小经验值',
    `max_experience`                    bigint(20) NOT NULL COMMENT '最大经验值',
    `level_icon`                        varchar(512) DEFAULT NULL COMMENT '等级图标',
    `level_color`                       varchar(16) DEFAULT '#1890ff' COMMENT '等级颜色',
    `privileges`                        json DEFAULT NULL COMMENT '等级权益',
    `description`                       varchar(256) DEFAULT NULL COMMENT '等级描述',
    `status`                            tinyint(2) DEFAULT 1 COMMENT '状态(0:无效 1:有效)',
    `version`                           bigint(11) unsigned NOT NULL DEFAULT '1' COMMENT '乐观锁',
    `is_deleted`                        tinyint(3) DEFAULT 0 COMMENT '逻辑删除 1: 已删除, 0: 未删除',
    `created_by`                        varchar(64) NULL COMMENT '创建者',
    `updated_by`                        varchar(64) NULL COMMENT '更新者',
    `created_at`                        datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`                        datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_level` (`level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户等级表';

-- 积分记录表
DROP TABLE IF EXISTS `uc_points_record`;
CREATE TABLE `uc_points_record` (
    `id`                                bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id`                           bigint(20) NOT NULL COMMENT '用户ID',
    `points_change`                     int(11) NOT NULL COMMENT '积分变化(正数增加，负数减少)',
    `points_before`                     bigint(20) NOT NULL COMMENT '变化前积分',
    `points_after`                      bigint(20) NOT NULL COMMENT '变化后积分',
    `change_type`                       varchar(32) NOT NULL COMMENT '变化类型(sign:签到 post:发帖 like:点赞 comment:评论)',
    `change_reason`                     varchar(256) DEFAULT NULL COMMENT '变化原因',
    `related_id`                        varchar(64) DEFAULT NULL COMMENT '关联ID(如帖子ID、评论ID等)',
    `status`                            tinyint(2) DEFAULT 1 COMMENT '状态(0:无效 1:有效)',
    `version`                           bigint(11) unsigned NOT NULL DEFAULT '1' COMMENT '乐观锁',
    `is_deleted`                        tinyint(3) DEFAULT 0 COMMENT '逻辑删除 1: 已删除, 0: 未删除',
    `created_by`                        varchar(64) NULL COMMENT '创建者',
    `updated_by`                        varchar(64) NULL COMMENT '更新者',
    `created_at`                        datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`                        datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='积分记录表';