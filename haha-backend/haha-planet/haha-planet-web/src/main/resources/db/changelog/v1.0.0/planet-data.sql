-- liquibase formatted sql

-- changeset whoiszxl:1
-- comment 星球模块: 初始化模块表数据

-- 插入星球分类数据
INSERT INTO `pla_planet_category` (`id`, `category_name`, `icon_url`, `parent_id`, `level`, `sort`, `created_by`) VALUES
(1, '技术开发', 'https://example.com/icons/tech.png', 0, 1, 100, 1),
(2, '产品设计', 'https://example.com/icons/design.png', 0, 1, 90, 1),
(3, '运营推广', 'https://example.com/icons/marketing.png', 0, 1, 80, 1),
(4, '职场成长', 'https://example.com/icons/career.png', 0, 1, 70, 1),
(5, '投资理财', 'https://example.com/icons/finance.png', 0, 1, 60, 1),
(6, 'Java开发', 'https://example.com/icons/java.png', 1, 2, 50, 1),
(7, 'Python开发', 'https://example.com/icons/python.png', 1, 2, 40, 1),
(8, '前端开发', 'https://example.com/icons/frontend.png', 1, 2, 30, 1);

-- 插入星球标签数据
INSERT INTO `pla_planet_tag` (`id`, `name`, `color`, `use_count`, `category_id`, `created_by`) VALUES
(1, 'Java', '#f50', 15, 1, 1),
(2, 'Spring Boot', '#2db7f5', 12, 1, 1),
(3, 'Python', '#87d068', 10, 1, 1),
(4, 'Vue.js', '#108ee9', 8, 1, 1),
(5, 'React', '#f56a00', 6, 1, 1),
(6, '产品经理', '#722ed1', 5, 2, 1),
(7, 'UI设计', '#eb2f96', 4, 2, 1),
(8, '数据分析', '#52c41a', 3, 3, 1),
(9, '职场规划', '#1890ff', 2, 4, 1),
(10, '股票投资', '#fa8c16', 1, 5, 1);

-- 插入星球数据
INSERT INTO `pla_planet` (`id`, `planet_code`, `name`, `description`, `avatar`, `cover_image`, `owner_id`, `category_id`, `tags`, `price_type`, `price`, `original_price`, `discount_price`, `join_type`, `is_public`, `max_members`, `join_question`, `auto_approve`, `allow_member_post`, `post_need_approve`, `allow_anonymous`, `watermark_enabled`, `member_count`, `post_count`, `view_count`, `like_count`, `share_count`, `total_income`, `hot_score`, `quality_score`, `last_active_time`, `recommend_weight`, `is_featured`, `is_official`, `extra_config`, `notice`, `rules`, `created_by`) VALUES
(1, 'JAVA_MASTER_2024', 'Java技术大师班', '专注于Java后端开发技术分享，包括Spring Boot、微服务、分布式系统等前沿技术。适合有一定基础的Java开发者进阶学习。', 'https://example.com/avatars/java_master.jpg', 'https://example.com/covers/java_master_cover.jpg', 1001, 6, 'Java,Spring Boot,微服务,分布式', 2, 299.00, 399.00, 199.00, 1, 1, 500, NULL, 1, 1, 0, 0, 1, 156, 89, 2340, 234, 45, 46844.00, 85, 4.8, '2024-01-15 14:30:00', 90, 1, 0, '{"theme": "tech", "level": "advanced"}', '欢迎加入Java技术大师班！这里有最新的技术分享和实战项目。', '1. 禁止发布广告内容\n2. 保持技术讨论的专业性\n3. 尊重他人，友善交流', 1001),
(2, 'PYTHON_AI_2024', 'Python AI实战营', '专注Python人工智能开发，涵盖机器学习、深度学习、数据分析等领域。从基础到实战，助你成为AI工程师。', 'https://example.com/avatars/python_ai.jpg', 'https://example.com/covers/python_ai_cover.jpg', 1002, 7, 'Python,AI,机器学习,深度学习', 2, 399.00, 599.00, 299.00, 2, 1, 300, '请简述你的Python基础和学习AI的目标', 0, 1, 1, 0, 1, 89, 67, 1890, 189, 23, 26611.00, 78, 4.6, '2024-01-15 16:45:00', 85, 1, 0, '{"theme": "ai", "level": "intermediate"}', 'Python AI实战营开营啦！每周都有实战项目分享。', '1. 发帖前请先搜索是否有重复内容\n2. 代码分享请使用代码块格式\n3. 积极参与讨论，共同进步', 1002),
(3, 'FRONTEND_PRO_2024', '前端开发进阶圈', '前端技术交流社区，涵盖Vue、React、小程序等技术栈。分享最新前端趋势和实战经验。', 'https://example.com/avatars/frontend_pro.jpg', 'https://example.com/covers/frontend_pro_cover.jpg', 1003, 8, 'Vue.js,React,前端,小程序', 1, 0.00, 0.00, 0.00, 1, 1, 0, NULL, 1, 1, 0, 1, 0, 234, 145, 3456, 345, 67, 0.00, 92, 4.7, '2024-01-15 18:20:00', 95, 0, 0, '{"theme": "frontend", "level": "all"}', '前端开发者的聚集地，欢迎大家分享技术心得！', '1. 技术分享优先\n2. 可以匿名发帖\n3. 鼓励原创内容', 1003),
(4, 'PRODUCT_DESIGN_2024', '产品设计思维圈', '产品经理和设计师的交流平台，分享产品设计方法论、用户体验设计、产品运营策略等。', 'https://example.com/avatars/product_design.jpg', 'https://example.com/covers/product_design_cover.jpg', 1004, 2, '产品经理,UI设计,用户体验,产品运营', 2, 199.00, 299.00, 149.00, 3, 1, 200, NULL, 1, 1, 1, 0, 0, 67, 34, 1234, 123, 12, 13333.00, 65, 4.4, '2024-01-15 10:15:00', 70, 0, 1, '{"theme": "design", "level": "professional"}', '产品设计思维圈，让设计更有温度！', '1. 分享设计作品请注明版权\n2. 讨论要有建设性\n3. 保护商业机密', 1004),
(5, 'CAREER_GROWTH_2024', '职场成长加油站', '职场人的成长社区，分享职业规划、面试技巧、工作经验、职场心得等内容。', 'https://example.com/avatars/career_growth.jpg', 'https://example.com/covers/career_growth_cover.jpg', 1005, 4, '职场规划,面试技巧,工作经验,职场心得', 1, 0.00, 0.00, 0.00, 1, 1, 0, NULL, 1, 1, 0, 1, 0, 345, 234, 4567, 456, 89, 0.00, 88, 4.5, '2024-01-15 12:30:00', 80, 0, 0, '{"theme": "career", "level": "all"}', '职场路上不孤单，一起成长一起进步！', '1. 分享真实经历\n2. 互相鼓励支持\n3. 保护个人隐私', 1005);

-- 插入星球标签关联数据
INSERT INTO `pla_planet_tag_relation` (`planet_id`, `tag_id`, `created_by`) VALUES
(1, 1, 1001), (1, 2, 1001),
(2, 3, 1002),
(3, 4, 1003), (3, 5, 1003),
(4, 6, 1004), (4, 7, 1004),
(5, 9, 1005);

-- 插入星球成员数据
INSERT INTO `pla_planet_member` (`planet_id`, `user_id`, `member_type`, `join_time`, `expire_time`, `join_source`, `inviter_id`, `order_id`, `nickname`, `total_posts`, `total_likes`, `created_by`) VALUES
-- Java技术大师班成员
(1, 1001, 3, '2024-01-01 10:00:00', NULL, 1, NULL, NULL, 'Java大师', 15, 89, 1001),
(1, 2001, 1, '2024-01-02 14:30:00', '2025-01-02 14:30:00', 1, NULL, 10001, 'Spring学徒', 8, 45, 2001),
(1, 2002, 1, '2024-01-03 09:15:00', '2025-01-03 09:15:00', 2, 1001, 10002, '微服务探索者', 12, 67, 2002),
(1, 2003, 2, '2024-01-04 16:45:00', '2025-01-04 16:45:00', 1, NULL, 10003, 'Java架构师', 20, 123, 2003),
-- Python AI实战营成员
(2, 1002, 3, '2024-01-01 11:00:00', NULL, 1, NULL, NULL, 'AI导师', 12, 78, 1002),
(2, 2004, 1, '2024-01-05 10:20:00', '2025-01-05 10:20:00', 1, NULL, 10004, 'ML新手', 5, 23, 2004),
(2, 2005, 1, '2024-01-06 15:30:00', '2025-01-06 15:30:00', 2, 1002, 10005, '数据科学家', 9, 56, 2005),
-- 前端开发进阶圈成员
(3, 1003, 3, '2024-01-01 12:00:00', NULL, 1, NULL, NULL, '前端大神', 25, 156, 1003),
(3, 2006, 1, '2024-01-07 11:45:00', NULL, 1, NULL, NULL, 'Vue爱好者', 18, 89, 2006),
(3, 2007, 1, '2024-01-08 14:20:00', NULL, 3, 2006, NULL, 'React开发者', 22, 134, 2007),
-- 产品设计思维圈成员
(4, 1004, 3, '2024-01-01 13:00:00', NULL, 1, NULL, NULL, '设计总监', 8, 45, 1004),
(4, 2008, 1, '2024-01-09 09:30:00', '2025-01-09 09:30:00', 1, NULL, 10006, 'UI设计师', 6, 34, 2008),
-- 职场成长加油站成员
(5, 1005, 3, '2024-01-01 14:00:00', NULL, 1, NULL, NULL, '职场导师', 35, 234, 1005),
(5, 2009, 1, '2024-01-10 16:15:00', NULL, 1, NULL, NULL, '职场新人', 12, 67, 2009),
(5, 2010, 1, '2024-01-11 10:45:00', NULL, 2, 1005, NULL, '资深HR', 28, 189, 2010);

-- 插入星球帖子数据
INSERT INTO `pla_planet_post` (`id`, `planet_id`, `user_id`, `title`, `content`, `content_type`, `media_urls`, `is_anonymous`, `is_top`, `is_essence`, `view_count`, `like_count`, `comment_count`, `share_count`, `collect_count`, `audit_status`, `last_comment_time`, `hot_score`, `created_by`) VALUES
(1, 1, 1001, 'Spring Boot 3.0 新特性详解', '本文详细介绍Spring Boot 3.0的主要新特性，包括对Java 17的支持、GraalVM原生镜像支持、以及性能优化等方面的改进...', 1, NULL, 0, 1, 1, 234, 45, 12, 8, 15, 2, '2024-01-15 14:30:00', 85, 1001),
(2, 1, 2003, '微服务架构设计最佳实践', '分享在实际项目中微服务架构设计的经验和踩过的坑，包括服务拆分、数据一致性、分布式事务等关键问题的解决方案...', 1, '["https://example.com/images/microservice1.jpg", "https://example.com/images/microservice2.jpg"]', 0, 0, 1, 189, 38, 8, 5, 12, 2, '2024-01-14 16:45:00', 78, 2003),
(3, 2, 1002, 'PyTorch深度学习入门教程', '从零开始学习PyTorch深度学习框架，包括张量操作、自动求导、神经网络构建等基础知识...', 1, '["https://example.com/videos/pytorch_tutorial.mp4"]', 0, 1, 0, 156, 32, 6, 3, 8, 2, '2024-01-13 18:20:00', 72, 1002),
(4, 3, 1003, 'Vue 3 Composition API 实战指南', 'Vue 3 Composition API的详细使用指南，通过实际项目案例展示如何更好地组织和复用代码...', 1, NULL, 0, 0, 1, 145, 28, 5, 4, 6, 2, '2024-01-12 10:15:00', 68, 1003),
(5, 3, 2007, 'React Hooks 最佳实践', '分享React Hooks在实际开发中的最佳实践，包括useState、useEffect、自定义Hooks等的使用技巧...', 1, NULL, 1, 0, 0, 123, 25, 4, 2, 5, 2, '2024-01-11 14:30:00', 65, 2007),
(6, 4, 1004, '产品需求分析方法论', '如何进行有效的产品需求分析，从用户调研到需求文档编写的完整流程...', 1, '["https://example.com/docs/requirement_analysis.pdf"]', 0, 0, 1, 98, 18, 3, 2, 4, 2, '2024-01-10 16:45:00', 58, 1004),
(7, 5, 1005, '程序员职业规划指南', '作为一名资深程序员，分享从初级到高级的职业发展路径和建议...', 1, NULL, 0, 1, 1, 234, 56, 15, 12, 18, 2, '2024-01-15 12:30:00', 88, 1005),
(8, 5, 2010, 'IT行业面试技巧分享', '从HR角度分享IT行业面试的注意事项和技巧，帮助大家更好地准备面试...', 1, NULL, 0, 0, 0, 167, 34, 8, 6, 9, 2, '2024-01-14 09:20:00', 75, 2010);

-- 插入评论数据
INSERT INTO `pla_planet_comment` (`post_id`, `planet_id`, `user_id`, `parent_id`, `reply_to_user_id`, `content`, `is_anonymous`, `like_count`, `reply_count`, `created_by`) VALUES
(1, 1, 2001, 0, NULL, '感谢分享！Spring Boot 3.0的GraalVM支持确实很棒，启动速度提升明显。', 0, 8, 2, 2001),
(1, 1, 2002, 1, 2001, '是的，我们项目已经在使用了，内存占用也减少了不少。', 0, 5, 0, 2002),
(1, 1, 1001, 1, 2001, '很高兴对你有帮助，后续会继续分享更多实战经验。', 0, 3, 0, 1001),
(2, 1, 2001, 0, NULL, '微服务确实复杂，但是这篇文章讲得很清楚，收藏了！', 0, 6, 1, 2001),
(3, 2, 2004, 0, NULL, '正在学习PyTorch，这个教程来得太及时了！', 0, 4, 0, 2004),
(4, 3, 2006, 0, NULL, 'Composition API确实比Options API更灵活，感谢分享！', 0, 7, 0, 2006),
(7, 5, 2009, 0, NULL, '作为职场新人，这篇文章给了我很多启发，谢谢！', 0, 12, 3, 2009),
(7, 5, 2010, 7, 2009, '职场路上确实需要规划，建议多参加技术分享和培训。', 0, 8, 0, 2010);

-- 插入点赞记录
INSERT INTO `pla_planet_like` (`planet_id`, `user_id`, `target_type`, `target_id`, `target_user_id`, `created_by`) VALUES
(1, 2001, 1, 1, 1001, 2001),
(1, 2002, 1, 1, 1001, 2002),
(1, 2003, 1, 2, 2003, 2003),
(2, 2004, 1, 3, 1002, 2004),
(3, 2006, 1, 4, 1003, 2006),
(3, 2007, 1, 5, 2007, 2007),
(5, 2009, 1, 7, 1005, 2009),
(5, 2010, 1, 7, 1005, 2010),
(1, 2001, 2, 1, 2001, 2001),
(5, 2009, 2, 7, 2009, 2009);

-- 插入收藏记录
INSERT INTO `pla_planet_collect` (`planet_id`, `user_id`, `post_id`, `folder_name`, `created_by`) VALUES
(1, 2001, 1, '技术学习', 2001),
(1, 2002, 2, '架构设计', 2002),
(2, 2004, 3, '深度学习', 2004),
(3, 2006, 4, 'Vue学习', 2006),
(5, 2009, 7, '职业规划', 2009);

-- 插入星球申请记录
INSERT INTO `pla_planet_apply` (`planet_id`, `user_id`, `apply_reason`, `answer`, `apply_status`, `audit_reason`, `audit_time`, `audit_by`, `created_by`) VALUES
(2, 2011, '希望学习AI技术，提升自己的技术能力', '有Python基础，希望转向AI方向发展', 1, NULL, NULL, NULL, 2011),
(2, 2012, '对机器学习很感兴趣', '计算机专业在读，想深入学习AI', 2, '申请理由充分，欢迎加入', '2024-01-15 10:30:00', 1002, 2012),
(4, 2013, '产品经理转行', '有3年产品经验，希望学习更多设计思维', 3, '当前名额已满，请等待下期开放', '2024-01-14 15:20:00', 1004, 2013);

-- 插入邀请记录
INSERT INTO `pla_planet_invite` (`planet_id`, `inviter_id`, `invitee_id`, `invite_code`, `invite_type`, `invite_message`, `expire_time`, `max_use_count`, `used_count`, `invite_status`, `created_by`) VALUES
(1, 1001, 2002, 'JAVA2024INVITE001', 1, '邀请你加入Java技术大师班，一起学习进步！', '2024-02-01 23:59:59', 1, 1, 2, 1001),
(2, 1002, 2005, 'AI2024INVITE001', 1, '欢迎加入Python AI实战营！', '2024-02-15 23:59:59', 1, 1, 2, 1002),
(3, 1003, NULL, 'FRONTEND2024SHARE', 2, '前端技术分享，欢迎大家加入！', '2024-03-01 23:59:59', 10, 3, 1, 1003),
(5, 1005, 2010, 'CAREER2024INVITE001', 1, '职场成长路上，我们一起前行！', '2024-02-20 23:59:59', 1, 1, 2, 1005);

-- 插入通知数据
INSERT INTO `pla_planet_notification` (`planet_id`, `user_id`, `sender_id`, `notification_type`, `title`, `content`, `target_type`, `target_id`, `is_read`, `read_time`, `created_by`) VALUES
(1, 2001, 1001, 1, '新帖子发布', 'Java大师发布了新帖子《Spring Boot 3.0 新特性详解》', 1, 1, 1, '2024-01-15 15:00:00', 1001),
(1, 2002, 2001, 2, '新评论', '有人评论了你关注的帖子', 1, 1, 0, NULL, 2001),
(1, 2003, 2001, 3, '获得点赞', '你的帖子《微服务架构设计最佳实践》获得了点赞', 1, 2, 1, '2024-01-14 17:00:00', 2001),
(2, 2004, 1002, 6, '星球公告', 'Python AI实战营本周学习计划发布', 3, 2, 0, NULL, 1002),
(5, 2009, 1005, 1, '新帖子发布', '职场导师发布了新帖子《程序员职业规划指南》', 1, 7, 1, '2024-01-15 13:00:00', 1005);

-- 插入星球设置数据
INSERT INTO `pla_planet_setting` (`planet_id`, `setting_key`, `setting_value`, `setting_type`, `description`, `created_by`) VALUES
(1, 'welcome_message', '欢迎加入Java技术大师班！请先阅读星球规则。', 1, '新成员欢迎消息', 1001),
(1, 'post_reward_points', '10', 2, '发帖奖励积分', 1001),
(1, 'comment_reward_points', '2', 2, '评论奖励积分', 1001),
(2, 'welcome_message', '欢迎来到Python AI实战营！', 1, '新成员欢迎消息', 1002),
(2, 'ai_model_enabled', 'true', 3, '是否启用AI助手', 1002),
(3, 'anonymous_post_enabled', 'true', 3, '是否允许匿名发帖', 1003),
(4, 'design_review_enabled', 'true', 3, '是否开启设计评审功能', 1004),
(5, 'career_guidance_enabled', 'true', 3, '是否开启职业指导功能', 1005);