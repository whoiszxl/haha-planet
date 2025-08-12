-- liquibase formatted sql

-- changeset whoiszxl:1
-- comment 星球模块: 初始化模块表数据

-- 插入星球分类数据
INSERT INTO `pla_planet_category` (`id`, `category_name`, `icon_url`, `parent_id`, `level`, `sort`, `created_by`) VALUES
(1, '编程开发', 'https://example.com/icons/tech.png', 0, 1, 100, 1),
(2, '游戏开发', 'https://example.com/icons/design.png', 0, 1, 90, 1),
(3, '人工智能', 'https://example.com/icons/marketing.png', 0, 1, 80, 1),
(4, '摄影摄像', 'https://example.com/icons/career.png', 0, 1, 70, 1),
(5, '互联网', 'https://example.com/icons/finance.png', 0, 1, 60, 1),
(6, '艺术设计', 'https://example.com/icons/java.png', 0, 1, 50, 1),
(7, '赚钱副业', 'https://example.com/icons/python.png', 0, 1, 40, 1),
(8, '信息安全', 'https://example.com/icons/frontend.png', 0, 1, 30, 1),
(9, '健康运动', 'https://example.com/icons/frontend.png', 0, 1, 30, 1),
(10, '语言学习', 'https://example.com/icons/frontend.png', 0, 1, 30, 1),
(11, '音乐创作', 'https://example.com/icons/frontend.png', 0, 1, 30, 1),
(12, '旅游探险', 'https://example.com/icons/frontend.png', 0, 1, 30, 1),
(13, '科普教育', 'https://example.com/icons/frontend.png', 0, 1, 30, 1),
(14, '心理成长', 'https://example.com/icons/frontend.png', 0, 1, 30, 1),
(15, '文学文化', 'https://example.com/icons/frontend.png', 0, 1, 30, 1),
(16, '营销销售', 'https://example.com/icons/frontend.png', 0, 1, 30, 1),
(17, '跨境电商', 'https://example.com/icons/frontend.png', 0, 1, 30, 1),
(18, '电子竞技', 'https://example.com/icons/frontend.png', 0, 1, 30, 1),
(19, '行业交流', 'https://example.com/icons/frontend.png', 0, 1, 30, 1),
(20, '读书交流', 'https://example.com/icons/frontend.png', 0, 1, 30, 1);

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
(5, 'CAREER_GROWTH_2024', '职场成长加油站', '职场人的成长社区，分享职业规划、面试技巧、工作经验、职场心得等内容。', 'https://example.com/avatars/career_growth.jpg', 'https://example.com/covers/career_growth_cover.jpg', 1005, 4, '职场规划,面试技巧,工作经验,职场心得', 1, 0.00, 0.00, 0.00, 1, 1, 0, NULL, 1, 1, 0, 1, 0, 345, 234, 4567, 456, 89, 0.00, 88, 4.5, '2024-01-15 12:30:00', 80, 0, 0, '{"theme": "career", "level": "all"}', '职场路上不孤单，一起成长一起进步！', '1. 分享真实经历\n2. 互相鼓励支持\n3. 保护个人隐私', 1005),
(6, 'GOLANG_EXPERT_2024', 'Go语言专家圈', '专注Go语言开发技术分享，包括并发编程、微服务架构、性能优化等。', 'https://example.com/avatars/golang.jpg', 'https://example.com/covers/golang_cover.jpg', 1006, 1, 'Go,并发,微服务,性能优化', 2, 199.00, 299.00, 149.00, 1, 1, 300, NULL, 1, 1, 0, 0, 1, 89, 56, 1567, 156, 23, 17711.00, 75, 4.6, '2024-01-14 10:20:00', 75, 0, 0, '{"theme": "tech", "level": "intermediate"}', 'Go语言学习交流社区', '1. 分享代码请注明来源\n2. 积极参与技术讨论', 1006),
(7, 'NODEJS_COMMUNITY_2024', 'Node.js开发社区', 'Node.js全栈开发技术交流，包括Express、Koa、Nest.js等框架学习。', 'https://example.com/avatars/nodejs.jpg', 'https://example.com/covers/nodejs_cover.jpg', 1007, 1, 'Node.js,Express,全栈开发', 1, 0.00, 0.00, 0.00, 1, 1, 0, NULL, 1, 1, 0, 1, 0, 178, 89, 2234, 223, 34, 0.00, 82, 4.4, '2024-01-13 15:30:00', 78, 0, 0, '{"theme": "tech", "level": "all"}', 'Node.js开发者聚集地', '1. 欢迎新手提问\n2. 鼓励开源项目分享', 1007),
(8, 'UNITY_GAMEDEV_2024', 'Unity游戏开发圈', 'Unity游戏开发技术分享，从入门到精通，包括2D/3D游戏制作。', 'https://example.com/avatars/unity.jpg', 'https://example.com/covers/unity_cover.jpg', 1008, 2, 'Unity,游戏开发,C#,3D建模', 2, 299.00, 399.00, 199.00, 2, 1, 200, '请描述你的游戏开发经验', 0, 1, 1, 0, 1, 67, 45, 1345, 134, 18, 20033.00, 70, 4.3, '2024-01-12 14:15:00', 72, 0, 0, '{"theme": "game", "level": "intermediate"}', 'Unity游戏开发学习营', '1. 分享项目请注明版权\n2. 鼓励原创作品展示', 1008),
(9, 'INDIE_GAME_STUDIO', '独立游戏工作室', '独立游戏开发者交流平台，分享创意、技术和商业化经验。', 'https://example.com/avatars/indie_game.jpg', 'https://example.com/covers/indie_game_cover.jpg', 1009, 2, '独立游戏,创意设计,商业化', 1, 0.00, 0.00, 0.00, 1, 1, 0, NULL, 1, 1, 0, 0, 0, 123, 78, 1890, 189, 25, 0.00, 68, 4.2, '2024-01-11 16:45:00', 70, 0, 0, '{"theme": "indie", "level": "all"}', '独立游戏开发者的家', '1. 支持原创游戏作品\n2. 分享开发心得', 1009),
(10, 'ML_ALGORITHM_2024', '机器学习算法研究院', '深入研究机器学习算法原理与实现，适合有一定数学基础的开发者。', 'https://example.com/avatars/ml_algo.jpg', 'https://example.com/covers/ml_algo_cover.jpg', 1010, 3, '机器学习,算法,数学,Python', 2, 399.00, 599.00, 299.00, 3, 1, 150, NULL, 1, 1, 1, 0, 1, 45, 34, 987, 98, 12, 17955.00, 65, 4.7, '2024-01-10 11:30:00', 68, 1, 0, '{"theme": "ai", "level": "advanced"}', '深度学习算法研究', '1. 需要一定数学基础\n2. 鼓励论文分享讨论', 1010),
(11, 'CV_VISION_LAB', '计算机视觉实验室', '计算机视觉技术交流，包括图像处理、目标检测、人脸识别等应用。', 'https://example.com/avatars/cv_lab.jpg', 'https://example.com/covers/cv_lab_cover.jpg', 1011, 3, '计算机视觉,OpenCV,深度学习', 2, 299.00, 399.00, 199.00, 1, 1, 200, NULL, 1, 1, 0, 0, 1, 78, 56, 1456, 145, 19, 23322.00, 73, 4.5, '2024-01-09 13:20:00', 75, 0, 0, '{"theme": "cv", "level": "intermediate"}', '计算机视觉技术分享', '1. 欢迎项目实战分享\n2. 提供数据集资源', 1011),
(12, 'PHOTOGRAPHY_MASTER', '摄影大师工坊', '专业摄影技术交流社区，包括人像、风光、商业摄影等各类摄影技巧分享。', 'https://example.com/avatars/photography.jpg', 'https://example.com/covers/photography_cover.jpg', 1012, 4, '摄影技巧,后期处理,器材评测', 2, 199.00, 299.00, 149.00, 1, 1, 500, NULL, 1, 1, 0, 0, 1, 234, 156, 3456, 345, 45, 46566.00, 88, 4.6, '2024-01-08 14:30:00', 85, 1, 0, '{"theme": "photo", "level": "all"}', '摄影爱好者的聚集地', '1. 分享作品请注明拍摄参数\n2. 尊重版权，禁止盗图', 1012),
(13, 'VIDEO_CREATOR_HUB', '视频创作者中心', '视频制作技术分享，包括拍摄、剪辑、调色、特效等全流程制作技巧。', 'https://example.com/avatars/video.jpg', 'https://example.com/covers/video_cover.jpg', 1013, 4, '视频制作,剪辑技巧,调色', 1, 0.00, 0.00, 0.00, 1, 1, 0, NULL, 1, 1, 0, 1, 0, 189, 123, 2567, 256, 38, 0.00, 85, 4.4, '2024-01-07 16:20:00', 82, 0, 0, '{"theme": "video", "level": "all"}', '视频创作者交流平台', '1. 欢迎原创作品分享\n2. 提供素材资源交流', 1013),
(14, 'INTERNET_TRENDS_2024', '互联网趋势观察', '关注互联网行业最新动态，分析商业模式、技术趋势和市场变化。', 'https://example.com/avatars/internet.jpg', 'https://example.com/covers/internet_cover.jpg', 1014, 5, '互联网趋势,商业模式,市场分析', 2, 99.00, 199.00, 79.00, 1, 1, 1000, NULL, 1, 1, 0, 0, 1, 456, 234, 5678, 567, 78, 45144.00, 92, 4.7, '2024-01-06 10:15:00', 90, 1, 0, '{"theme": "internet", "level": "all"}', '把握互联网脉搏', '1. 分享行业见解\n2. 理性讨论，避免恶意竞争', 1014),
(15, 'STARTUP_FOUNDERS', '创业者联盟', '创业者交流平台，分享创业经验、融资技巧、团队管理等实战经验。', 'https://example.com/avatars/startup.jpg', 'https://example.com/covers/startup_cover.jpg', 1015, 5, '创业经验,融资,团队管理', 2, 299.00, 499.00, 199.00, 2, 1, 300, '请简述你的创业经历或创业想法', 0, 1, 1, 0, 1, 123, 89, 2345, 234, 29, 36777.00, 78, 4.5, '2024-01-05 12:45:00', 80, 0, 0, '{"theme": "startup", "level": "professional"}', '创业路上，我们同行', '1. 真实分享创业经历\n2. 保护商业机密', 1015),
(16, 'UI_DESIGN_ACADEMY', 'UI设计学院', '专业UI/UX设计师交流社区，分享设计理念、工具使用、作品点评等内容。', 'https://example.com/avatars/ui_design.jpg', 'https://example.com/covers/ui_design_cover.jpg', 1016, 6, 'UI设计,UX设计,Figma,Sketch', 2, 199.00, 299.00, 149.00, 1, 1, 400, NULL, 1, 1, 0, 0, 1, 178, 123, 2890, 289, 34, 35422.00, 82, 4.5, '2024-01-04 11:30:00', 85, 0, 0, '{"theme": "design", "level": "intermediate"}', 'UI设计师的成长之路', '1. 作品分享请注明设计思路\n2. 互相学习，共同进步', 1016),
(17, 'GRAPHIC_DESIGN_STUDIO', '平面设计工作室', '平面设计师聚集地，包括海报设计、品牌设计、包装设计等各类平面设计作品分享。', 'https://example.com/avatars/graphic.jpg', 'https://example.com/covers/graphic_cover.jpg', 1017, 6, '平面设计,品牌设计,包装设计', 1, 0.00, 0.00, 0.00, 1, 1, 0, NULL, 1, 1, 0, 1, 0, 234, 167, 3234, 323, 45, 0.00, 86, 4.4, '2024-01-03 14:20:00', 88, 0, 0, '{"theme": "graphic", "level": "all"}', '创意无限，设计有界', '1. 尊重原创设计\n2. 提供建设性反馈', 1017),
(18, 'SIDE_HUSTLE_MASTERS', '副业赚钱大师班', '分享各种副业赚钱方法，包括自媒体、电商、技能变现等多种赚钱途径。', 'https://example.com/avatars/sidehustle.jpg', 'https://example.com/covers/sidehustle_cover.jpg', 1018, 7, '副业赚钱,自媒体,电商,技能变现', 2, 99.00, 199.00, 69.00, 1, 1, 800, NULL, 1, 1, 0, 0, 1, 567, 345, 6789, 678, 89, 56133.00, 95, 4.6, '2024-01-02 09:15:00', 92, 1, 0, '{"theme": "money", "level": "all"}', '副业改变生活，努力创造财富', '1. 分享真实收益数据\n2. 禁止发布违法项目', 1018),
(19, 'FREELANCER_COMMUNITY', '自由职业者社区', '自由职业者交流平台，分享接单技巧、客户管理、时间管理等自由职业经验。', 'https://example.com/avatars/freelancer.jpg', 'https://example.com/covers/freelancer_cover.jpg', 1019, 7, '自由职业,接单技巧,时间管理', 1, 0.00, 0.00, 0.00, 1, 1, 0, NULL, 1, 1, 0, 1, 0, 289, 178, 3567, 356, 42, 0.00, 83, 4.3, '2024-01-01 16:45:00', 85, 0, 0, '{"theme": "freelance", "level": "all"}', '自由职业，自由生活', '1. 分享真实工作经验\n2. 互助合作，共同成长', 1019),
(20, 'CYBERSECURITY_EXPERTS', '网络安全专家圈', '信息安全专业人士交流平台，分享安全技术、漏洞分析、防护策略等内容。', 'https://example.com/avatars/security.jpg', 'https://example.com/covers/security_cover.jpg', 1020, 8, '网络安全,漏洞分析,防护策略,渗透测试', 2, 399.00, 599.00, 299.00, 2, 1, 200, '请提供相关安全认证或工作经验', 0, 1, 1, 0, 1, 123, 89, 2345, 234, 29, 49077.00, 78, 4.7, '2023-12-31 14:30:00', 80, 1, 0, '{"theme": "security", "level": "professional"}', '守护网络安全，共建安全生态', '1. 禁止分享恶意代码\n2. 遵守法律法规', 1020),
(21, 'ETHICAL_HACKING_LAB', '白帽黑客实验室', '白帽黑客和渗透测试工程师交流社区，分享渗透测试技术、安全工具使用等。', 'https://example.com/avatars/ethical_hacking.jpg', 'https://example.com/covers/ethical_hacking_cover.jpg', 1021, 8, '白帽黑客,渗透测试,安全工具,漏洞挖掘', 2, 299.00, 399.00, 199.00, 2, 1, 150, '请简述你的安全技术背景', 0, 1, 1, 0, 1, 89, 67, 1890, 189, 23, 26611.00, 75, 4.5, '2023-12-30 11:20:00', 78, 0, 0, '{"theme": "hacking", "level": "intermediate"}', '以攻促防，提升安全意识', '1. 仅限合法渗透测试\n2. 保护客户隐私信息', 1021),
(22, 'FITNESS_TRANSFORMATION', '健身蜕变营', '健身爱好者和教练交流平台，分享训练计划、营养搭配、健身心得等内容。', 'https://example.com/avatars/fitness.jpg', 'https://example.com/covers/fitness_cover.jpg', 1022, 9, '健身训练,营养搭配,体型塑造,运动康复', 2, 199.00, 299.00, 149.00, 1, 1, 600, NULL, 1, 1, 0, 0, 1, 345, 234, 4567, 456, 67, 68655.00, 89, 4.6, '2023-12-29 08:30:00', 90, 1, 0, '{"theme": "fitness", "level": "all"}', '健康生活，从健身开始', '1. 分享科学训练方法\n2. 避免极端减肥建议', 1022),
(23, 'NUTRITION_WELLNESS', '营养健康管家', '营养师和健康管理师社区，分享营养知识、健康饮食、疾病预防等专业内容。', 'https://example.com/avatars/nutrition.jpg', 'https://example.com/covers/nutrition_cover.jpg', 1023, 9, '营养学,健康饮食,疾病预防,养生保健', 2, 299.00, 399.00, 199.00, 2, 1, 400, '请提供相关营养或医学背景', 0, 1, 1, 0, 1, 234, 156, 3456, 345, 48, 69666.00, 86, 4.7, '2023-12-28 10:45:00', 88, 0, 1, '{"theme": "nutrition", "level": "professional"}', '科学营养，健康生活', '1. 提供专业营养建议\n2. 不得替代医疗诊断', 1023),
(24, 'ENGLISH_MASTERY', '英语精通之路', '英语学习者交流社区，分享学习方法、口语练习、考试技巧等英语学习内容。', 'https://example.com/avatars/english.jpg', 'https://example.com/covers/english_cover.jpg', 1024, 10, '英语学习,口语练习,考试技巧,语法解析', 2, 199.00, 299.00, 149.00, 1, 1, 800, NULL, 1, 1, 0, 0, 1, 456, 289, 5678, 567, 78, 86533.00, 91, 4.5, '2023-12-27 14:20:00', 93, 1, 0, '{"theme": "english", "level": "all"}', 'Master English, Master the World', '1. 鼓励英语交流\n2. 互相帮助纠错', 1024),
(25, 'POLYGLOT_COMMUNITY', '多语言学习社区', '多语言学习爱好者聚集地，分享各种语言学习经验、文化交流、语言交换等。', 'https://example.com/avatars/polyglot.jpg', 'https://example.com/covers/polyglot_cover.jpg', 1025, 10, '多语言学习,文化交流,语言交换,国际交流', 1, 0.00, 0.00, 0.00, 1, 1, 0, NULL, 1, 1, 0, 1, 0, 378, 245, 4234, 423, 56, 0.00, 87, 4.4, '2023-12-26 16:30:00', 89, 0, 0, '{"theme": "languages", "level": "all"}', '语言是沟通世界的桥梁', '1. 尊重不同文化\n2. 积极参与语言交换', 1025),
(26, 'MUSIC_PRODUCTION_STUDIO', '音乐制作工作室', '音乐制作人和音频工程师交流平台，分享音乐制作技巧、混音技术、音频处理等内容。', 'https://example.com/avatars/music_production.jpg', 'https://example.com/covers/music_production_cover.jpg', 1026, 11, '音乐制作,混音技术,音频处理,编曲创作', 2, 299.00, 399.00, 199.00, 1, 1, 300, NULL, 1, 1, 0, 0, 1, 189, 123, 2890, 289, 42, 56511.00, 84, 4.6, '2023-12-25 19:30:00', 86, 0, 0, '{"theme": "music", "level": "intermediate"}', '音乐是灵魂的语言', '1. 尊重原创作品\n2. 分享制作技巧和经验', 1026),
(27, 'INDIE_MUSIC_COLLECTIVE', '独立音乐集体', '独立音乐人和音乐爱好者社区，分享原创音乐、演出经验、音乐推广等内容。', 'https://example.com/avatars/indie_music.jpg', 'https://example.com/covers/indie_music_cover.jpg', 1027, 11, '独立音乐,原创作品,演出经验,音乐推广', 1, 0.00, 0.00, 0.00, 1, 1, 0, NULL, 1, 1, 0, 1, 0, 234, 156, 3234, 323, 45, 0.00, 86, 4.4, '2023-12-24 17:45:00', 88, 0, 0, '{"theme": "indie_music", "level": "all"}', '独立音乐，自由表达', '1. 支持原创音乐\n2. 鼓励音乐人交流合作', 1027),
(28, 'TRAVEL_EXPLORERS', '旅行探险家', '旅行爱好者和探险家交流平台，分享旅行攻略、景点推荐、摄影作品、旅行心得等内容。', 'https://example.com/avatars/travel.jpg', 'https://example.com/covers/travel_cover.jpg', 1028, 12, '旅行攻略,景点推荐,旅行摄影,文化体验', 2, 199.00, 299.00, 149.00, 1, 1, 600, NULL, 1, 1, 0, 0, 1, 345, 234, 4567, 456, 67, 68655.00, 89, 4.5, '2023-12-23 14:20:00', 91, 1, 0, '{"theme": "travel", "level": "all"}', '世界那么大，我想去看看', '1. 分享真实旅行经历\n2. 尊重当地文化习俗', 1028),
(29, 'OUTDOOR_ADVENTURE', '户外探险俱乐部', '户外运动和探险爱好者社区，分享登山、徒步、野营、极限运动等户外经验。', 'https://example.com/avatars/outdoor.jpg', 'https://example.com/covers/outdoor_cover.jpg', 1029, 12, '户外探险,登山徒步,野营装备,极限运动', 1, 0.00, 0.00, 0.00, 1, 1, 0, NULL, 1, 1, 0, 0, 0, 278, 167, 3234, 323, 48, 0.00, 86, 4.4, '2023-12-22 10:30:00', 88, 0, 0, '{"theme": "outdoor", "level": "all"}', '在大自然中找到真正的自己', '1. 安全第一，做好防护\n2. 保护环境，不留痕迹', 1029),
(30, 'YOGA_MEDITATION', '瑜伽冥想空间', '瑜伽和冥想练习者的静心之地，分享体式技巧、冥想方法、身心健康理念。', 'https://example.com/avatars/yoga.jpg', 'https://example.com/covers/yoga_cover.jpg', 1022, 9, '瑜伽,冥想,身心健康,正念', 2, 99.00, 199.00, 79.00, 1, 1, 500, NULL, 1, 1, 0, 0, 1, 234, 156, 3234, 323, 45, 23166.00, 85, 4.6, '2024-01-15 19:20:00', 85, 0, 0, '{"theme": "yoga", "level": "all"}', '身心合一，内外兼修', '1. 保持练习的纯净性\n2. 分享正能量内容', 1022),
(31, 'ENGLISH_FLUENCY', '英语流利说', '英语学习者的交流平台，提供口语练习、语法讲解、文化交流等全方位英语学习支持。', 'https://example.com/avatars/english.jpg', 'https://example.com/covers/english_cover.jpg', 1023, 10, '英语学习,口语练习,语法,文化交流', 2, 199.00, 299.00, 149.00, 1, 1, 600, NULL, 1, 1, 0, 1, 1, 456, 289, 5678, 567, 78, 90744.00, 92, 4.7, '2024-01-14 16:45:00', 90, 1, 0, '{"theme": "language", "level": "all"}', 'Practice makes perfect!', '1. 鼓励用英语交流\n2. 互相纠错，共同进步', 1023),
(32, 'JAPANESE_CULTURE', '日语文化研习社', '日语学习和日本文化爱好者社区，从五十音到高级日语，从动漫到传统文化。', 'https://example.com/avatars/japanese.jpg', 'https://example.com/covers/japanese_cover.jpg', 1024, 10, '日语学习,日本文化,动漫,传统文化', 2, 159.00, 259.00, 119.00, 1, 1, 400, NULL, 1, 1, 0, 0, 1, 289, 178, 3567, 356, 52, 45951.00, 86, 4.4, '2024-01-13 14:30:00', 85, 0, 0, '{"theme": "japanese", "level": "all"}', 'こんにちは！日语学习之旅开始了', '1. 尊重日本文化\n2. 积极参与语言交换', 1024),
(33, 'MUSIC_PRODUCTION', '音乐制作工坊', '音乐制作人和创作者的聚集地，分享编曲技巧、混音经验、创作灵感等音乐制作内容。', 'https://example.com/avatars/music.jpg', 'https://example.com/covers/music_cover.jpg', 1025, 11, '音乐制作,编曲,混音,创作', 2, 299.00, 399.00, 199.00, 2, 1, 300, '请简述你的音乐制作经验', 0, 1, 1, 0, 1, 123, 89, 2345, 234, 34, 36777.00, 82, 4.5, '2024-01-12 20:15:00', 82, 0, 0, '{"theme": "music", "level": "intermediate"}', '用音乐表达内心的声音', '1. 尊重原创音乐作品\n2. 提供建设性的音乐反馈', 1025),
(34, 'INDIE_MUSICIANS', '独立音乐人联盟', '独立音乐人交流平台，分享原创作品、演出经验、音乐推广等独立音乐圈的故事。', 'https://example.com/avatars/indie_music.jpg', 'https://example.com/covers/indie_music_cover.jpg', 1026, 11, '独立音乐,原创,演出,推广', 1, 0.00, 0.00, 0.00, 1, 1, 0, NULL, 1, 1, 0, 1, 0, 178, 123, 2890, 289, 41, 0.00, 79, 4.3, '2024-01-11 18:45:00', 80, 0, 0, '{"theme": "indie_music", "level": "all"}', '独立音乐，独特声音', '1. 支持原创音乐创作\n2. 分享真实音乐经历', 1026),
(35, 'TRAVEL_ADVENTURERS', '环球旅行者', '旅行爱好者的聚集地，分享旅行攻略、美食推荐、文化体验、摄影技巧等旅行相关内容。', 'https://example.com/avatars/travel.jpg', 'https://example.com/covers/travel_cover.jpg', 1027, 12, '旅行攻略,美食推荐,文化体验,旅行摄影', 2, 199.00, 299.00, 149.00, 1, 1, 800, NULL, 1, 1, 0, 0, 1, 567, 345, 6789, 678, 89, 112833.00, 94, 4.6, '2024-01-10 09:30:00', 92, 1, 0, '{"theme": "travel", "level": "all"}', '世界那么大，一起去看看', '1. 分享真实旅行体验\n2. 提供实用旅行建议', 1027),
(36, 'OUTDOOR_EXPLORERS', '户外探险队', '户外运动和探险爱好者社区，包括登山、徒步、露营、攀岩等各类户外活动分享。', 'https://example.com/avatars/outdoor.jpg', 'https://example.com/covers/outdoor_cover.jpg', 1028, 12, '户外运动,登山,徒步,露营,攀岩', 1, 0.00, 0.00, 0.00, 1, 1, 0, NULL, 1, 1, 0, 0, 0, 234, 156, 3456, 345, 52, 0.00, 87, 4.4, '2024-01-09 15:20:00', 85, 0, 0, '{"theme": "outdoor", "level": "all"}', '探索自然，挑战自我', '1. 注意户外安全\n2. 保护自然环境', 1028),
(37, 'SCIENCE_POPULARIZATION', '科学普及站', '科学知识普及平台，用通俗易懂的方式分享物理、化学、生物、天文等各领域科学知识。', 'https://example.com/avatars/science.jpg', 'https://example.com/covers/science_cover.jpg', 1029, 13, '科学普及,物理,化学,生物,天文', 2, 99.00, 199.00, 79.00, 1, 1, 1000, NULL, 1, 1, 0, 0, 1, 456, 289, 5678, 567, 78, 45144.00, 91, 4.7, '2024-01-08 11:45:00', 90, 1, 0, '{"theme": "science", "level": "all"}', '科学改变世界，知识点亮未来', '1. 保证科学内容的准确性\n2. 用简单语言解释复杂概念', 1029),
(38, 'STEM_EDUCATION', 'STEM教育研究院', 'STEM教育工作者和家长交流平台，分享教学方法、学习资源、教育理念等STEM教育内容。', 'https://example.com/avatars/stem.jpg', 'https://example.com/covers/stem_cover.jpg', 1030, 13, 'STEM教育,教学方法,学习资源,教育理念', 2, 299.00, 399.00, 199.00, 2, 1, 400, '请简述你的教育背景或教学经验', 0, 1, 1, 0, 1, 189, 123, 2890, 289, 42, 56511.00, 84, 4.5, '2024-01-07 14:30:00', 85, 0, 0, '{"theme": "education", "level": "professional"}', '培养未来的科技创新人才', '1. 分享实用教学经验\n2. 关注学生全面发展', 1030),
(39, 'PSYCHOLOGY_GROWTH', '心理成长花园', '心理健康和个人成长社区，分享心理学知识、情绪管理、人际关系、自我提升等内容。', 'https://example.com/avatars/psychology.jpg', 'https://example.com/covers/psychology_cover.jpg', 1031, 14, '心理健康,情绪管理,人际关系,自我提升', 2, 199.00, 299.00, 149.00, 1, 1, 600, NULL, 1, 1, 0, 1, 1, 345, 234, 4567, 456, 67, 68655.00, 88, 4.6, '2024-01-06 16:20:00', 88, 0, 0, '{"theme": "psychology", "level": "all"}', '了解自己，成就更好的自己', '1. 保护个人隐私\n2. 提供专业心理建议', 1031),
(40, 'MINDFULNESS_PRACTICE', '正念冥想练习', '正念和冥想练习社区，帮助大家减压放松、提升专注力、培养内心平静。', 'https://example.com/avatars/mindfulness.jpg', 'https://example.com/covers/mindfulness_cover.jpg', 1032, 14, '正念,冥想,减压,专注力,内心平静', 1, 0.00, 0.00, 0.00, 1, 1, 0, NULL, 1, 1, 0, 0, 0, 278, 167, 3234, 323, 48, 0.00, 86, 4.4, '2024-01-05 19:45:00', 85, 0, 0, '{"theme": "mindfulness", "level": "all"}', '当下即是力量，正念改变生活', '1. 营造安静祥和的氛围\n2. 分享正能量内容', 1032),
(41, 'LITERATURE_SALON', '文学沙龙', '文学爱好者和创作者的交流平台，分享原创作品、文学评论、写作技巧等文学内容。', 'https://example.com/avatars/literature.jpg', 'https://example.com/covers/literature_cover.jpg', 1033, 15, '文学创作,原创作品,文学评论,写作技巧', 2, 199.00, 299.00, 149.00, 1, 1, 500, NULL, 1, 1, 0, 1, 1, 289, 189, 3789, 378, 56, 57711.00, 87, 4.5, '2024-01-04 14:20:00', 87, 0, 0, '{"theme": "literature", "level": "all"}', '文字是心灵的窗口', '1. 尊重原创作品\n2. 提供建设性文学反馈', 1033),
(42, 'ANCIENT_POETRY', '古诗词鉴赏', '中华古诗词爱好者社区，分享古诗词鉴赏、书法作品、传统文化知识等内容。', 'https://example.com/avatars/poetry.jpg', 'https://example.com/covers/poetry_cover.jpg', 1034, 15, '古诗词,书法,传统文化,文化传承', 1, 0.00, 0.00, 0.00, 1, 1, 0, NULL, 1, 1, 0, 0, 0, 234, 156, 3234, 323, 42, 0.00, 84, 4.4, '2024-01-03 16:45:00', 85, 0, 0, '{"theme": "poetry", "level": "all"}', '诗意人生，传承文化', '1. 传承中华优秀文化\n2. 鼓励原创诗词作品', 1034),
(43, 'MARKETING_MASTERS', '营销大师课堂', '营销专业人士交流平台，分享营销策略、品牌建设、市场推广、销售技巧等内容。', 'https://example.com/avatars/marketing.jpg', 'https://example.com/covers/marketing_cover.jpg', 1035, 16, '营销策略,品牌建设,市场推广,销售技巧', 2, 299.00, 399.00, 199.00, 2, 1, 400, '请简述你的营销或销售经验', 0, 1, 1, 0, 1, 234, 156, 3456, 345, 48, 69666.00, 86, 4.6, '2024-01-02 11:30:00', 88, 0, 0, '{"theme": "marketing", "level": "professional"}', '营销改变世界，创意驱动增长', '1. 分享实战营销经验\n2. 提供有价值的市场洞察', 1035),
(44, 'SALES_CHAMPIONS', '销售冠军俱乐部', '销售精英和创业者社区，分享销售技巧、客户开发、谈判策略、业绩提升等内容。', 'https://example.com/avatars/sales.jpg', 'https://example.com/covers/sales_cover.jpg', 1036, 16, '销售技巧,客户开发,谈判策略,业绩提升', 2, 199.00, 299.00, 149.00, 1, 1, 600, NULL, 1, 1, 0, 0, 1, 345, 234, 4567, 456, 67, 68655.00, 89, 4.5, '2024-01-01 09:15:00', 90, 0, 0, '{"theme": "sales", "level": "intermediate"}', '销售是一门艺术，成交是最好的回报', '1. 分享真实销售案例\n2. 互相学习，共同成长', 1036),
(45, 'CROSS_BORDER_ECOMMERCE', '跨境电商大本营', '跨境电商从业者交流平台，分享平台运营、产品选品、物流仓储、市场分析等内容。', 'https://example.com/avatars/ecommerce.jpg', 'https://example.com/covers/ecommerce_cover.jpg', 1037, 17, '跨境电商,平台运营,产品选品,物流仓储', 2, 399.00, 599.00, 299.00, 2, 1, 500, '请简述你的电商经验或兴趣方向', 0, 1, 1, 0, 1, 456, 289, 5678, 567, 78, 182244.00, 93, 4.7, '2023-12-30 10:20:00', 95, 1, 0, '{"theme": "ecommerce", "level": "professional"}', '全球市场，无限机会', '1. 分享实战运营经验\n2. 提供最新市场资讯', 1037),
(46, 'AMAZON_SELLERS', '亚马逊卖家联盟', '亚马逊平台卖家交流社区，分享店铺运营、广告投放、客户服务、政策解读等内容。', 'https://example.com/avatars/amazon.jpg', 'https://example.com/covers/amazon_cover.jpg', 1038, 17, '亚马逊,店铺运营,广告投放,客户服务', 2, 299.00, 399.00, 199.00, 1, 1, 400, NULL, 1, 1, 0, 0, 1, 289, 178, 3567, 356, 52, 86533.00, 88, 4.4, '2023-12-29 14:45:00', 90, 0, 0, '{"theme": "amazon", "level": "intermediate"}', '亚马逊创业，成就梦想', '1. 分享实用运营技巧\n2. 及时更新平台政策', 1038),
(47, 'ESPORTS_ARENA', '电竞竞技场', '电子竞技爱好者和职业选手交流平台，分享游戏技巧、赛事分析、战队管理、电竞资讯等内容。', 'https://example.com/avatars/esports.jpg', 'https://example.com/covers/esports_cover.jpg', 1039, 18, '电子竞技,游戏技巧,赛事分析,战队管理', 1, 0.00, 0.00, 0.00, 1, 1, 0, NULL, 1, 1, 0, 1, 0, 567, 345, 6789, 678, 89, 0.00, 92, 4.6, '2023-12-28 20:30:00', 94, 1, 0, '{"theme": "esports", "level": "all"}', '竞技精神，永不言败', '1. 公平竞技，拒绝作弊\n2. 尊重对手，友好交流', 1039),
(48, 'MOBILE_GAMING', '手游大神殿', '手机游戏玩家社区，分享手游攻略、新游推荐、游戏评测、充值优惠等内容。', 'https://example.com/avatars/mobile_game.jpg', 'https://example.com/covers/mobile_game_cover.jpg', 1040, 18, '手机游戏,游戏攻略,新游推荐,游戏评测', 1, 0.00, 0.00, 0.00, 1, 1, 0, NULL, 1, 1, 0, 0, 0, 456, 289, 5678, 567, 78, 0.00, 89, 4.5, '2023-12-27 18:45:00', 91, 0, 0, '{"theme": "mobile_game", "level": "all"}', '指尖上的精彩世界', '1. 分享真实游戏体验\n2. 理性消费，适度游戏', 1040),
(49, 'TECH_INDUSTRY', '科技行业观察', '科技行业从业者交流平台，分享行业动态、技术趋势、职场经验、创业故事等内容。', 'https://example.com/avatars/tech_industry.jpg', 'https://example.com/covers/tech_industry_cover.jpg', 1041, 19, '科技行业,行业动态,技术趋势,职场经验', 2, 399.00, 599.00, 299.00, 2, 1, 300, '请简述你的行业背景或关注领域', 0, 1, 1, 1, 1, 234, 156, 3456, 345, 48, 93244.00, 91, 4.7, '2023-12-26 15:20:00', 93, 1, 1, '{"theme": "tech_industry", "level": "professional"}', '洞察科技前沿，把握行业脉搏', '1. 分享深度行业见解\n2. 保护商业机密信息', 1041),
(50, 'FINANCE_CIRCLE', '金融圈内参', '金融行业专业人士交流社区，分享市场分析、投资策略、风险管理、政策解读等内容。', 'https://example.com/avatars/finance.jpg', 'https://example.com/covers/finance_cover.jpg', 1042, 19, '金融行业,市场分析,投资策略,风险管理', 2, 599.00, 899.00, 399.00, 2, 1, 200, '请提供相关金融从业资格证明', 0, 1, 1, 1, 1, 189, 123, 2890, 289, 42, 113211.00, 94, 4.8, '2023-12-25 11:10:00', 96, 1, 1, '{"theme": "finance", "level": "expert"}', '专业金融，理性投资', '1. 提供专业金融建议\n2. 严禁内幕交易信息', 1042),
(51, 'BOOK_LOVERS', '书友会', '读书爱好者交流社区，分享读书心得、好书推荐、阅读方法、书评讨论等内容。', 'https://example.com/avatars/books.jpg', 'https://example.com/covers/books_cover.jpg', 1043, 20, '读书心得,好书推荐,阅读方法,书评讨论', 1, 0.00, 0.00, 0.00, 1, 1, 0, NULL, 1, 1, 0, 1, 0, 678, 456, 7890, 789, 98, 0.00, 93, 4.6, '2023-12-24 16:30:00', 95, 1, 0, '{"theme": "books", "level": "all"}', '书中自有黄金屋，阅读改变人生', '1. 尊重版权，支持正版\n2. 理性讨论，包容不同观点', 1043),
(52, 'KNOWLEDGE_SHARING', '知识分享站', '知识分享和学习交流平台，涵盖各领域知识分享、学习方法、思维训练、认知提升等内容。', 'https://example.com/avatars/knowledge.jpg', 'https://example.com/covers/knowledge_cover.jpg', 1044, 20, '知识分享,学习方法,思维训练,认知提升', 2, 199.00, 299.00, 149.00, 1, 1, 800, NULL, 1, 1, 0, 1, 1, 567, 378, 6789, 678, 87, 112533.00, 95, 4.7, '2023-12-23 13:45:00', 97, 1, 0, '{"theme": "knowledge", "level": "all"}', '知识就是力量，分享创造价值', '1. 分享高质量内容\n2. 鼓励原创知识输出', 1044);

-- 插入星球标签关联数据
INSERT INTO `pla_planet_tag_relation` (`planet_id`, `tag_id`, `created_by`) VALUES 
(1, 1, 1001), (1, 2, 1001),
(2, 3, 1002),
(3, 4, 1003), (3, 5, 1003),
(4, 6, 1004), (4, 7, 1004),
(5, 9, 1005),
(6, 1, 1006), (7, 4, 1007), (8, 6, 1008),
(9, 6, 1009), (10, 3, 1010), (11, 3, 1011),
(12, 7, 1012), (13, 7, 1013), (14, 8, 1014),
(15, 9, 1015), (16, 7, 1016), (17, 7, 1017),
(18, 10, 1018), (19, 9, 1019), (20, 8, 1020),
(21, 9, 1021), (22, 9, 1022), (23, 9, 1023),
(24, 9, 1024), (25, 7, 1025), (26, 7, 1026),
(27, 8, 1027), (28, 9, 1028), (29, 8, 1029),
(30, 8, 1030), (31, 9, 1031), (32, 9, 1032),
(33, 7, 1033), (34, 7, 1034), (35, 8, 1035),
(36, 8, 1036), (37, 10, 1037), (38, 10, 1038),
(39, 3, 1039), (40, 3, 1040), (41, 8, 1041),
(42, 8, 1042), (43, 7, 1043), (44, 7, 1044),
(18, 8, 1018), (19, 8, 1019),
(22, 4, 1022), (23, 4, 1023),
(24, 7, 1024);

-- 插入星球成员数据
INSERT INTO `pla_planet_member` (`planet_id`, `user_id`, `member_type`, `join_time`, `expire_time`, `join_source`, `inviter_id`, `order_id`, `nickname`, `total_posts`, `total_likes`, `created_by`) VALUES
(1, 2001, 1, '2024-01-02 14:30:00', '2025-01-02 14:30:00', 1, NULL, 10001, 'Spring学徒', 8, 45, 2001),
(1, 2002, 1, '2024-01-03 09:15:00', '2025-01-03 09:15:00', 2, 1001, 10002, '微服务探索者', 12, 67, 2002),
(1, 2003, 2, '2024-01-04 16:45:00', '2025-01-04 16:45:00', 1, NULL, 10003, 'Java架构师', 20, 123, 2003),
(2, 1002, 3, '2024-01-01 11:00:00', NULL, 1, NULL, NULL, 'AI导师', 12, 78, 1002),
(2, 2004, 1, '2024-01-05 10:20:00', '2025-01-05 10:20:00', 1, NULL, 10004, 'ML新手', 5, 23, 2004),
(2, 2005, 1, '2024-01-06 15:30:00', NULL, 2, 1002, 10005, '数据科学家', 9, 56, 2005),
(3, 1003, 3, '2024-01-01 12:00:00', NULL, 1, NULL, NULL, '前端大神', 25, 156, 1003),
(3, 2006, 1, '2024-01-07 11:45:00', NULL, 1, NULL, NULL, 'Vue爱好者', 18, 89, 2006),
(3, 2007, 1, '2024-01-08 14:20:00', NULL, 3, 2006, NULL, 'React开发者', 22, 134, 2007),
(4, 1004, 3, '2024-01-01 13:00:00', NULL, 1, NULL, NULL, '设计总监', 8, 45, 1004),
(4, 2008, 1, '2024-01-09 09:30:00', '2025-01-09 09:30:00', 1, NULL, 10006, 'UI设计师', 6, 34, 2008),
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