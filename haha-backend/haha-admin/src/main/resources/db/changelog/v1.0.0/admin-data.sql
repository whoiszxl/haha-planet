-- liquibase formatted sql

-- changeset whoiszxl:1
-- comment 初始化admin模块的表数据

-- 初始化默认用户 banana/123456
INSERT INTO `sys_admin` (`id`, `username`, `nickname`, `password`, `avatar`, `real_name`, `gender`, `mobile`, `email`, `google_code`, `google_status`, `last_login_time`, `dept_id`, `version`, `status`, `is_deleted`, `created_by`, `updated_by`, `created_at`, `updated_at`) VALUES (1, 'banana', '香蕉', '{bcrypt}$2a$10$g9P9DxemFdIylwhkrp.m3.clkjgjiGDRcVeTSb.ZEwDk4aUWc3b8e', 'https://shopzz.oss-cn-guangzhou.aliyuncs.com/other/a1.jpg', '香蕉', 0, '12311231345', 'whoisbanana@gmail.com', '', 0, NULL, 5, 1, 1, 0, 1, 1, NULL, '2020-12-28 00:00:00');

-- 初始化用户角色
INSERT INTO `sys_role` (`id`, `name`, `code`, `description`, `data_scope`, `version`, `status`, `is_deleted`, `created_by`, `updated_by`, `created_at`, `updated_at`) VALUES (1, '超级管理员', 'admin', '超级管理员', 1, 1, 1, 0, 1, 1, '2020-12-28 00:00:00', '2020-12-28 00:00:00');
INSERT INTO `sys_role` (`id`, `name`, `code`, `description`, `data_scope`, `version`, `status`, `is_deleted`, `created_by`, `updated_by`, `created_at`, `updated_at`) VALUES (2, '开发人员', 'dev', '做开发的', 5, 1, 1, 0, 1, 1, '2020-12-28 00:00:00', '2020-12-28 00:00:00');
INSERT INTO `sys_role` (`id`, `name`, `code`, `description`, `data_scope`, `version`, `status`, `is_deleted`, `created_by`, `updated_by`, `created_at`, `updated_at`) VALUES (3, '测试人员', 'test', '做测试的', 5, 1, 1, 0, 1, 1, '2020-12-28 00:00:00', '2020-12-28 00:00:00');
INSERT INTO `sys_role` (`id`, `name`, `code`, `description`, `data_scope`, `version`, `status`, `is_deleted`, `created_by`, `updated_by`, `created_at`, `updated_at`) VALUES (4, '运维人员', 'devops', '做运维的', 5, 1, 1, 0, 1, 1, '2020-12-28 00:00:00', '2020-12-28 00:00:00');
INSERT INTO `sys_role` (`id`, `name`, `code`, `description`, `data_scope`, `version`, `status`, `is_deleted`, `created_by`, `updated_by`, `created_at`, `updated_at`) VALUES (5, '产品人员', 'pm', '做产品的', 5, 1, 1, 0, 1, 1, '2020-12-28 00:00:00', '2020-12-28 00:00:00');

-- 初始化部门
INSERT INTO `sys_dept` (`id`, `name`, `parent_id`, `ancestors`, `description`, `version`, `status`, `is_deleted`, `created_by`, `updated_by`, `created_at`, `updated_at`) VALUES (1, '霍格沃兹魔法无限公司', 0, '0', '总公司', 1, 1, 0, 1, 1, '2020-12-28 00:00:00', '2020-12-28 00:00:00');
INSERT INTO `sys_dept` (`id`, `name`, `parent_id`, `ancestors`, `description`, `version`, `status`, `is_deleted`, `created_by`, `updated_by`, `created_at`, `updated_at`) VALUES (2, '霍格沃兹总部', 1, '0,1', '总部', 1, 1, 0, 1, 1, '2020-12-28 00:00:00', '2020-12-28 00:00:00');
INSERT INTO `sys_dept` (`id`, `name`, `parent_id`, `ancestors`, `description`, `version`, `status`, `is_deleted`, `created_by`, `updated_by`, `created_at`, `updated_at`) VALUES (3, '研发部', 2, '0,1,2', '魔法研发部门', 1, 1, 0, 1, 1, '2020-12-28 00:00:00', '2020-12-28 00:00:00');
INSERT INTO `sys_dept` (`id`, `name`, `parent_id`, `ancestors`, `description`, `version`, `status`, `is_deleted`, `created_by`, `updated_by`, `created_at`, `updated_at`) VALUES (4, '测试部', 2, '0,1,2', '魔法测试部门', 1, 1, 0, 1, 1, '2020-12-28 00:00:00', '2020-12-28 00:00:00');
INSERT INTO `sys_dept` (`id`, `name`, `parent_id`, `ancestors`, `description`, `version`, `status`, `is_deleted`, `created_by`, `updated_by`, `created_at`, `updated_at`) VALUES (5, '研发一部', 3, '0,1,2,3', '魔法研发一部', 1, 1, 0, 1, 1, '2020-12-28 00:00:00', '2020-12-28 00:00:00');
INSERT INTO `sys_dept` (`id`, `name`, `parent_id`, `ancestors`, `description`, `version`, `status`, `is_deleted`, `created_by`, `updated_by`, `created_at`, `updated_at`) VALUES (6, '研发二部', 3, '0,1,2,3', '魔法研发二部', 1, 1, 0, 1, 1, '2020-12-28 00:00:00', '2020-12-28 00:00:00');

-- 初始化菜单
insert into `sys_menu` (id, title, parent_id, type, path, name, component, query, redirect, icon, is_frame, is_cache, is_visible, permission, sort, version, status, is_deleted, created_by, updated_by, created_at, updated_at)
values  (1, '系统管理', 0, 1, '/system', 'System', 'Layout', null, null, 'settings', 0, 1, 0, 'system', 1, 1, 1, 0, 1, 1, '2020-12-28 00:00:00', '2024-05-23 16:20:45'),
        (2, '管理员管理', 1, 2, '/system/user', 'SystemUser', 'system/user/index', null, null, 'user', 0, 1, 0, 'system:admin:list', 1, 1, 1, 0, 1, 1, '2020-12-28 00:00:00', '2024-05-23 16:20:45'),
        (3, '管理员新增', 2, 3, '', null, null, null, null, null, 0, 1, 0, 'system:admin:add', 1, 1, 1, 0, 1, 1, '2020-12-28 00:00:00', '2024-05-23 16:20:45'),
        (4, '管理员修改', 2, 3, null, null, null, null, null, null, 0, 1, 0, 'system:admin:update', 1, 1, 1, 0, 1, 1, '2020-12-28 00:00:00', '2024-05-23 16:20:45'),
        (5, '管理员删除', 2, 3, null, null, null, null, null, null, 0, 1, 0, 'system:admin:delete', 1, 1, 1, 0, 1, 1, '2020-12-28 00:00:00', '2024-05-23 16:20:45'),
        (6, '管理员导出', 2, 3, null, null, null, null, null, null, 0, 1, 0, 'system:admin:export', 1, 1, 1, 0, 1, 1, '2020-12-28 00:00:00', '2024-05-23 16:20:45'),
        (7, '管理员密码重置', 2, 3, null, null, null, null, null, null, 0, 1, 0, 'system:admin:reset-password', 1, 1, 1, 0, 1, 1, '2020-12-28 00:00:00', '2024-05-23 16:20:45'),
        (8, '管理员角色分配', 2, 3, null, null, null, null, null, null, 0, 1, 0, 'system:admin:update-role', 1, 1, 1, 0, 1, 1, '2020-12-28 00:00:00', '2024-05-23 16:20:45'),
        (9, '角色管理', 1, 2, '/system/role', 'SystemRole', 'system/role/index', null, null, 'user-group', 0, 1, 0, 'system:role:list', 1, 1, 1, 0, 1, 1, '2020-12-28 00:00:00', '2024-05-23 16:20:46'),
        (10, '角色新增', 9, 3, null, null, null, null, null, null, 0, 1, 0, 'system:role:add', 1, 1, 1, 0, 1, 1, '2020-12-28 00:00:00', '2024-05-23 16:22:37'),
        (11, '角色修改', 9, 3, null, null, null, null, null, null, 0, 1, 0, 'system:role:update', 1, 1, 1, 0, 1, 1, '2020-12-28 00:00:00', '2024-05-23 16:22:34'),
        (12, '角色删除', 9, 3, null, null, null, null, null, null, 0, 1, 0, 'system:role:delete', 1, 1, 1, 0, 1, 1, '2020-12-28 00:00:00', '2024-05-23 16:22:34'),
        (13, '角色导出', 9, 3, null, null, null, null, null, null, 0, 1, 0, 'system:role:export', 1, 1, 1, 0, 1, 1, '2020-12-28 00:00:00', '2024-05-23 16:22:31'),
        (14, '菜单管理', 1, 2, '/system/menu', 'SystemMenu', 'system/menu/index', null, null, 'menu', 0, 1, 0, 'system:menu:list', 1, 1, 1, 0, 1, 1, '2020-12-28 00:00:00', '2024-05-23 16:22:19'),
        (15, '菜单新增', 14, 3, null, null, null, null, null, null, 0, 1, 0, 'system:menu:add', 1, 1, 1, 0, 1, 1, '2020-12-28 00:00:00', '2024-05-23 16:22:25'),
        (16, '菜单修改', 14, 3, null, null, null, null, null, null, 0, 1, 0, 'system:menu:update', 1, 1, 1, 0, 1, 1, '2020-12-28 00:00:00', '2020-12-28 00:00:00'),
        (17, '菜单删除', 14, 3, null, null, null, null, null, null, 0, 1, 0, 'system:menu:delete', 1, 1, 1, 0, 1, 1, '2020-12-28 00:00:00', '2020-12-28 00:00:00'),
        (18, '菜单导出', 14, 3, null, null, null, null, null, null, 0, 1, 0, 'system:menu:export', 1, 1, 1, 0, 1, 1, '2020-12-28 00:00:00', '2020-12-28 00:00:00'),
        (19, '部门管理', 1, 2, '/system/dept', 'SystemDept', 'system/dept/index', null, null, 'mind-mapping', 0, 1, 0, 'system:dept:list', 1, 1, 1, 0, 1, 1, '2024-05-28 15:11:17', '2024-05-28 15:20:58'),
        (20, '部门新增', 19, 3, '', null, null, null, null, null, 0, 1, 0, 'system:dept:add', 1, 1, 1, 0, 1, 1, '2024-05-28 15:12:16', '2024-05-28 15:20:58'),
        (21, '部门修改', 19, 3, '', null, null, null, null, null, 0, 1, 0, 'system:dept:update', 1, 1, 1, 0, 1, 1, '2024-05-28 15:12:16', '2024-05-28 15:20:58'),
        (22, '部门删除', 19, 3, '', null, null, null, null, null, 0, 1, 0, 'system:dept:delete', 1, 1, 1, 0, 1, 1, '2024-05-28 15:12:16', '2024-05-28 15:20:58'),
        (23, '部门导出', 19, 3, '', null, null, null, null, null, 0, 1, 0, 'system:dept:export', 1, 1, 1, 0, 1, 1, '2024-05-28 15:12:16', '2024-05-28 15:20:58'),
        (24, '在线用户', 1, 2, '/monitor/online', 'MonitorOnline', 'monitor/online/index', null, null, 'user', 0, 1, 0, 'system:online:list', 1, 1, 1, 0, 1, 1, '2024-05-28 15:12:16', '2024-05-28 15:20:58'),
        (25, '在线用户查看', 24, 3, null, null, null, null, null, null, 0, 1, 0, 'system:online:list', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:20:58'),
        (26, '在线用户强退', 24, 3, null, null, null, null, null, null, 0, 1, 0, 'system:online:kick-out', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:20:58'),
        (27, '系统日志', 1, 2, '/monitor/log', 'MonitorLog', 'monitor/log/index', null, null, 'history', 0, 1, 0, 'system:log:list', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:20:58'),
        (28, '日志查看', 27, 3, '', '', '', null, null, '', 0, 1, 0, 'system:log:list', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 22:16:32'),
        (29, '日志导出', 27, 3, '', '', '', null, null, '', 0, 1, 0, 'system:log:export', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 22:16:48'),
        (30, '代码生成器', 1, 2, '/tool/generator', 'ToolGenerator', 'tool/generator/index', null, null, 'code', 0, 1, 0, 'tool:generator:list', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 22:16:48'),
        (31, '查看', 30, 3, '', '', '', null, null, '', 0, 1, 0, 'tool:generator:list', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 22:16:48'),
        (32, '配置', 30, 3, '', '', '', null, null, '', 0, 1, 0, 'tool:generator:config', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 22:16:48'),
        (33, '预览', 30, 3, '', '', '', null, null, '', 0, 1, 0, 'tool:generator:preview', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 22:16:48'),
        (34, '生成代码', 30, 3, '', '', '', null, null, '', 0, 1, 0, 'tool:generator:generator', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 22:16:48'),
        (41, '星球管理', 0, 1, '/planet', 'Planet', 'Layout', null, '', 'code', 0, 1, 0, 'planet', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2025-03-06 09:05:13'),
        (42, '星球管理', 41, 2, '/planet/planet', 'PlanetPlanet', 'planet/planet/index', null, '', 'code', 0, 1, 0, 'planet:planet:list', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2025-03-06 09:05:12'),
        (43, '星球新增', 42, 3, null, null, null, null, null, null, 0, 0, 1, 'planet:planet:add', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (44, '星球修改', 42, 3, null, null, null, null, null, null, 0, 0, 1, 'planet:planet:update', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (45, '星球删除', 42, 3, null, null, null, null, null, null, 0, 0, 1, 'planet:planet:delete', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (46, '星球导出', 42, 3, null, null, null, null, null, null, 0, 0, 1, 'planet:planet:export', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (47, '星球分类', 41, 2, '/planet/planetCategory', 'PlanetPlanetCategory', 'planet/planetCategory/index', null, '', 'code', 0, 1, 0, 'planet:planetCategory:list', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2025-03-06 09:05:12'),
        (48, '星球分类新增', 47, 3, null, null, null, null, null, null, 0, 0, 1, 'planet:planetCategory:add', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (49, '星球分类修改', 47, 3, null, null, null, null, null, null, 0, 0, 1, 'planet:planetCategory:update', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (50, '星球分类删除', 47, 3, null, null, null, null, null, null, 0, 0, 1, 'planet:planetCategory:delete', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (51, '星球分类导出', 47, 3, null, null, null, null, null, null, 0, 0, 1, 'planet:planetCategory:export', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2025-03-07 06:29:11'),
        (52, '星球评论', 41, 2, '/planet/planetComment', 'PlanetPlanetComment', 'planet/planetComment/index', null, '', 'code', 0, 1, 0, 'planet:planetComment:list', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2025-03-06 09:05:12'),
        (53, '星球评论新增', 52, 3, null, null, null, null, null, null, 0, 0, 1, 'planet:planetComment:add', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (54, '星球评论修改', 52, 3, null, null, null, null, null, null, 0, 0, 1, 'planet:planetComment:update', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (55, '星球评论删除', 52, 3, null, null, null, null, null, null, 0, 0, 1, 'planet:planetComment:delete', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (56, '星球评论导出', 52, 3, null, null, null, null, null, null, 0, 0, 1, 'planet:planetComment:export', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (57, '星球帖子', 41, 2, '/planet/planetPost', 'PlanetPlanetPost', 'planet/planetPost/index', null, '', 'edit', 0, 1, 0, 'planet:planetPost:list', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2025-03-06 09:05:12'),
        (58, '星球帖子新增', 57, 3, null, null, null, null, null, null, 0, 0, 1, 'planet:planetPost:add', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (59, '星球帖子修改', 57, 3, null, null, null, null, null, null, 0, 0, 1, 'planet:planetPost:update', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (60, '星球帖子删除', 57, 3, null, null, null, null, null, null, 0, 0, 1, 'planet:planetPost:delete', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (61, '星球帖子导出', 57, 3, null, null, null, null, null, null, 0, 0, 1, 'planet:planetPost:export', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (62, '星球标签', 41, 2, '/planet/planetTag', 'PlanetPlanetTag', 'planet/planetTag/index', null, '', 'tag', 0, 1, 0, 'planet:planetTag:list', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2025-03-06 09:05:12'),
        (63, '星球标签新增', 62, 3, null, null, null, null, null, null, 0, 0, 1, 'planet:planetTag:add', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (64, '星球标签修改', 62, 3, null, null, null, null, null, null, 0, 0, 1, 'planet:planetTag:update', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (65, '星球标签删除', 62, 3, null, null, null, null, null, null, 0, 0, 1, 'planet:planetTag:delete', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (66, '星球标签导出', 62, 3, null, null, null, null, null, null, 0, 0, 1, 'planet:planetTag:export', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (67, '星球设置', 41, 2, '/planet/planetSetting', 'PlanetPlanetSetting', 'planet/planetSetting/index', null, '', 'setting', 0, 1, 0, 'planet:planetSetting:list', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2025-03-06 09:05:12'),
        (68, '星球设置新增', 67, 3, null, null, null, null, null, null, 0, 0, 1, 'planet:planetSetting:add', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (69, '星球设置修改', 67, 3, null, null, null, null, null, null, 0, 0, 1, 'planet:planetSetting:update', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (70, '星球设置删除', 67, 3, null, null, null, null, null, null, 0, 0, 1, 'planet:planetSetting:delete', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (71, '星球设置导出', 67, 3, null, null, null, null, null, null, 0, 0, 1, 'planet:planetSetting:export', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (72, '星球帖子收藏', 41, 2, '/planet/planetCollect', 'PlanetPlanetCollect', 'planet/planetCollect/index', null, '', 'collect', 0, 1, 0, 'planet:planetCollect:list', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2025-03-06 09:05:12'),
        (73, '星球帖子收藏新增', 72, 3, null, null, null, null, null, null, 0, 0, 1, 'planet:planetCollect:add', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (74, '星球帖子收藏修改', 72, 3, null, null, null, null, null, null, 0, 0, 1, 'planet:planetCollect:update', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (75, '星球帖子收藏删除', 72, 3, null, null, null, null, null, null, 0, 0, 1, 'planet:planetCollect:delete', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (76, '星球帖子收藏导出', 72, 3, null, null, null, null, null, null, 0, 0, 1, 'planet:planetCollect:export', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (77, '星球邀请', 41, 2, '/planet/planetInvite', 'PlanetPlanetInvite', 'planet/planetInvite/index', null, '', 'invite', 0, 1, 0, 'planet:planetInvite:list', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2025-03-06 09:05:12'),
        (78, '星球邀请新增', 77, 3, null, null, null, null, null, null, 0, 0, 1, 'planet:planetInvite:add', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (79, '星球邀请修改', 77, 3, null, null, null, null, null, null, 0, 0, 1, 'planet:planetInvite:update', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (80, '星球邀请删除', 77, 3, null, null, null, null, null, null, 0, 0, 1, 'planet:planetInvite:delete', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (81, '星球邀请导出', 77, 3, null, null, null, null, null, null, 0, 0, 1, 'planet:planetInvite:export', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (82, '星球申请', 41, 2, '/planet/planetApply', 'PlanetPlanetApply', 'planet/planetApply/index', null, '', 'apply', 0, 1, 0, 'planet:planetApply:list', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2025-03-06 09:05:12'),
        (83, '星球申请新增', 82, 3, null, null, null, null, null, null, 0, 0, 1, 'planet:planetApply:add', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (84, '星球申请修改', 82, 3, null, null, null, null, null, null, 0, 0, 1, 'planet:planetApply:update', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (85, '星球申请删除', 82, 3, null, null, null, null, null, null, 0, 0, 1, 'planet:planetApply:delete', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (86, '星球申请导出', 82, 3, null, null, null, null, null, null, 0, 0, 1, 'planet:planetApply:export', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        

        (87, '用户管理', 0, 1, '/user', 'User', 'Layout', null, '', 'user', 0, 1, 0, 'user', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (88, '用户账号管理', 87, 2, '/user/userInfo', 'UserUserInfo', 'user/userInfo/index', null, '', 'user', 0, 1, 0, 'user:userInfo:list', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (89, '账号新增', 88, 3, null, null, null, null, null, null, 0, 0, 1, 'user:userInfo:add', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (90, '账号修改', 88, 3, null, null, null, null, null, null, 0, 0, 1, 'user:userInfo:update', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (91, '账号删除', 88, 3, null, null, null, null, null, null, 0, 0, 1, 'user:userInfo:delete', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (92, '账号导出', 88, 3, null, null, null, null, null, null, 0, 0, 1, 'user:userInfo:export', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        
        (93, '客户端管理', 87, 2, '/user/userClient', 'UserUserClient', 'user/userClient/index', null, '', 'user', 0, 1, 0, 'user:userClient:list', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (94, '客户端新增', 93, 3, null, null, null, null, null, null, 0, 0, 1, 'user:userClient:add', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (95, '客户端修改', 93, 3, null, null, null, null, null, null, 0, 0, 1, 'user:userClient:update', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (96, '客户端删除', 93, 3, null, null, null, null, null, null, 0, 0, 1, 'user:userClient:delete', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (97, '客户端导出', 93, 3, null, null, null, null, null, null, 0, 0, 1, 'user:userClient:export', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),


        (98, '用户等级管理', 87, 2, '/user/userLevel', 'UserUserLevel', 'user/userLevel/index', null, '', 'user', 0, 1, 0, 'user:userLevel:list', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (99, '用户等级新增', 98, 3, null, null, null, null, null, null, 0, 0, 1, 'user:userLevel:add', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (100, '用户等级修改', 98, 3, null, null, null, null, null, null, 0, 0, 1, 'user:userLevel:update', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (101, '用户等级删除', 98, 3, null, null, null, null, null, null, 0, 0, 1, 'user:userLevel:delete', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (102, '用户等级导出', 98, 3, null, null, null, null, null, null, 0, 0, 1, 'user:userLevel:export', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (103, '用户设置管理', 87, 2, '/user/userSettings', 'UserUserSettings', 'user/userSettings/index', null, '', 'user', 0, 1, 0, 'user:userSettings:list', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (104, '用户设置新增', 103, 3, null, null, null, null, null, null, 0, 0, 1, 'user:userSettings:add', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (105, '用户设置修改', 103, 3, null, null, null, null, null, null, 0, 0, 1, 'user:userSettings:update', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (106, '用户设置删除', 103, 3, null, null, null, null, null, null, 0, 0, 1, 'user:userSettings:delete', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (107, '用户设置导出', 103, 3, null, null, null, null, null, null, 0, 0, 1, 'user:userSettings:export', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (108, '用户令牌管理', 87, 2, '/user/userToken', 'UserUserToken', 'user/userToken/index', null, '', 'user', 0, 1, 0, 'user:userToken:list', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (109, '用户令牌新增', 108, 3, null, null, null, null, null, null, 0, 0, 1, 'user:userToken:add', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (110, '用户令牌修改', 108, 3, null, null, null, null, null, null, 0, 0, 1, 'user:userToken:update', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (111, '用户令牌删除', 108, 3, null, null, null, null, null, null, 0, 0, 1, 'user:userToken:delete', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (112, '用户令牌导出', 108, 3, null, null, null, null, null, null, 0, 0, 1, 'user:userToken:export', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20')
        ;


-- 初始化管理员和角色关联关系
INSERT INTO `sys_admin_role` (`admin_id`, `role_id`) VALUES (1, 1);

-- 初始化角色和菜单关联关系
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (2, 1);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (2, 2);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (2, 3);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (2, 4);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (2, 5);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (2, 6);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (2, 7);
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (2, 8);

-- 初始化角色和部门关联关系
INSERT INTO `sys_role_dept` (`role_id`, `dept_id`) VALUES (2, 5);

