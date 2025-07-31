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
        (35, '会员管理', 0, 1, '/member', 'Member', 'Layout', null, '', 'user', 0, 1, 0, 'member', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (36, '会员账号管理', 35, 2, '/member/member', 'MemberMember', 'member/member/index', null, '', 'user', 0, 1, 0, 'member:member:list', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (37, '账号新增', 36, 3, null, null, null, null, null, null, 0, 0, 1, 'member:member:add', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (38, '账号修改', 36, 3, null, null, null, null, null, null, 0, 0, 1, 'member:member:update', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (39, '账号删除', 36, 3, null, null, null, null, null, null, 0, 0, 1, 'member:member:delete', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (40, '账号导出', 36, 3, null, null, null, null, null, null, 0, 0, 1, 'member:member:export', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (41, '文章管理', 0, 1, '/article', 'Article', 'Layout', null, '', 'code', 0, 1, 0, 'article', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2025-03-06 09:05:13'),
        (42, '文章内容管理', 41, 2, '/article/article', 'ArticleArticle', 'article/article/index', null, '', 'code', 0, 1, 0, 'article:article:list', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2025-03-06 09:05:12'),
        (43, '文章新增', 42, 3, null, null, null, null, null, null, 0, 0, 1, 'article:article:add', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (44, '文章修改', 42, 3, null, null, null, null, null, null, 0, 0, 1, 'article:article:update', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (45, '文章删除', 42, 3, null, null, null, null, null, null, 0, 0, 1, 'article:article:delete', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (46, '文章导出', 42, 3, null, null, null, null, null, null, 0, 0, 1, 'article:article:export', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (47, '文章分类', 41, 2, '/article/category', 'ArticleCategory', 'article/category/index', null, '', 'code', 0, 1, 0, 'article:category:list', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2025-03-06 09:05:12'),
        (48, '文章分类新增', 47, 3, null, null, null, null, null, null, 0, 0, 1, 'article:category:add', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (49, '文章分类修改', 47, 3, null, null, null, null, null, null, 0, 0, 1, 'article:category:update', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (50, '文章分类删除', 47, 3, null, null, null, null, null, null, 0, 0, 1, 'article:category:delete', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (51, '文章分类导出', 47, 3, null, null, null, null, null, null, 0, 0, 1, 'article:category:export', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2025-03-07 06:29:11'),
        (52, '文章评论', 41, 2, '/article/comment', 'ArticleComment', 'article/comment/index', null, '', 'code', 0, 1, 0, 'article:comment:list', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2025-03-06 09:05:12'),
        (53, '文章评论新增', 52, 3, null, null, null, null, null, null, 0, 0, 1, 'article:comment:add', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (54, '文章评论修改', 52, 3, null, null, null, null, null, null, 0, 0, 1, 'article:comment:update', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (55, '文章评论删除', 52, 3, null, null, null, null, null, null, 0, 0, 1, 'article:comment:delete', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (56, '文章评论导出', 52, 3, null, null, null, null, null, null, 0, 0, 1, 'article:comment:export', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (57, '简历管理', 0, 1, '/resume', 'Resume', 'Layout', null, '', 'code', 0, 1, 0, 'resume', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2025-03-06 09:05:13'),
        (58, '简历内容管理', 57, 2, '/resume/resumeInfo', 'ResumeResumeInfo', 'resume/resumeInfo/index', null, '', 'code', 0, 1, 0, 'resume:resume:list', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2025-03-06 09:05:12'),
        (59, '简历新增', 58, 3, null, null, null, null, null, null, 0, 0, 1, 'resume:resumeInfo:add', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (60, '简历修改', 58, 3, null, null, null, null, null, null, 0, 0, 1, 'resume:resumeInfo:update', 1, 1, 1, 0, 1, 1, '2024-05-28 15-17-20', '2024-05-28 15:17:20'),
        (61, '简历删除', 58, 3, null, null, null, null, null, null, 0, 0, 1, 'resume:resumeInfo:delete', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (62, '简历导出', 58, 3, null, null, null, null, null, null, 0, 0, 1, 'resume:resumeInfo:export', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (63, '模板管理', 57, 2, '/resume/template', 'ResumeTemplate', 'resume/template/index', null, '', 'code', 0, 1, 0, 'resume:template:list', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2025-03-06 09:05:12'),
        (64, '模板新增', 63, 3, null, null, null, null, null, null, 0, 0, 1, 'resume:template:add', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (65, '模板修改', 63, 3, null, null, null, null, null, null, 0, 0, 1, 'resume:template:update', 1, 1, 1, 0, 1, 1, '2024-05-28 15-17-20', '2024-05-28 15:17:20'),
        (66, '模板删除', 63, 3, null, null, null, null, null, null, 0, 0, 1, 'resume:template:delete', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (67, '模板导出', 63, 3, null, null, null, null, null, null, 0, 0, 1, 'resume:template:export', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2222-05-28 15:17:20'),
        (68, '问答管理', 0, 1, '/answering', 'Answering', 'Layout', null, '', 'code', 0, 1, 0, 'answering', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2025-03-06 09:05:13'),
        (69, '考试管理', 68, 2, '/answering/exam', 'AnsweringExam', 'answering/exam/index', null, '', 'code', 0, 1, 0, 'answering:exam:list', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2025-03-06 09:05:12'),
        (70, '考试新增', 69, 3, null, null, null, null, null, null, 0, 0, 1, 'answering:exam:add', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (71, '考试修改', 69, 3, null, null, null, null, null, null, 0, 0, 1, 'answering:exam:update', 1, 1, 1, 0, 1, 1, '2024-05-28 15-17-20', '2024-05-28 15:17:20'),
        (72, '考试删除', 69, 3, null, null, null, null, null, null, 0, 0, 1, 'answering:exam:delete', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (73, '考试导出', 69, 3, null, null, null, null, null, null, 0, 0, 1, 'answering:exam:export', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (74, '题型管理', 68, 2, '/answering/questionType', 'AnsweringQuestionType', 'answering/questionType/index', null, '', 'code', 0, 1, 0, 'answering:questionType:list', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2025-03-06 09:05:12'),
        (75, '题型新增', 74, 3, null, null, null, null, null, null, 0, 0, 1, 'answering:questionType:add', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (76, '题型修改', 74, 3, null, null, null, null, null, null, 0, 0, 1, 'answering:questionType:update', 1, 1, 1, 0, 1, 1, '2024-05-28 15-17-20', '2024-05-28 15:17:20'),
        (77, '题型删除', 74, 3, null, null, null, null, null, null, 0, 0, 1, 'answering:questionType:delete', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (78, '题型导出', 74, 3, null, null, null, null, null, null, 0, 0, 1, 'answering:questionType:export', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (79, '题目分类管理', 68, 2, '/answering/questionCategory', 'AnsweringQuestionCategory', 'answering/questionCategory/index', null, '', 'code', 0, 1, 0, 'answering:questionCategory:list', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2025-03-06 09:05:12'),
        (80, '题目分类新增', 79, 3, null, null, null, null, null, null, 0, 0, 1, 'answering:questionCategory:add', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (81, '题目分类修改', 79, 3, null, null, null, null, null, null, 0, 0, 1, 'answering:questionCategory:update', 1, 1, 1, 0, 1, 1, '2024-05-28 15-17-20', '2024-05-28 15:17:20'),
        (82, '题目分类删除', 79, 3, null, null, null, null, null, null, 0, 0, 1, 'answering:questionCategory:delete', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (83, '题目分类导出', 79, 3, null, null, null, null, null, null, 0, 0, 1, 'answering:questionCategory:export', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (84, '题目管理', 68, 2, '/answering/question', 'AnsweringQuestion', 'answering/question/index', null, '', 'code', 0, 1, 0, 'answering:question:list', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2025-03-06 09:05:12'),
        (85, '题目新增', 79, 3, null, null, null, null, null, null, 0, 0, 1, 'answering:question:add', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (86, '题目修改', 79, 3, null, null, null, null, null, null, 0, 0, 1, 'answering:question:update', 1, 1, 1, 0, 1, 1, '2024-05-28 15-17-20', '2024-05-28 15:17:20'),
        (87, '题目删除', 79, 3, null, null, null, null, null, null, 0, 0, 1, 'answering:question:delete', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20'),
        (88, '题目导出', 79, 3, null, null, null, null, null, null, 0, 0, 1, 'answering:question:export', 1, 1, 1, 0, 1, 1, '2024-05-28 15:17:20', '2024-05-28 15:17:20');



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

