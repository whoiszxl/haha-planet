-- liquibase formatted sql

-- changeset whoiszxl:1
-- comment 初始化admin模块的表结构

DROP TABLE IF EXISTS `sys_admin`;
CREATE TABLE IF NOT EXISTS `sys_admin`(
    `id`                        bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `username`                  varchar(32) NOT NULL COMMENT '账号',
    `nickname`                  varchar(32) NOT NULL COMMENT '昵称',
    `password`                  varchar(255) NOT NULL COMMENT '密码',
    `avatar`                    varchar(255) DEFAULT NULL COMMENT '头像',
    `real_name`                 varchar(32) DEFAULT NULL COMMENT '姓名',
    `gender`                    tinyint(1) UNSIGNED DEFAULT 0 COMMENT '性别: 0-未知 1-男 2-女',
    `mobile`                    varchar(16) DEFAULT NULL COMMENT '手机号',
    `email`                     varchar(128) DEFAULT NULL COMMENT '邮箱',
    `google_code`               varchar(32) DEFAULT '' COMMENT '谷歌验证码',
    `google_status`             tinyint(1) UNSIGNED DEFAULT '0' COMMENT '谷歌验证码是否开启，默认不开启 0-不开启； 1-开启',
    `last_login_time`           datetime COMMENT '最后登录时间',
    `dept_id`                   bigint(20) unsigned NOT NULL COMMENT '部门ID',
    `version`                   bigint(20) unsigned NOT NULL DEFAULT '1' COMMENT '乐观锁',
    `status`                    tinyint(1) UNSIGNED DEFAULT 1 COMMENT '业务状态',
    `is_deleted`                tinyint(1) UNSIGNED DEFAULT 0 COMMENT '逻辑删除 1: 已删除， 0: 未删除',
    `created_by`                bigint(20) NOT NULL COMMENT '创建者',
    `updated_by`                bigint(20) DEFAULT NULL COMMENT '更新者',
    `created_at`                datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`                datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_username`(`username`) USING BTREE,
    UNIQUE INDEX `uk_mobile`(`mobile`) USING BTREE,
    UNIQUE INDEX `uk_email`(`email`) USING BTREE
) ENGINE = InnoDB CHARSET = utf8mb4 COMMENT = '管理员表' ROW_FORMAT = Dynamic;



DROP TABLE IF EXISTS `sys_admin_role`;
CREATE TABLE IF NOT EXISTS `sys_admin_role`(
  `admin_id`                    bigint(20) UNSIGNED NOT NULL COMMENT '管理员ID',
  `role_id`                     bigint(20) UNSIGNED NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`admin_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARSET = utf8mb4 COMMENT = '管理员&角色关联表' ROW_FORMAT = Dynamic;


DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE IF NOT EXISTS `sys_role`(
    `id`                        bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`                      varchar(32) NOT NULL COMMENT '角色名称',
    `code`                      varchar(32) NOT NULL COMMENT '角色代码',
    `description`               varchar(128) DEFAULT NULL COMMENT '角色描述',
    `data_scope`                tinyint(1) NOT NULL DEFAULT 4 COMMENT '数据权限 1-全部数据权限 2-本部门及以下数据权限 3-本部门数据权限 4-仅本人数据权限 5-自定义数据权限',
    `version`                   bigint(20) unsigned NOT NULL DEFAULT '1' COMMENT '乐观锁',
    `status`                    tinyint(1) UNSIGNED DEFAULT 1 COMMENT '业务状态',
    `is_deleted`                tinyint(1) UNSIGNED DEFAULT 0 COMMENT '逻辑删除 1: 已删除， 0: 未删除',
    `created_by`                bigint(20) NOT NULL COMMENT '创建者',
    `updated_by`                bigint(20) DEFAULT NULL COMMENT '更新者',
    `created_at`                datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`                datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARSET = utf8mb4 COMMENT = '角色' ROW_FORMAT = Dynamic;


DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE IF NOT EXISTS `sys_role_menu`(
  `role_id`                     bigint(20) UNSIGNED NOT NULL COMMENT '角色ID',
  `menu_id`                     bigint(20) UNSIGNED NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB CHARSET = utf8mb4 COMMENT = '角色与菜单关联表' ROW_FORMAT = Dynamic;


DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE IF NOT EXISTS `sys_menu`(
    `id`                        bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `title`                     varchar(64) NOT NULL COMMENT '标题名称',
    `parent_id`                 bigint(20) UNSIGNED NULL DEFAULT 0 COMMENT '上级菜单ID',
    `type`                      tinyint(1) UNSIGNED NOT NULL DEFAULT 1 COMMENT '菜单类型 1-目录 2-菜单 3-按钮',
    `path`                      varchar(255) DEFAULT NULL COMMENT '路由地址',
    `name`                      varchar(255) DEFAULT NULL COMMENT '组件名称',
    `component`                 varchar(255) DEFAULT NULL COMMENT '组件路径',
    `query`                     varchar(255) DEFAULT NULL COMMENT '路由参数',
    `redirect`                  varchar(255) DEFAULT NULL COMMENT '重定向地址',
    `icon`                      varchar(255) DEFAULT NULL COMMENT '菜单图标',
    `is_frame`                  tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '是否外链 1-是 0-否',
    `is_cache`                  tinyint(1) UNSIGNED NOT NULL DEFAULT 1 COMMENT '是否缓存 1-是 0-否',
    `is_visible`                tinyint(1) UNSIGNED NOT NULL DEFAULT 1 COMMENT '是否可见 1-是 0-否',
    `permission`                varchar(255) DEFAULT NULL COMMENT '权限标识',
    `sort`                      int(7) UNSIGNED NULL DEFAULT NULL COMMENT '排序索引',
    `version`                   bigint(20) unsigned NOT NULL DEFAULT '1' COMMENT '乐观锁',
    `status`                    tinyint(1) UNSIGNED DEFAULT 1 COMMENT '业务状态',
    `is_deleted`                tinyint(1) UNSIGNED DEFAULT 0 COMMENT '逻辑删除 1: 已删除， 0: 未删除',
    `created_by`                bigint(20) NOT NULL COMMENT '创建者',
    `updated_by`                bigint(20) DEFAULT NULL COMMENT '更新者',
    `created_at`                datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`                datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARSET = utf8mb4 COMMENT = '系统菜单' ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE IF NOT EXISTS `sys_dept`(
    `id`                        bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`                      varchar(64) NOT NULL COMMENT '部门名称',
    `parent_id`                 bigint(20) unsigned NOT NULL DEFAULT 0 COMMENT '上级部门ID',
    `ancestors`                 varchar(512) NOT NULL DEFAULT '' COMMENT '祖级列表',
    `description`                varchar(512) NOT NULL DEFAULT '' COMMENT '描述',
    `version`                   bigint(20) unsigned NOT NULL DEFAULT '1' COMMENT '乐观锁',
    `status`                    tinyint(1) UNSIGNED DEFAULT 1 COMMENT '业务状态',
    `is_deleted`                tinyint(1) UNSIGNED DEFAULT 0 COMMENT '逻辑删除 1: 已删除， 0: 未删除',
    `created_by`                bigint(20) NOT NULL COMMENT '创建者',
    `updated_by`                bigint(20) DEFAULT NULL COMMENT '更新者',
    `created_at`                datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`                datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARSET = utf8mb4 COMMENT = '部门' ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE IF NOT EXISTS `sys_role_dept`(
  `role_id`                     bigint(20) UNSIGNED NOT NULL COMMENT '角色ID',
  `dept_id`                     bigint(20) UNSIGNED NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`role_id`, `dept_id`) USING BTREE
) ENGINE = InnoDB CHARSET = utf8mb4 COMMENT = '角色与部门关联表' ROW_FORMAT = Dynamic;


DROP TABLE IF EXISTS `sys_admin_log`;
CREATE TABLE IF NOT EXISTS `sys_admin_log` (
    `id`                        bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `trace_id`                  varchar(255) NOT NULL COMMENT '链路ID',
    `description`               varchar(255) NOT NULL COMMENT '日志描述',
    `module`                    varchar(64) NOT NULL COMMENT '模块',
    `request_url`               varchar(512) NOT NULL COMMENT '请求URL',
    `request_http_method`       varchar(16) DEFAULT 'GET' COMMENT '请求方式',
    `request_headers`           text DEFAULT NULL COMMENT '请求头',
    `request_body`              text DEFAULT NULL COMMENT '请求体',
    `status_code`               int(8) unsigned NOT NULL COMMENT '状态码',
    `response_headers`          text DEFAULT NULL  COMMENT '响应头',
    `response_body`             text DEFAULT NULL COMMENT '响应体',
    `consuming_time`            bigint(20) UNSIGNED DEFAULT '0' COMMENT '消耗时间(ms)',
    `ip`                        varchar(16) NOT NULL COMMENT 'IP地址',
    `ip_address`                varchar(32) NOT NULL COMMENT 'IP所属地区',
    `browser`                   varchar(32) NOT NULL COMMENT '浏览器',
    `os`                        varchar(32) NOT NULL COMMENT '操作系统',
    `version`                   bigint(20) unsigned NOT NULL DEFAULT '1' COMMENT '乐观锁',
    `status`                    tinyint(1) UNSIGNED DEFAULT 1 COMMENT '业务状态 1:成功 2:失败',
    `is_deleted`                tinyint(1) UNSIGNED DEFAULT 0 COMMENT '逻辑删除 1: 已删除， 0: 未删除',
    `created_by`                bigint(20) NOT NULL COMMENT '创建者',
    `updated_by`                bigint(20) DEFAULT NULL COMMENT '更新者',
    `created_at`                datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`                datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='系统日志表';


DROP TABLE IF EXISTS `gen_config`;
CREATE TABLE IF NOT EXISTS `gen_config` (
    `table_name`                varchar(64)  NOT NULL              COMMENT '表名称',
    `module_name`               varchar(60)  NOT NULL              COMMENT '模块名称',
    `package_name`              varchar(60)  NOT NULL              COMMENT '包名称',
    `business_name`             varchar(50)  NOT NULL              COMMENT '业务名称',
    `author`                    varchar(100) NOT NULL              COMMENT '作者',
    `table_prefix`              varchar(20)  DEFAULT NULL          COMMENT '表前缀',
    `is_override`               bit(1)       NOT NULL DEFAULT b'0' COMMENT '是否覆盖',
    `created_at`                datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`                datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`table_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='生成配置表';

DROP TABLE IF EXISTS `gen_field_config`;
CREATE TABLE IF NOT EXISTS `gen_field_config` (
    `table_name`                varchar(64)  NOT NULL              COMMENT '表名称',
    `column_name`               varchar(64)  NOT NULL              COMMENT '列名称',
    `column_type`               varchar(25)  NOT NULL              COMMENT '列类型',
    `column_size`               bigint(20)   DEFAULT NULL          COMMENT '列大小',
    `field_name`                varchar(64)  NOT NULL              COMMENT '字段名称',
    `field_type`                varchar(25)  NOT NULL              COMMENT '字段类型',
    `field_sort`                int          NOT NULL DEFAULT 999  COMMENT '字段排序',
    `comment`                   varchar(512) DEFAULT NULL          COMMENT '注释',
    `is_required`               bit(1)       NOT NULL DEFAULT b'1' COMMENT '是否必填',
    `show_in_list`              bit(1)       NOT NULL DEFAULT b'1' COMMENT '是否在列表中显示',
    `show_in_form`              bit(1)       NOT NULL DEFAULT b'1' COMMENT '是否在表单中显示',
    `show_in_query`             bit(1)       NOT NULL DEFAULT b'1' COMMENT '是否在查询中显示',
    `form_type`                 tinyint(1)   UNSIGNED DEFAULT NULL COMMENT '表单类型',
    `query_type`                tinyint(1)   UNSIGNED DEFAULT NULL COMMENT '查询方式',
    `created_at`                datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`                datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_table_name`(`table_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字段配置表';

DROP TABLE IF EXISTS `sys_message`;
CREATE TABLE IF NOT EXISTS `sys_message` (
    `id`          bigint(20)     NOT NULL AUTO_INCREMENT                COMMENT 'ID',
    `title`       varchar(100)   NOT NULL                               COMMENT '标题',
    `content`     text           DEFAULT NULL                           COMMENT '内容',
    `type`        tinyint(1)     UNSIGNED NOT NULL DEFAULT 1           COMMENT '类型（1：系统消息）',
    `version`     bigint(20)     UNSIGNED NOT NULL DEFAULT 1           COMMENT '乐观锁',
    `status`      tinyint(1)     UNSIGNED DEFAULT 1                    COMMENT '业务状态 1:成功 2:失败',
    `is_deleted`  tinyint(1)     UNSIGNED DEFAULT 0                    COMMENT '逻辑删除 1: 已删除， 0: 未删除',
    `created_by`  bigint(20)     NOT NULL                              COMMENT '创建者',
    `updated_by`  bigint(20)     DEFAULT NULL                          COMMENT '更新者',
    `created_at`  datetime       DEFAULT CURRENT_TIMESTAMP             COMMENT '创建时间',
    `updated_at`  datetime       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX `idx_type` (`type`),
    INDEX `idx_created_at` (`created_at`),
    INDEX `idx_status` (`status`),
    INDEX `idx_is_deleted` (`is_deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='消息表';

DROP TABLE IF EXISTS `sys_message_admin`;
CREATE TABLE IF NOT EXISTS `sys_message_admin` (
    `id`          bigint(20)     NOT NULL AUTO_INCREMENT                COMMENT '主键ID',
    `message_id`  bigint(20)     NOT NULL                              COMMENT '消息ID',
    `admin_id`    bigint(20)     NOT NULL                              COMMENT '管理员ID',
    `is_read`     tinyint(1)     NOT NULL DEFAULT 0                    COMMENT '是否已读（0：未读，1：已读）',
    `read_time`   datetime       DEFAULT NULL                          COMMENT '读取时间',
    `version`     bigint(20)     UNSIGNED NOT NULL DEFAULT 1           COMMENT '乐观锁',
    `status`      tinyint(1)     UNSIGNED DEFAULT 1                    COMMENT '业务状态 1:成功 2:失败',
    `is_deleted`  tinyint(1)     UNSIGNED DEFAULT 0                    COMMENT '逻辑删除 1: 已删除， 0: 未删除',
    `created_by`  bigint(20)     NOT NULL                              COMMENT '创建者',
    `updated_by`  bigint(20)     DEFAULT NULL                          COMMENT '更新者',
    `created_at`  datetime       DEFAULT CURRENT_TIMESTAMP             COMMENT '创建时间',
    `updated_at`  datetime       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_message_admin` (`message_id`, `admin_id`, `is_deleted`),
    INDEX `idx_admin_id` (`admin_id`, `is_deleted`),
    INDEX `idx_is_read` (`is_read`),
    INDEX `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='管理员消息关联表';