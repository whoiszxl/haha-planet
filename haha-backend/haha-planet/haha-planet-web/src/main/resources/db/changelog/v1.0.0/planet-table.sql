-- liquibase formatted sql

-- changeset whoiszxl:1
-- comment 星球模块: 初始化模块表结构

DROP TABLE IF EXISTS `pla_planet`;
CREATE TABLE IF NOT EXISTS `pla_planet`(
    `id`                        bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `planet_code`               varchar(32) NOT NULL COMMENT '星球编码，唯一标识',
    `name`                      varchar(100) NOT NULL COMMENT '星球名称',
    `description`               text COMMENT '星球简介',
    `avatar`                    varchar(255) DEFAULT NULL COMMENT '星球头像',
    `cover_image`               varchar(255) DEFAULT NULL COMMENT '星球封面图',
    `owner_id`                  bigint(20) NOT NULL COMMENT '星球主ID',
    `category_id`               bigint(20) DEFAULT NULL COMMENT '分类ID',
    `tags`                      varchar(500) DEFAULT NULL COMMENT '标签，逗号分隔',
    `price_type`                tinyint(1) UNSIGNED DEFAULT 1 COMMENT '价格类型: 1-免费 2-付费',
    `price`                     decimal(10,2) DEFAULT 0.00 COMMENT '加入价格',
    `original_price`            decimal(10,2) DEFAULT 0.00 COMMENT '原价',
    `discount_price`            decimal(10,2) DEFAULT 0.00 COMMENT '优惠价',
    `discount_start_time`       datetime DEFAULT NULL COMMENT '优惠开始时间',
    `discount_end_time`         datetime DEFAULT NULL COMMENT '优惠结束时间',
    `join_type`                 tinyint(1) UNSIGNED DEFAULT 1 COMMENT '加入方式: 1-直接加入 2-申请审核 3-邀请制',
    `is_public`                 tinyint(1) UNSIGNED DEFAULT 1 COMMENT '是否公开: 0-私密 1-公开',
    `max_members`               int(11) DEFAULT 0 COMMENT '最大成员数，0表示无限制',
    `join_question`             text DEFAULT NULL COMMENT '加入问题',
    `auto_approve`              tinyint(1) UNSIGNED DEFAULT 1 COMMENT '是否自动通过申请: 0-否 1-是',
    `allow_member_post`         tinyint(1) UNSIGNED DEFAULT 1 COMMENT '是否允许成员发帖: 0-否 1-是',
    `post_need_approve`         tinyint(1) UNSIGNED DEFAULT 0 COMMENT '发帖是否需要审核: 0-否 1-是',
    `allow_anonymous`           tinyint(1) UNSIGNED DEFAULT 0 COMMENT '是否允许匿名发帖: 0-否 1-是',
    `watermark_enabled`         tinyint(1) UNSIGNED DEFAULT 0 COMMENT '是否开启水印: 0-否 1-是',
    `member_count`              int(11) DEFAULT 0 COMMENT '成员数量',
    `post_count`                int(11) DEFAULT 0 COMMENT '帖子数量',
    `view_count`                int(11) DEFAULT 0 COMMENT '浏览次数',
    `like_count`                int(11) DEFAULT 0 COMMENT '点赞数',
    `share_count`               int(11) DEFAULT 0 COMMENT '分享次数',
    `total_income`              decimal(15,2) DEFAULT 0.00 COMMENT '总收入',
    `hot_score`                 int(11) DEFAULT 0 COMMENT '热度分数',
    `quality_score`             decimal(3,1) DEFAULT 0.0 COMMENT '质量评分',
    `last_active_time`          datetime DEFAULT NULL COMMENT '最后活跃时间',
    `recommend_weight`          int(11) DEFAULT 0 COMMENT '推荐权重',
    `is_featured`               tinyint(1) UNSIGNED DEFAULT 0 COMMENT '是否精选: 0-否 1-是',
    `is_official`               tinyint(1) UNSIGNED DEFAULT 0 COMMENT '是否官方: 0-否 1-是',
    `valid_start_time`          datetime DEFAULT NULL COMMENT '有效开始时间',
    `valid_end_time`            datetime DEFAULT NULL COMMENT '有效结束时间',
    `close_reason`              varchar(255) DEFAULT NULL COMMENT '关闭原因',
    `extra_config`              json DEFAULT NULL COMMENT '扩展配置，JSON格式',
    `notice`                    text DEFAULT NULL COMMENT '星球公告',
    `rules`                     text DEFAULT NULL COMMENT '星球规则',
    `version`                   bigint(20) unsigned NOT NULL DEFAULT '1' COMMENT '乐观锁',
    `status`                    tinyint(1) UNSIGNED DEFAULT 1 COMMENT '业务状态: 0-禁用 1-启用 2-审核中 3-已关闭',
    `is_deleted`                tinyint(1) UNSIGNED DEFAULT 0 COMMENT '逻辑删除 1: 已删除， 0: 未删除',
    `created_by`                bigint(20) NOT NULL COMMENT '创建者',
    `updated_by`                bigint(20) DEFAULT NULL COMMENT '更新者',
    `created_at`                datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`                datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_planet_code`(`planet_code`) USING BTREE,
    INDEX `idx_owner_id`(`owner_id`) USING BTREE,
    INDEX `idx_category_id`(`category_id`) USING BTREE,
    INDEX `idx_price_type`(`price_type`) USING BTREE,
    INDEX `idx_status`(`status`) USING BTREE,
    INDEX `idx_is_public`(`is_public`) USING BTREE,
    INDEX `idx_hot_score`(`hot_score`) USING BTREE,
    INDEX `idx_created_at`(`created_at`) USING BTREE,
    INDEX `idx_member_count`(`member_count`) USING BTREE
) ENGINE = InnoDB CHARSET = utf8mb4 COMMENT = '星球表' ROW_FORMAT = Dynamic;


DROP TABLE IF EXISTS `pla_planet_category`;
CREATE TABLE IF NOT EXISTS `pla_planet_category`(
    `id`                        bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `category_name`             varchar(64) NOT NULL COMMENT '分类名称',
    `icon_url`                  varchar(255) NOT NULL COMMENT '分类图标链接',
    `parent_id`                 bigint(20) DEFAULT 0 COMMENT '父分类ID，0表示顶级分类',
    `level`                     tinyint(1) DEFAULT 1 COMMENT '分类级别:1->1级; 2->2级 3->3级',
    `sort`                      int(6) DEFAULT 1 COMMENT '排序,数值越大越靠前',
    `version`                   bigint(20) unsigned NOT NULL DEFAULT '1' COMMENT '乐观锁',
    `status`                    tinyint(1) UNSIGNED DEFAULT 1 COMMENT '业务状态',
    `is_deleted`                tinyint(1) UNSIGNED DEFAULT 0 COMMENT '逻辑删除 1: 已删除， 0: 未删除',
    `created_by`                bigint(20) NOT NULL COMMENT '创建者',
    `updated_by`                bigint(20) DEFAULT NULL COMMENT '更新者',
    `created_at`                datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`                datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_parent_id`(`parent_id`) USING BTREE,
    INDEX `idx_sort`(`sort`) USING BTREE
) ENGINE = InnoDB CHARSET = utf8mb4 COMMENT = '星球分类表' ROW_FORMAT = Dynamic;


DROP TABLE IF EXISTS `pla_planet_member`;
CREATE TABLE IF NOT EXISTS `pla_planet_member`(
    `id`                        bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `planet_id`                 bigint(20) NOT NULL COMMENT '星球ID',
    `user_id`                   bigint(20) NOT NULL COMMENT '用户ID',
    `member_type`               tinyint(1) UNSIGNED DEFAULT 1 COMMENT '成员类型: 1-普通成员 2-管理员 3-星球主',
    `join_time`                 datetime DEFAULT CURRENT_TIMESTAMP COMMENT '加入时间',
    `expire_time`               datetime DEFAULT NULL COMMENT '到期时间',
    `join_source`               tinyint(1) UNSIGNED DEFAULT 1 COMMENT '加入来源: 1-直接加入 2-邀请 3-分享',
    `inviter_id`                bigint(20) DEFAULT NULL COMMENT '邀请人ID',
    `order_id`                  bigint(20) DEFAULT NULL COMMENT '订单ID',
    `nickname`                  varchar(50) DEFAULT NULL COMMENT '在星球中的昵称',
    `is_muted`                  tinyint(1) UNSIGNED DEFAULT 0 COMMENT '是否被禁言: 0-否 1-是',
    `mute_end_time`             datetime DEFAULT NULL COMMENT '禁言结束时间',
    `last_read_time`            datetime DEFAULT NULL COMMENT '最后阅读时间',
    `total_posts`               int(11) DEFAULT 0 COMMENT '总发帖数',
    `total_likes`               int(11) DEFAULT 0 COMMENT '总获赞数',
    `version`                   bigint(20) unsigned NOT NULL DEFAULT '1' COMMENT '乐观锁',
    `status`                    tinyint(1) UNSIGNED DEFAULT 1 COMMENT '业务状态: 1-正常 2-已退出',
    `is_deleted`                tinyint(1) UNSIGNED DEFAULT 0 COMMENT '逻辑删除 1: 已删除， 0: 未删除',
    `created_by`                bigint(20) NOT NULL COMMENT '创建者',
    `updated_by`                bigint(20) DEFAULT NULL COMMENT '更新者',
    `created_at`                datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`                datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_planet_user`(`planet_id`, `user_id`) USING BTREE,
    INDEX `idx_user_id`(`user_id`) USING BTREE,
    INDEX `idx_member_type`(`member_type`) USING BTREE,
    INDEX `idx_join_time`(`join_time`) USING BTREE,
    INDEX `idx_expire_time`(`expire_time`) USING BTREE
) ENGINE = InnoDB CHARSET = utf8mb4 COMMENT = '星球成员表' ROW_FORMAT = Dynamic;


DROP TABLE IF EXISTS `pla_planet_post`;
CREATE TABLE IF NOT EXISTS `pla_planet_post`(
    `id`                        bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `planet_id`                 bigint(20) NOT NULL COMMENT '星球ID',
    `user_id`                   bigint(20) NOT NULL COMMENT '发帖用户ID',
    `title`                     varchar(200) DEFAULT NULL COMMENT '帖子标题',
    `summary`                   varchar(500) NOT NULL COMMENT '帖子概要',
    `content_type`              tinyint(1) UNSIGNED DEFAULT 1 COMMENT '内容类型: 1-主题 2-文章',
    `media_urls`                json DEFAULT NULL COMMENT '媒体文件URLs, JSON数组',
    `is_anonymous`              tinyint(1) UNSIGNED DEFAULT 0 COMMENT '是否匿名发帖: 0-否 1-是',
    `is_top`                    tinyint(1) UNSIGNED DEFAULT 0 COMMENT '是否置顶: 0-否 1-是',
    `is_essence`                tinyint(1) UNSIGNED DEFAULT 0 COMMENT '是否精华: 0-否 1-是',
    `view_count`                int(11) DEFAULT 0 COMMENT '浏览次数',
    `like_count`                int(11) DEFAULT 0 COMMENT '点赞数',
    `comment_count`             int(11) DEFAULT 0 COMMENT '评论数',
    `share_count`               int(11) DEFAULT 0 COMMENT '分享次数',
    `collect_count`             int(11) DEFAULT 0 COMMENT '收藏次数',
    `audit_status`              tinyint(1) UNSIGNED DEFAULT 1 COMMENT '审核状态: 1-待审核 2-审核通过 3-审核拒绝',
    `audit_reason`              varchar(255) DEFAULT NULL COMMENT '审核原因',
    `audit_time`                datetime DEFAULT NULL COMMENT '审核时间',
    `audit_by`                  bigint(20) DEFAULT NULL COMMENT '审核人',
    `last_comment_time`         datetime DEFAULT NULL COMMENT '最后评论时间',
    `hot_score`                 int(11) DEFAULT 0 COMMENT '热度分数',
    `version`                   bigint(20) unsigned NOT NULL DEFAULT '1' COMMENT '乐观锁',
    `status`                    tinyint(1) UNSIGNED DEFAULT 1 COMMENT '业务状态: 1-正常 2-已删除 3-已隐藏',
    `is_deleted`                tinyint(1) UNSIGNED DEFAULT 0 COMMENT '逻辑删除 1: 已删除， 0: 未删除',
    `created_by`                bigint(20) NOT NULL COMMENT '创建者',
    `updated_by`                bigint(20) DEFAULT NULL COMMENT '更新者',
    `created_at`                datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`                datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_planet_id`(`planet_id`) USING BTREE,
    INDEX `idx_user_id`(`user_id`) USING BTREE,
    INDEX `idx_audit_status`(`audit_status`) USING BTREE,
    INDEX `idx_is_top`(`is_top`) USING BTREE,
    INDEX `idx_hot_score`(`hot_score`) USING BTREE,
    INDEX `idx_created_at`(`created_at`) USING BTREE,
    INDEX `idx_last_comment_time`(`last_comment_time`) USING BTREE
) ENGINE = InnoDB CHARSET = utf8mb4 COMMENT = '星球帖子表' ROW_FORMAT = Dynamic;


DROP TABLE IF EXISTS `pla_planet_comment`;
CREATE TABLE IF NOT EXISTS `pla_planet_comment`(
    `id`                        bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `post_id`                   bigint(20) NOT NULL COMMENT '帖子ID',
    `planet_id`                 bigint(20) NOT NULL COMMENT '星球ID',
    `user_id`                   bigint(20) NOT NULL COMMENT '评论用户ID',
    `parent_id`                 bigint(20) DEFAULT 0 COMMENT '父评论ID，0表示顶级评论',
    `reply_to_user_id`          bigint(20) DEFAULT NULL COMMENT '回复的用户ID',
    `content`                   text NOT NULL COMMENT '评论内容',
    `media_urls`                json DEFAULT NULL COMMENT '媒体文件URLs',
    `is_anonymous`              tinyint(1) UNSIGNED DEFAULT 0 COMMENT '是否匿名评论: 0-否 1-是',
    `like_count`                int(11) DEFAULT 0 COMMENT '点赞数',
    `reply_count`               int(11) DEFAULT 0 COMMENT '回复数',
    `audit_status`              tinyint(1) UNSIGNED DEFAULT 2 COMMENT '审核状态: 1-待审核 2-审核通过 3-审核拒绝',
    `version`                   bigint(20) unsigned NOT NULL DEFAULT '1' COMMENT '乐观锁',
    `status`                    tinyint(1) UNSIGNED DEFAULT 1 COMMENT '业务状态',
    `is_deleted`                tinyint(1) UNSIGNED DEFAULT 0 COMMENT '逻辑删除 1: 已删除， 0: 未删除',
    `created_by`                bigint(20) NOT NULL COMMENT '创建者',
    `updated_by`                bigint(20) DEFAULT NULL COMMENT '更新者',
    `created_at`                datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`                datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_post_id`(`post_id`) USING BTREE,
    INDEX `idx_planet_id`(`planet_id`) USING BTREE,
    INDEX `idx_user_id`(`user_id`) USING BTREE,
    INDEX `idx_parent_id`(`parent_id`) USING BTREE,
    INDEX `idx_created_at`(`created_at`) USING BTREE
) ENGINE = InnoDB CHARSET = utf8mb4 COMMENT = '帖子评论表' ROW_FORMAT = Dynamic;

-- 点赞记录表
DROP TABLE IF EXISTS `pla_planet_like`;
CREATE TABLE IF NOT EXISTS `pla_planet_like`(
    `id`                        bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `planet_id`                 bigint(20) NOT NULL COMMENT '星球ID',
    `user_id`                   bigint(20) NOT NULL COMMENT '用户ID',
    `target_type`               tinyint(1) UNSIGNED NOT NULL COMMENT '点赞目标类型: 1-帖子 2-评论',
    `target_id`                 bigint(20) NOT NULL COMMENT '目标ID',
    `target_user_id`            bigint(20) NOT NULL COMMENT '被点赞的用户ID',
    `version`                   bigint(20) unsigned NOT NULL DEFAULT '1' COMMENT '乐观锁',
    `status`                    tinyint(1) UNSIGNED DEFAULT 1 COMMENT '业务状态',
    `is_deleted`                tinyint(1) UNSIGNED DEFAULT 0 COMMENT '逻辑删除 1: 已删除， 0: 未删除',
    `created_by`                bigint(20) NOT NULL COMMENT '创建者',
    `updated_by`                bigint(20) DEFAULT NULL COMMENT '更新者',
    `created_at`                datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`                datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_user_target`(`user_id`, `target_type`, `target_id`) USING BTREE,
    INDEX `idx_planet_id`(`planet_id`) USING BTREE,
    INDEX `idx_target`(`target_type`, `target_id`) USING BTREE,
    INDEX `idx_target_user_id`(`target_user_id`) USING BTREE
) ENGINE = InnoDB CHARSET = utf8mb4 COMMENT = '点赞记录表' ROW_FORMAT = Dynamic;

-- 收藏记录表
DROP TABLE IF EXISTS `pla_planet_collect`;
CREATE TABLE IF NOT EXISTS `pla_planet_collect`(
    `id`                        bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `planet_id`                 bigint(20) NOT NULL COMMENT '星球ID',
    `user_id`                   bigint(20) NOT NULL COMMENT '用户ID',
    `post_id`                   bigint(20) NOT NULL COMMENT '帖子ID',
    `folder_name`               varchar(50) DEFAULT '默认收藏夹' COMMENT '收藏夹名称',
    `version`                   bigint(20) unsigned NOT NULL DEFAULT '1' COMMENT '乐观锁',
    `status`                    tinyint(1) UNSIGNED DEFAULT 1 COMMENT '业务状态',
    `is_deleted`                tinyint(1) UNSIGNED DEFAULT 0 COMMENT '逻辑删除 1: 已删除， 0: 未删除',
    `created_by`                bigint(20) NOT NULL COMMENT '创建者',
    `updated_by`                bigint(20) DEFAULT NULL COMMENT '更新者',
    `created_at`                datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`                datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_user_post`(`user_id`, `post_id`) USING BTREE,
    INDEX `idx_planet_id`(`planet_id`) USING BTREE,
    INDEX `idx_folder_name`(`folder_name`) USING BTREE
) ENGINE = InnoDB CHARSET = utf8mb4 COMMENT = '收藏记录表' ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `pla_planet_apply`;
CREATE TABLE IF NOT EXISTS `pla_planet_apply`(
    `id`                        bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `planet_id`                 bigint(20) NOT NULL COMMENT '星球ID',
    `user_id`                   bigint(20) NOT NULL COMMENT '申请用户ID',
    `apply_reason`              text DEFAULT NULL COMMENT '申请理由',
    `answer`                    text DEFAULT NULL COMMENT '问题答案',
    `apply_status`              tinyint(1) UNSIGNED DEFAULT 1 COMMENT '申请状态: 1-待审核 2-已通过 3-已拒绝',
    `audit_reason`              varchar(255) DEFAULT NULL COMMENT '审核原因',
    `audit_time`                datetime DEFAULT NULL COMMENT '审核时间',
    `audit_by`                  bigint(20) DEFAULT NULL COMMENT '审核人',
    `version`                   bigint(20) unsigned NOT NULL DEFAULT '1' COMMENT '乐观锁',
    `status`                    tinyint(1) UNSIGNED DEFAULT 1 COMMENT '业务状态',
    `is_deleted`                tinyint(1) UNSIGNED DEFAULT 0 COMMENT '逻辑删除 1: 已删除， 0: 未删除',
    `created_by`                bigint(20) NOT NULL COMMENT '创建者',
    `updated_by`                bigint(20) DEFAULT NULL COMMENT '更新者',
    `created_at`                datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`                datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_planet_user`(`planet_id`, `user_id`) USING BTREE,
    INDEX `idx_apply_status`(`apply_status`) USING BTREE,
    INDEX `idx_audit_by`(`audit_by`) USING BTREE
) ENGINE = InnoDB CHARSET = utf8mb4 COMMENT = '星球申请表' ROW_FORMAT = Dynamic;

-- 星球邀请表
DROP TABLE IF EXISTS `pla_planet_invite`;
CREATE TABLE IF NOT EXISTS `pla_planet_invite`(
    `id`                        bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `planet_id`                 bigint(20) NOT NULL COMMENT '星球ID',
    `inviter_id`                bigint(20) NOT NULL COMMENT '邀请人ID',
    `invitee_id`                bigint(20) DEFAULT NULL COMMENT '被邀请人ID',
    `invite_code`               varchar(32) NOT NULL COMMENT '邀请码',
    `invite_type`               tinyint(1) UNSIGNED DEFAULT 1 COMMENT '邀请类型: 1-邀请码 2-链接邀请',
    `invite_message`            varchar(255) DEFAULT NULL COMMENT '邀请消息',
    `expire_time`               datetime DEFAULT NULL COMMENT '过期时间',
    `max_use_count`             int(11) DEFAULT 1 COMMENT '最大使用次数',
    `used_count`                int(11) DEFAULT 0 COMMENT '已使用次数',
    `invite_status`             tinyint(1) UNSIGNED DEFAULT 1 COMMENT '邀请状态: 1-有效 2-已使用 3-已过期 4-已取消',
    `version`                   bigint(20) unsigned NOT NULL DEFAULT '1' COMMENT '乐观锁',
    `status`                    tinyint(1) UNSIGNED DEFAULT 1 COMMENT '业务状态',
    `is_deleted`                tinyint(1) UNSIGNED DEFAULT 0 COMMENT '逻辑删除 1: 已删除， 0: 未删除',
    `created_by`                bigint(20) NOT NULL COMMENT '创建者',
    `updated_by`                bigint(20) DEFAULT NULL COMMENT '更新者',
    `created_at`                datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`                datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_invite_code`(`invite_code`) USING BTREE,
    INDEX `idx_planet_id`(`planet_id`) USING BTREE,
    INDEX `idx_inviter_id`(`inviter_id`) USING BTREE,
    INDEX `idx_invitee_id`(`invitee_id`) USING BTREE,
    INDEX `idx_invite_status`(`invite_status`) USING BTREE
) ENGINE = InnoDB CHARSET = utf8mb4 COMMENT = '星球邀请表' ROW_FORMAT = Dynamic;

-- 星球标签表
DROP TABLE IF EXISTS `pla_planet_tag`;
CREATE TABLE IF NOT EXISTS `pla_planet_tag`(
    `id`                        bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`                      varchar(50) NOT NULL COMMENT '标签名称',
    `color`                     varchar(7) DEFAULT '#1890ff' COMMENT '标签颜色',
    `use_count`                 int(11) DEFAULT 0 COMMENT '使用次数',
    `category_id`               bigint(20) DEFAULT NULL COMMENT '所属分类ID',
    `version`                   bigint(20) unsigned NOT NULL DEFAULT '1' COMMENT '乐观锁',
    `status`                    tinyint(1) UNSIGNED DEFAULT 1 COMMENT '业务状态',
    `is_deleted`                tinyint(1) UNSIGNED DEFAULT 0 COMMENT '逻辑删除 1: 已删除， 0: 未删除',
    `created_by`                bigint(20) NOT NULL COMMENT '创建者',
    `updated_by`                bigint(20) DEFAULT NULL COMMENT '更新者',
    `created_at`                datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`                datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_name`(`name`) USING BTREE,
    INDEX `idx_category_id`(`category_id`) USING BTREE,
    INDEX `idx_use_count`(`use_count`) USING BTREE
) ENGINE = InnoDB CHARSET = utf8mb4 COMMENT = '星球标签表' ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `pla_planet_tag_relation`;
CREATE TABLE IF NOT EXISTS `pla_planet_tag_relation`(
    `id`                        bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `planet_id`                 bigint(20) NOT NULL COMMENT '星球ID',
    `tag_id`                    bigint(20) NOT NULL COMMENT '标签ID',
    `version`                   bigint(20) unsigned NOT NULL DEFAULT '1' COMMENT '乐观锁',
    `status`                    tinyint(1) UNSIGNED DEFAULT 1 COMMENT '业务状态',
    `is_deleted`                tinyint(1) UNSIGNED DEFAULT 0 COMMENT '逻辑删除 1: 已删除， 0: 未删除',
    `created_by`                bigint(20) NOT NULL COMMENT '创建者',
    `updated_by`                bigint(20) DEFAULT NULL COMMENT '更新者',
    `created_at`                datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`                datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_planet_tag`(`planet_id`, `tag_id`) USING BTREE,
    INDEX `idx_tag_id`(`tag_id`) USING BTREE
) ENGINE = InnoDB CHARSET = utf8mb4 COMMENT = '星球标签关联表' ROW_FORMAT = Dynamic;

-- 星球通知表
DROP TABLE IF EXISTS `pla_planet_notification`;
CREATE TABLE IF NOT EXISTS `pla_planet_notification`(
    `id`                        bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `planet_id`                 bigint(20) NOT NULL COMMENT '星球ID',
    `user_id`                   bigint(20) NOT NULL COMMENT '接收通知的用户ID',
    `sender_id`                 bigint(20) DEFAULT NULL COMMENT '发送者ID',
    `notification_type`         tinyint(1) UNSIGNED NOT NULL COMMENT '通知类型: 1-新帖子 2-新评论 3-点赞 4-关注 5-系统通知 6-星球公告',
    `title`                     varchar(100) NOT NULL COMMENT '通知标题',
    `content`                   text DEFAULT NULL COMMENT '通知内容',
    `target_type`               tinyint(1) UNSIGNED DEFAULT NULL COMMENT '关联目标类型: 1-帖子 2-评论 3-星球',
    `target_id`                 bigint(20) DEFAULT NULL COMMENT '关联目标ID',
    `is_read`                   tinyint(1) UNSIGNED DEFAULT 0 COMMENT '是否已读: 0-未读 1-已读',
    `read_time`                 datetime DEFAULT NULL COMMENT '阅读时间',
    `version`                   bigint(20) unsigned NOT NULL DEFAULT '1' COMMENT '乐观锁',
    `status`                    tinyint(1) UNSIGNED DEFAULT 1 COMMENT '业务状态',
    `is_deleted`                tinyint(1) UNSIGNED DEFAULT 0 COMMENT '逻辑删除 1: 已删除， 0: 未删除',
    `created_by`                bigint(20) NOT NULL COMMENT '创建者',
    `updated_by`                bigint(20) DEFAULT NULL COMMENT '更新者',
    `created_at`                datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`                datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_planet_id`(`planet_id`) USING BTREE,
    INDEX `idx_user_id`(`user_id`) USING BTREE,
    INDEX `idx_notification_type`(`notification_type`) USING BTREE,
    INDEX `idx_is_read`(`is_read`) USING BTREE,
    INDEX `idx_created_at`(`created_at`) USING BTREE
) ENGINE = InnoDB CHARSET = utf8mb4 COMMENT = '星球通知表' ROW_FORMAT = Dynamic;

-- 星球设置表
DROP TABLE IF EXISTS `pla_planet_setting`;
CREATE TABLE IF NOT EXISTS `pla_planet_setting`(
    `id`                        bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `planet_id`                 bigint(20) NOT NULL COMMENT '星球ID',
    `setting_key`               varchar(50) NOT NULL COMMENT '设置键',
    `setting_value`             text DEFAULT NULL COMMENT '设置值',
    `setting_type`              tinyint(1) UNSIGNED DEFAULT 1 COMMENT '设置类型: 1-字符串 2-数字 3-布尔 4-JSON',
    `description`               varchar(255) DEFAULT NULL COMMENT '设置描述',
    `version`                   bigint(20) unsigned NOT NULL DEFAULT '1' COMMENT '乐观锁',
    `status`                    tinyint(1) UNSIGNED DEFAULT 1 COMMENT '业务状态',
    `is_deleted`                tinyint(1) UNSIGNED DEFAULT 0 COMMENT '逻辑删除 1: 已删除， 0: 未删除',
    `created_by`                bigint(20) NOT NULL COMMENT '创建者',
    `updated_by`                bigint(20) DEFAULT NULL COMMENT '更新者',
    `created_at`                datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`                datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_planet_key`(`planet_id`, `setting_key`) USING BTREE
) ENGINE = InnoDB CHARSET = utf8mb4 COMMENT = '星球设置表' ROW_FORMAT = Dynamic;

-- 星球帖子文章扩展表
DROP TABLE IF EXISTS `pla_planet_post_article`;
CREATE TABLE IF NOT EXISTS `pla_planet_post_article`(
    `id`                        bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `post_id`                   bigint(20) NOT NULL COMMENT '帖子ID',
    `content`                   longtext DEFAULT NULL COMMENT '文章内容',
    `cover_image`               varchar(255) DEFAULT NULL COMMENT '封面图片URL',
    `tags`                      varchar(255) DEFAULT NULL COMMENT '文章标签，逗号分隔',
    `word_count`                int(11) DEFAULT 0 COMMENT '字数统计',
    `reading_time`              int(11) DEFAULT 0 COMMENT '预估阅读时间（分钟）',
    `is_original`               tinyint(1) UNSIGNED DEFAULT 1 COMMENT '是否原创: 0-否 1-是',
    `source_url`                varchar(255) DEFAULT NULL COMMENT '来源链接',
    `version`                   bigint(20) unsigned NOT NULL DEFAULT '1' COMMENT '乐观锁',
    `status`                    tinyint(1) UNSIGNED DEFAULT 1 COMMENT '业务状态: 1-正常 2-已删除',
    `is_deleted`                tinyint(1) UNSIGNED DEFAULT 0 COMMENT '逻辑删除 1: 已删除， 0: 未删除',
    `created_by`                bigint(20) NOT NULL COMMENT '创建者',
    `updated_by`                bigint(20) DEFAULT NULL COMMENT '更新者',
    `created_at`                datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`                datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_post_id`(`post_id`) USING BTREE,
    INDEX `idx_created_at`(`created_at`) USING BTREE
) ENGINE = InnoDB CHARSET = utf8mb4 COMMENT = '星球帖子文章扩展表' ROW_FORMAT = Dynamic;