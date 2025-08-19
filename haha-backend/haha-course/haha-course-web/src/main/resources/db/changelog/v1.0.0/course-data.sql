-- liquibase formatted sql

-- changeset whoiszxl:1
-- comment 课程模块: 初始化模块表数据

-- 课程分类数据（匹配新的表结构）
INSERT INTO `cou_category` (`id`, `category_name`, `icon_url`, `parent_id`, `level`, `sort`, `description`, `course_count`, `is_featured`, `version`, `status`, `is_deleted`, `created_by`, `updated_by`, `created_at`, `updated_at`) VALUES
(1, '前端开发', 'https://haha-planet.oss-cn-shanghai.aliyuncs.com/icons/frontend.png', 0, 1, 100, '前端开发相关课程，包括 HTML、CSS、JavaScript 等', 0, 1, 1, 1, 0, 1, 1, NOW(), NOW()),
(2, 'HTML/CSS', 'https://haha-planet.oss-cn-shanghai.aliyuncs.com/icons/html-css.png', 1, 2, 90, 'HTML 和 CSS 基础知识与实战', 0, 1, 1, 1, 0, 1, 1, NOW(), NOW()),
(3, 'JavaScript', 'https://haha-planet.oss-cn-shanghai.aliyuncs.com/icons/javascript.png', 1, 2, 80, 'JavaScript 编程语言基础与进阶', 0, 1, 1, 1, 0, 1, 1, NOW(), NOW()),
(4, '前端框架', 'https://haha-planet.oss-cn-shanghai.aliyuncs.com/icons/frontend-framework.png', 1, 2, 70, '前端框架学习，包括 React、Vue 等', 0, 1, 1, 1, 0, 1, 1, NOW(), NOW()),
(5, '后端开发', 'https://haha-planet.oss-cn-shanghai.aliyuncs.com/icons/backend.png', 0, 1, 90, '后端开发相关课程，包括 Java、Python、Go 等', 0, 1, 1, 1, 0, 1, 1, NOW(), NOW()),
(6, 'Java', 'https://haha-planet.oss-cn-shanghai.aliyuncs.com/icons/java.png', 5, 2, 90, 'Java 编程语言基础与进阶', 0, 1, 1, 1, 0, 1, 1, NOW(), NOW()),
(7, 'Python', 'https://haha-planet.oss-cn-shanghai.aliyuncs.com/icons/python.png', 5, 2, 80, 'Python 编程语言基础与应用', 0, 1, 1, 1, 0, 1, 1, NOW(), NOW()),
(8, 'Go', 'https://haha-planet.oss-cn-shanghai.aliyuncs.com/icons/go.png', 5, 2, 70, 'Go 语言编程基础与实战', 0, 1, 1, 1, 0, 1, 1, NOW(), NOW()),
(9, '移动开发', 'https://haha-planet.oss-cn-shanghai.aliyuncs.com/icons/mobile.png', 0, 1, 80, '移动应用开发课程，包括 Android、iOS 等', 0, 1, 1, 1, 0, 1, 1, NOW(), NOW()),
(10, '人工智能', 'https://haha-planet.oss-cn-shanghai.aliyuncs.com/icons/ai.png', 0, 1, 70, '人工智能相关课程，包括机器学习、深度学习等', 0, 1, 1, 1, 0, 1, 1, NOW(), NOW());


-- 课程数据
INSERT INTO `cou_course` (`id`, `course_code`, `name`, `description`, `avatar`, `cover_img`, `teacher_id`, `category_id`, `planet_id`, `tags`, `price_type`, `price`, `original_price`, `discount_price`, `discount_start_time`, `discount_end_time`, `join_type`, `is_public`, `member_limit`, `join_question`, `need_approval`, `allow_post`, `allow_post_anonymous`, `allow_watermark`, `student_count`, `chapter_count`, `lesson_count`, `total_duration`, `view_count`, `like_count`, `share_count`, `income`, `hot_score`, `quality_score`, `is_featured`, `is_official`, `difficulty_level`, `course_type`, `course_status`, `valid_period`, `close_reason`, `extra_config`, `introduction`, `learning_objectives`, `version`, `status`, `is_deleted`, `created_by`, `updated_by`, `created_at`, `updated_at`) VALUES
(1, 'JAVA001', 'Java核心编程实战', 'Java核心编程实战课程，从零基础到项目实战', 'https://haha-planet.oss-cn-shanghai.aliyuncs.com/avatars/java-core.png', 'https://haha-planet.oss-cn-shanghai.aliyuncs.com/covers/java-core.jpg', 1, 6, 101, NULL, 1, 29900, 39900, 29900, '2025-01-01 00:00:00', '2025-12-31 23:59:59', 1, 1, 1000, NULL, 0, 1, 0, 1, 0, 5, 25, 3600, 0, 0, 0, 0, 0, 0, 1, 1, 2, 1, 1, 365, NULL, '{"certificate":true,"forum":true}', '<p>本课程是一门面向零基础学员的 Java 核心编程课程，包含 Java 语言基础、面向对象编程、常用类库和项目实战等内容。</p>', '<ul><li>掌握 Java 语言基础语法</li><li>理解面向对象编程思想</li><li>学会使用 Java 常用类库</li><li>能够独立完成简单的 Java 项目</li></ul>', 1, 1, 0, 1, 1, NOW(), NOW()),
(2, 'WEB001', 'Web前端开发入门与进阶', '全面讲解Web前端开发技术，从 HTML/CSS 到 JavaScript 再到框架', 'https://haha-planet.oss-cn-shanghai.aliyuncs.com/avatars/web-dev.png', 'https://haha-planet.oss-cn-shanghai.aliyuncs.com/covers/web-dev.jpg', 2, 1, 102, NULL, 1, 19900, 29900, 19900, '2025-01-01 00:00:00', '2025-12-31 23:59:59', 1, 1, 2000, NULL, 0, 1, 0, 1, 0, 4, 20, 2400, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 365, NULL, '{"certificate":true,"forum":true}', '<p>本课程是一门面向零基础学员的 Web 前端开发课程，包含 HTML、CSS、JavaScript 基础知识和前端框架入门等内容。</p>', '<ul><li>掌握 HTML5 和 CSS3 技术</li><li>理解 JavaScript 核心概念</li><li>学会使用前端框架</li><li>能够独立开发响应式网站</li></ul>', 1, 1, 0, 1, 1, NOW(), NOW()),
(3, 'PYTHON001', 'Python数据分析与可视化', '使用Python进行数据分析与可视化的实战课程', 'https://haha-planet.oss-cn-shanghai.aliyuncs.com/avatars/python-data.png', 'https://haha-planet.oss-cn-shanghai.aliyuncs.com/covers/python-data.jpg', 3, 7, 103, NULL, 1, 24900, 34900, 24900, '2025-01-01 00:00:00', '2025-12-31 23:59:59', 1, 1, 1500, NULL, 0, 1, 0, 1, 0, 4, 18, 2100, 0, 0, 0, 0, 0, 0, 1, 1, 2, 1, 1, 365, NULL, '{"certificate":true,"forum":true}', '<p>本课程是一门面向数据分析爱好者的 Python 实战课程，包含 Python 基础、数据处理、数据分析和数据可视化等内容。</p>', '<ul><li>掌握 Python 数据处理基础</li><li>理解数据分析的核心概念</li><li>学会使用数据可视化库</li><li>能够独立完成数据分析项目</li></ul>', 1, 1, 0, 1, 1, NOW(), NOW());


-- 课程章节数据
INSERT INTO `cou_chapter` (`id`, `course_id`, `title`, `description`, `sort`, `lesson_count`, `total_duration`, `is_free`, `version`, `status`, `is_deleted`, `created_by`, `updated_by`, `created_at`, `updated_at`) VALUES
-- Java课程章节
(1, 1, 'Java 入门基础', 'Java 编程语言的基础知识与环境搭建', 1, 5, 600, 1, 1, 1, 0, 1, 1, NOW(), NOW()),
(2, 1, 'Java 面向对象编程', 'Java 面向对象编程的核心概念与实践', 2, 5, 720, 0, 1, 1, 0, 1, 1, NOW(), NOW()),
(3, 1, 'Java 常用类库', 'Java 标准类库与常用工具类的使用', 3, 5, 780, 0, 1, 1, 0, 1, 1, NOW(), NOW()),
(4, 1, 'Java 高级特性', 'Java 高级特性与设计模式', 4, 5, 750, 0, 1, 1, 0, 1, 1, NOW(), NOW()),
(5, 1, 'Java 项目实战', '综合应用 Java 知识进行项目实战', 5, 5, 750, 0, 1, 1, 0, 1, 1, NOW(), NOW()),
-- Web前端课程章节
(6, 2, 'HTML5 基础', 'HTML5 的基础标签与语法', 1, 5, 600, 1, 1, 1, 0, 1, 1, NOW(), NOW()),
(7, 2, 'CSS3 样式与布局', 'CSS3 样式、选择器与布局技术', 2, 5, 600, 0, 1, 1, 0, 1, 1, NOW(), NOW()),
(8, 2, 'JavaScript 基础', 'JavaScript 语言基础与 DOM 操作', 3, 5, 600, 0, 1, 1, 0, 1, 1, NOW(), NOW()),
(9, 2, '前端框架入门', '主流前端框架的基础使用', 4, 5, 600, 0, 1, 1, 0, 1, 1, NOW(), NOW()),
-- Python课程章节
(10, 3, 'Python 基础', 'Python 编程语言的基础语法与数据类型', 1, 5, 600, 1, 1, 1, 0, 1, 1, NOW(), NOW()),
(11, 3, 'NumPy 与 Pandas 基础', '数据处理库 NumPy 与 Pandas 的基础使用', 2, 5, 600, 0, 1, 1, 0, 1, 1, NOW(), NOW()),
(12, 3, '数据分析与清洗', '数据分析、清洗与预处理技术', 3, 4, 480, 0, 1, 1, 0, 1, 1, NOW(), NOW()),
(13, 3, '数据可视化', '使用 Matplotlib 与 Seaborn 进行数据可视化', 4, 4, 420, 0, 1, 1, 0, 1, 1, NOW(), NOW());

-- 课程内容数据 (仅展示部分课时数据作为示例)
INSERT INTO `cou_lesson` (`id`, `course_id`, `chapter_id`, `title`, `description`, `content_type`, `content_url`, `video_duration`, `sort`, `is_free`, `is_trial`, `is_required`, `resource_json`, `text_content`, `version`, `status`, `is_deleted`, `created_by`, `updated_by`, `created_at`, `updated_at`) VALUES
-- Java课程第一章课时
(1, 1, 1, 'Java 简介与环境搭建', 'Java 编程语言的简介与开发环境搭建', 1, 'https://haha-planet.oss-cn-shanghai.aliyuncs.com/videos/java/java-intro.mp4', 720, 1, 1, 1, 1, '{"attachments":[{"name":"环境搭建指南.pdf","url":"https://haha-planet.oss-cn-shanghai.aliyuncs.com/resources/java/env-guide.pdf"}]}', NULL, 1, 1, 0, 1, 1, NOW(), NOW()),
(2, 1, 1, 'Java 基础语法', 'Java 编程语言的基础语法与数据类型', 1, 'https://haha-planet.oss-cn-shanghai.aliyuncs.com/videos/java/java-syntax.mp4', 900, 2, 1, 0, 1, '{"attachments":[{"name":"语法速查表.pdf","url":"https://haha-planet.oss-cn-shanghai.aliyuncs.com/resources/java/syntax-cheatsheet.pdf"}]}', NULL, 1, 1, 0, 1, 1, NOW(), NOW()),
(3, 1, 1, '变量与运算符', 'Java 中的变量声明、赋值与运算符使用', 1, 'https://haha-planet.oss-cn-shanghai.aliyuncs.com/videos/java/java-variables.mp4', 840, 3, 1, 0, 1, NULL, NULL, 1, 1, 0, 1, 1, NOW(), NOW()),
(4, 1, 1, '控制流程语句', 'Java 中的条件语句、循环语句与分支结构', 1, 'https://haha-planet.oss-cn-shanghai.aliyuncs.com/videos/java/java-control-flow.mp4', 780, 4, 0, 0, 1, NULL, NULL, 1, 1, 0, 1, 1, NOW(), NOW()),
(5, 1, 1, '数组与字符串', 'Java 中的数组操作与字符串处理', 1, 'https://haha-planet.oss-cn-shanghai.aliyuncs.com/videos/java/java-arrays-strings.mp4', 860, 5, 0, 0, 1, NULL, NULL, 1, 1, 0, 1, 1, NOW(), NOW()),

-- Web前端课程第一章课时
(6, 2, 6, 'HTML5 简介', 'HTML5 的发展历史与基本结构', 1, 'https://haha-planet.oss-cn-shanghai.aliyuncs.com/videos/web/html5-intro.mp4', 720, 1, 1, 1, 1, '{"attachments":[{"name":"HTML5参考手册.pdf","url":"https://haha-planet.oss-cn-shanghai.aliyuncs.com/resources/web/html5-reference.pdf"}]}', NULL, 1, 1, 0, 1, 1, NOW(), NOW()),
(7, 2, 6, '基础标签与属性', 'HTML5 的基础标签与属性使用', 1, 'https://haha-planet.oss-cn-shanghai.aliyuncs.com/videos/web/html5-basic-tags.mp4', 780, 2, 1, 0, 1, NULL, NULL, 1, 1, 0, 1, 1, NOW(), NOW()),
(8, 2, 6, '表单与表格', 'HTML5 中的表单元素与表格结构', 1, 'https://haha-planet.oss-cn-shanghai.aliyuncs.com/videos/web/html5-forms-tables.mp4', 840, 3, 1, 0, 1, NULL, NULL, 1, 1, 0, 1, 1, NOW(), NOW()),
(9, 2, 6, '多媒体元素', 'HTML5 中的音频、视频与画布元素', 1, 'https://haha-planet.oss-cn-shanghai.aliyuncs.com/videos/web/html5-multimedia.mp4', 720, 4, 0, 0, 1, NULL, NULL, 1, 1, 0, 1, 1, NOW(), NOW()),
(10, 2, 6, 'HTML5 语义化标签', 'HTML5 新增的语义化标签与最佳实践', 1, 'https://haha-planet.oss-cn-shanghai.aliyuncs.com/videos/web/html5-semantic.mp4', 660, 5, 0, 0, 1, NULL, NULL, 1, 1, 0, 1, 1, NOW(), NOW()),

-- Python课程第一章课时
(11, 3, 10, 'Python 简介与环境搭建', 'Python 编程语言的简介与开发环境搭建', 1, 'https://haha-planet.oss-cn-shanghai.aliyuncs.com/videos/python/python-intro.mp4', 720, 1, 1, 1, 1, '{"attachments":[{"name":"Python环境搭建指南.pdf","url":"https://haha-planet.oss-cn-shanghai.aliyuncs.com/resources/python/env-guide.pdf"}]}', NULL, 1, 1, 0, 1, 1, NOW(), NOW()),
(12, 3, 10, 'Python 基础语法', 'Python 编程语言的基础语法与数据类型', 1, 'https://haha-planet.oss-cn-shanghai.aliyuncs.com/videos/python/python-syntax.mp4', 780, 2, 1, 0, 1, '{"attachments":[{"name":"Python语法速查表.pdf","url":"https://haha-planet.oss-cn-shanghai.aliyuncs.com/resources/python/syntax-cheatsheet.pdf"}]}', NULL, 1, 1, 0, 1, 1, NOW(), NOW()),
(13, 3, 10, '控制流程与函数', 'Python 中的条件语句、循环与函数定义', 1, 'https://haha-planet.oss-cn-shanghai.aliyuncs.com/videos/python/python-control-flow.mp4', 840, 3, 1, 0, 1, NULL, NULL, 1, 1, 0, 1, 1, NOW(), NOW()),
(14, 3, 10, '数据结构', 'Python 中的列表、元组、字典与集合', 1, 'https://haha-planet.oss-cn-shanghai.aliyuncs.com/videos/python/python-data-structures.mp4', 780, 4, 0, 0, 1, NULL, NULL, 1, 1, 0, 1, 1, NOW(), NOW()),
(15, 3, 10, '模块与包', 'Python 中的模块导入与包管理', 1, 'https://haha-planet.oss-cn-shanghai.aliyuncs.com/videos/python/python-modules.mp4', 720, 5, 0, 0, 1, NULL, NULL, 1, 1, 0, 1, 1, NOW(), NOW());

-- 课程评价数据
INSERT INTO `cou_review` (`id`, `course_id`, `user_id`, `rating`, `content`, `pros`, `cons`, `is_anonymous`, `like_count`, `reply_count`, `is_featured`, `teacher_reply`, `completion_rate`, `review_time`, `version`, `status`, `is_deleted`, `created_by`, `updated_by`, `created_at`, `updated_at`) VALUES
(1, 1, 101, 5, '非常棒的课程，讲解清晰，案例丰富，很适合零基础学习。', '讲解清晰，案例丰富，实战性强', '课程节奏可以再快一点', 0, 5, 1, 1, '感谢支持，我们会继续提升课程质量！', 100, '2025-01-15 10:30:00', 1, 1, 0, 101, 101, NOW(), NOW()),
(2, 1, 102, 4, '课程内容很充实，适合入门学习，但有些概念讲解可以再深入一点。', '内容全面，实战项目很有帮助', '某些概念讲解不够深入', 0, 3, 0, 0, NULL, 85, '2025-01-20 14:20:00', 1, 1, 0, 102, 102, NOW(), NOW()),
(3, 2, 103, 5, '前端入门必备课程，从 HTML 到 JavaScript 再到框架，学完后能独立开发网站了！', '课程路线清晰，实战性强，讲解生动', '有些内容可以再补充更多案例', 0, 8, 2, 1, '感谢认可，我们会在后续版本中增加更多案例！', 100, '2025-02-05 09:15:00', 1, 1, 0, 103, 103, NOW(), NOW()),
(4, 2, 104, 4, '课程内容很实用，对我入门前端开发帮助很大，尤其是 CSS 部分讲解很清晰。', 'CSS 部分讲解很清晰，实战性强', '框架部分内容可以再多一些', 0, 4, 1, 0, NULL, 90, '2025-02-10 16:45:00', 1, 1, 0, 104, 104, NOW(), NOW()),
(5, 3, 105, 5, 'Python 数据分析课程很棒，从零基础到能够独立完成数据分析项目，学到了很多实用技能。', '实用性强，案例丰富，讲解清晰', '有些高级内容可以再深入', 0, 10, 3, 1, '感谢支持，我们已经在策划高级课程了！', 100, '2025-03-01 11:20:00', 1, 1, 0, 105, 105, NOW(), NOW()),
(6, 3, 106, 4, '课程内容很充实，尤其是数据可视化部分讲解很清晰，但有些内容节奏偏快。', '数据可视化部分讲解很清晰', '某些内容节奏偏快', 1, 5, 1, 0, NULL, 95, '2025-03-05 13:40:00', 1, 1, 0, 106, 106, NOW(), NOW());

-- 课程学习进度数据
INSERT INTO `cou_learning_progress` (`id`, `course_id`, `user_id`, `lesson_id`, `progress_percent`, `watch_duration`, `last_position`, `is_completed`, `completed_time`, `watch_count`, `first_watch_time`, `last_watch_time`, `notes`, `version`, `status`, `is_deleted`, `created_by`, `updated_by`, `created_at`, `updated_at`) VALUES
(1, 1, 101, 1, 100.00, 720, 720, 1, '2025-01-10 15:30:00', 2, '2025-01-09 10:00:00', '2025-01-10 15:30:00', '学习笔记：Java 是一种面向对象的编程语言，由 Sun 公司开发，现在属于 Oracle 公司。', 1, 1, 0, 101, 101, NOW(), NOW()),
(2, 1, 101, 2, 100.00, 900, 900, 1, '2025-01-11 16:20:00', 1, '2025-01-11 14:00:00', '2025-01-11 16:20:00', '学习笔记：Java 的基本数据类型包括：int、double、boolean 等。', 1, 1, 0, 101, 101, NOW(), NOW()),
(3, 1, 101, 3, 100.00, 840, 840, 1, '2025-01-12 11:45:00', 1, '2025-01-12 10:00:00', '2025-01-12 11:45:00', '学习笔记：变量声明格式：类型 变量名 = 初始值；', 1, 1, 0, 101, 101, NOW(), NOW()),
(4, 1, 101, 4, 75.00, 585, 585, 0, NULL, 1, '2025-01-13 14:30:00', '2025-01-13 14:30:00', '学习笔记：if-else 条件语句、for 循环、while 循环的基本用法。', 1, 1, 0, 101, 101, NOW(), NOW()),
(5, 2, 103, 6, 100.00, 720, 720, 1, '2025-02-01 10:15:00', 2, '2025-01-31 16:00:00', '2025-02-01 10:15:00', '学习笔记：HTML5 是最新的 HTML 标准，增加了许多新的语义化标签。', 1, 1, 0, 103, 103, NOW(), NOW()),
(6, 2, 103, 7, 100.00, 780, 780, 1, '2025-02-02 11:30:00', 1, '2025-02-02 09:30:00', '2025-02-02 11:30:00', '学习笔记：常用的 HTML 标签包括 div、span、p、h1-h6 等。', 1, 1, 0, 103, 103, NOW(), NOW()),
(7, 3, 105, 11, 100.00, 720, 720, 1, '2025-02-20 14:20:00', 1, '2025-02-20 12:40:00', '2025-02-20 14:20:00', '学习笔记：Python 是一种简洁、易学的编程语言，非常适合数据分析和科学计算。', 1, 1, 0, 105, 105, NOW(), NOW()),
(8, 3, 105, 12, 100.00, 780, 780, 1, '2025-02-21 16:10:00', 2, '2025-02-21 10:00:00', '2025-02-21 16:10:00', '学习笔记：Python 的基本数据类型包括整数、浮点数、字符串、列表、元组、字典等。', 1, 1, 0, 105, 105, NOW(), NOW());

-- 证书模板数据
INSERT INTO `cou_certificate_template` (`id`, `template_name`, `template_code`, `template_type`, `template_url`, `preview_url`, `template_config`, `is_default`, `sort_order`, `description`, `version`, `status`, `is_deleted`, `created_by`, `updated_by`, `created_at`, `updated_at`) VALUES
(1, '标准课程完成证书', 'STANDARD_COURSE', 1, 'https://haha-planet.oss-cn-shanghai.aliyuncs.com/templates/standard-course-template.pdf', 'https://haha-planet.oss-cn-shanghai.aliyuncs.com/templates/previews/standard-course-preview.jpg', '{"title_position":{"x":400,"y":200},"name_position":{"x":400,"y":300},"course_position":{"x":400,"y":350},"date_position":{"x":400,"y":400},"font_family":"SimSun","font_size":24}', 1, 1, '标准的课程完成证书模板，适用于大部分课程', 1, 1, 0, 1, 1, NOW(), NOW()),
(2, '技能认证证书', 'SKILL_CERT', 3, 'https://haha-planet.oss-cn-shanghai.aliyuncs.com/templates/skill-cert-template.pdf', 'https://haha-planet.oss-cn-shanghai.aliyuncs.com/templates/previews/skill-cert-preview.jpg', '{"title_position":{"x":400,"y":180},"name_position":{"x":400,"y":280},"skill_position":{"x":400,"y":330},"score_position":{"x":400,"y":380},"date_position":{"x":400,"y":430},"font_family":"Microsoft YaHei","font_size":22}', 0, 2, '技能认证证书模板，包含技能等级和分数信息', 1, 1, 0, 1, 1, NOW(), NOW()),
(3, '考试通过证书', 'EXAM_PASS', 2, 'https://haha-planet.oss-cn-shanghai.aliyuncs.com/templates/exam-pass-template.pdf', 'https://haha-planet.oss-cn-shanghai.aliyuncs.com/templates/previews/exam-pass-preview.jpg', '{"title_position":{"x":400,"y":190},"name_position":{"x":400,"y":290},"exam_position":{"x":400,"y":340},"score_position":{"x":400,"y":390},"date_position":{"x":400,"y":440},"font_family":"KaiTi","font_size":20}', 0, 3, '考试通过证书模板，显示考试成绩和通过状态', 1, 1, 0, 1, 1, NOW(), NOW());

-- 课程报名数据
INSERT INTO `cou_enrollment` (`id`, `course_id`, `user_id`, `order_id`, `enrollment_type`, `enrollment_time`, `expire_time`, `learning_progress`, `last_study_time`, `total_study_duration`, `completed_lessons`, `total_lessons`, `certificate_issued`, `source`, `version`, `status`, `is_deleted`, `created_by`, `updated_by`, `created_at`, `updated_at`) VALUES
(1, 1, 101, 1001, 1, '2025-01-09 10:00:00', '2026-01-09 10:00:00', 75.00, '2025-01-13 14:30:00', 180, 3, 25, 0, 'web', 1, 1, 0, 101, 101, NOW(), NOW()),
(2, 2, 103, 1002, 1, '2025-01-31 16:00:00', '2026-01-31 16:00:00', 100.00, '2025-02-02 11:30:00', 120, 20, 20, 1, 'web', 1, 2, 0, 103, 103, NOW(), NOW()),
(3, 3, 105, 1003, 1, '2025-02-20 12:40:00', '2026-02-20 12:40:00', 100.00, '2025-02-21 16:10:00', 150, 18, 18, 1, 'web', 1, 2, 0, 105, 105, NOW(), NOW());

-- 课程证书数据
INSERT INTO `cou_certificate` (`id`, `course_id`, `user_id`, `certificate_no`, `certificate_name`, `certificate_url`, `issue_date`, `expire_date`, `completion_rate`, `score`, `template_id`, `extra_info`, `version`, `status`, `is_deleted`, `created_by`, `updated_by`, `created_at`, `updated_at`) VALUES
(1, 2, 103, 'CERT-WEB001-103-20250202', 'Web前端开发入门与进阶课程结业证书', 'https://haha-planet.oss-cn-shanghai.aliyuncs.com/certificates/cert-web001-103.pdf', '2025-02-02 12:00:00', '2030-02-02 12:00:00', 100.00, 95.50, 1, '{"course_name":"Web前端开发入门与进阶","student_name":"张三","issue_org":"哈哈星球"}', 1, 1, 0, 1, 1, NOW(), NOW()),
(2, 3, 105, 'CERT-PYTHON001-105-20250221', 'Python数据分析与可视化课程结业证书', 'https://haha-planet.oss-cn-shanghai.aliyuncs.com/certificates/cert-python001-105.pdf', '2025-02-21 17:00:00', '2030-02-21 17:00:00', 100.00, 88.75, 1, '{"course_name":"Python数据分析与可视化","student_name":"李四","issue_org":"哈哈星球"}', 1, 1, 0, 1, 1, NOW(), NOW());

-- 课程作业数据
INSERT INTO `cou_assignment` (`id`, `course_id`, `chapter_id`, `lesson_id`, `title`, `description`, `content`, `assignment_type`, `difficulty_level`, `max_score`, `pass_score`, `time_limit`, `max_attempts`, `start_time`, `end_time`, `is_required`, `auto_grade`, `reference_answer`, `grading_criteria`, `attachment_urls`, `version`, `status`, `is_deleted`, `created_by`, `updated_by`, `created_at`, `updated_at`) VALUES
(1, 1, 2, NULL, 'Java面向对象编程实战', '设计并实现一个简单的学生管理系统，包含学生类、课程类等', '<p>请使用Java面向对象编程思想，设计并实现一个学生管理系统：</p><ul><li>创建Student类，包含姓名、学号、年龄等属性</li><li>创建Course类，包含课程名称、学分、教师等属性</li><li>实现学生选课、退课等功能</li><li>使用集合类存储学生和课程信息</li></ul>', 1, 2, 100.00, 60.00, 120, 3, '2025-01-15 00:00:00', '2025-01-30 23:59:59', 1, 0, '<p>参考实现：</p><pre><code>public class Student {\n    private String name;\n    private String studentId;\n    private int age;\n    // 构造方法和getter/setter方法\n}</code></pre>', '{"structure":30,"functionality":40,"code_quality":20,"documentation":10}', '["https://haha-planet.oss-cn-shanghai.aliyuncs.com/assignments/java-oop-template.zip"]', 1, 1, 0, 1, 1, NOW(), NOW()),
(2, 2, 4, NULL, '响应式网页设计实战', '使用HTML5、CSS3和JavaScript实现一个响应式个人作品集网站', '<p>请创建一个响应式的个人作品集网站：</p><ul><li>使用HTML5语义化标签构建页面结构</li><li>使用CSS3实现响应式布局和动画效果</li><li>使用JavaScript添加交互功能</li><li>确保在不同设备上都有良好的显示效果</li></ul>', 3, 2, 100.00, 70.00, 180, 2, '2025-02-10 00:00:00', '2025-02-25 23:59:59', 1, 0, '<p>参考设计要点：</p><ul><li>使用Flexbox或Grid布局</li><li>设置合适的媒体查询断点</li><li>优化图片和字体加载</li></ul>', '{"design":25,"responsive":25,"functionality":25,"code_quality":25}', '["https://haha-planet.oss-cn-shanghai.aliyuncs.com/assignments/portfolio-template.zip"]', 1, 1, 0, 1, 1, NOW(), NOW()),
(3, 3, 4, NULL, 'Python数据可视化项目', '使用Python分析并可视化一个真实数据集', '<p>选择一个公开数据集，完成以下任务：</p><ul><li>使用Pandas进行数据清洗和预处理</li><li>使用Matplotlib或Seaborn创建至少5种不同类型的图表</li><li>分析数据中的趋势和模式</li><li>撰写数据分析报告</li></ul>', 3, 2, 100.00, 70.00, 240, 2, '2025-03-01 00:00:00', '2025-03-20 23:59:59', 1, 0, '<p>参考分析流程：</p><ol><li>数据探索和清洗</li><li>描述性统计分析</li><li>数据可视化</li><li>结论和建议</li></ol>', '{"data_processing":30,"visualization":30,"analysis":25,"report":15}', '["https://haha-planet.oss-cn-shanghai.aliyuncs.com/assignments/data-analysis-template.ipynb"]', 1, 1, 0, 1, 1, NOW(), NOW());

-- 作业提交数据
INSERT INTO `cou_assignment_submission` (`id`, `assignment_id`, `user_id`, `submission_content`, `attachment_urls`, `code_repository`, `submit_time`, `score`, `grade_time`, `grader_id`, `feedback`, `attempt_count`, `is_passed`, `grade_status`, `version`, `status`, `is_deleted`, `created_by`, `updated_by`, `created_at`, `updated_at`) VALUES
(1, 2, 103, '<p>我已完成响应式个人作品集网站的开发，主要特性包括：</p><ul><li>使用HTML5语义化标签</li><li>CSS3 Flexbox布局</li><li>JavaScript交互效果</li><li>移动端适配</li></ul>', '["https://haha-planet.oss-cn-shanghai.aliyuncs.com/submissions/portfolio-103.zip"]', 'https://github.com/student103/portfolio-website', '2025-02-20 15:30:00', 85.00, '2025-02-22 10:00:00', 2, '作品整体质量很好，响应式设计实现得不错。建议优化一下加载速度，可以考虑图片懒加载。', 1, 1, 2, 1, 2, 0, 103, 103, NOW(), NOW()),
(2, 3, 105, '<p>我选择了全球气温变化数据集进行分析，完成了以下工作：</p><ul><li>数据清洗和预处理</li><li>创建了折线图、柱状图、热力图等多种可视化图表</li><li>分析了全球气温变化趋势</li><li>撰写了详细的分析报告</li></ul>', '["https://haha-planet.oss-cn-shanghai.aliyuncs.com/submissions/climate-analysis-105.zip"]', 'https://github.com/student105/climate-data-analysis', '2025-03-15 20:45:00', 92.00, '2025-03-17 14:20:00', 3, '数据分析非常全面，可视化图表制作精美，分析结论有理有据。是一份优秀的作业！', 1, 1, 2, 1, 2, 0, 105, 105, NOW(), NOW());