-- liquibase formatted sql

-- changeset whoiszxl:1
-- comment 用户模块: 初始化模块表数据

-- 插入用户等级数据
INSERT INTO `uc_user_level` (`level`, `level_name`, `min_experience`, `max_experience`, `level_icon`, `level_color`, `privileges`, `description`, `created_by`) VALUES
(1, '新手', 0, 99, '/icons/level1.png', '#52c41a', '{"daily_post_limit": 5, "comment_limit": 20}', '刚刚加入的新用户', '1'),
(2, '青铜', 100, 499, '/icons/level2.png', '#fa8c16', '{"daily_post_limit": 10, "comment_limit": 50}', '有一定活跃度的用户', '1'),
(3, '白银', 500, 999, '/icons/level3.png', '#722ed1', '{"daily_post_limit": 15, "comment_limit": 100}', '活跃的社区成员', '1'),
(4, '黄金', 1000, 2499, '/icons/level4.png', '#1890ff', '{"daily_post_limit": 25, "comment_limit": 200}', '优质内容贡献者', '1'),
(5, '铂金', 2500, 4999, '/icons/level5.png', '#eb2f96', '{"daily_post_limit": 50, "comment_limit": 500}', '社区意见领袖', '1'),
(6, '钻石', 5000, 9999, '/icons/level6.png', '#f5222d', '{"daily_post_limit": 100, "comment_limit": 1000}', '哈哈星球达人', '1'),
(7, '王者', 10000, 99999999, '/icons/level7.png', '#faad14', '{"daily_post_limit": -1, "comment_limit": -1}', '社区顶级贡献者', '1');

-- 插入用户基础信息数据
INSERT INTO `uc_user_info` (`user_code`, `username`, `nickname`, `password`, `avatar`, `gender`, `birthday`, `phone`, `email`, `real_name`, `province`, `city`, `district`, `address`, `bio`, `profession`, `company`, `education`, `school`, `user_type`, `level`, `experience`, `points`, `balance`, `follower_count`, `following_count`, `post_count`, `like_count`, `last_login_time`, `last_login_ip`, `login_count`, `register_source`, `register_ip`, `is_verified`, `verified_time`, `created_by`) VALUES
('user_001', 'zhangsan', '张三', '{bcrypt}$2a$10$g9P9DxemFdIylwhkrp.m3.clkjgjiGDRcVeTSb.ZEwDk4aUWc3b8e', 'avatar/default_avatar.png', 1, '1990-05-15', '13800138001', 'zhangsan@example.com', '张三', '广东省', '深圳市', '南山区', '科技园南区', '热爱技术，专注于后端开发', '软件工程师', '腾讯科技', '本科', '清华大学', 2, 4, 1500, 2800, 1200.50, 156, 89, 45, 234, '2025-08-05 15:30:00', '192.168.1.100', 89, 'web', '192.168.1.100', 1, '2024-12-01 10:00:00', '1'),
('user_002', 'lisi', '李四', '{bcrypt}$2a$10$g9P9DxemFdIylwhkrp.m3.clkjgjiGDRcVeTSb.ZEwDk4aUWc3b8e', 'avatar/default_avatar.png', 2, '1992-08-20', '13800138002', 'lisi@example.com', '李四', '北京市', '北京市', '海淀区', '中关村软件园', '产品经理，关注用户体验', '产品经理', '字节跳动', '硕士', '北京大学', 1, 3, 750, 1200, 800.00, 89, 123, 28, 156, '2025-08-05 14:20:00', '192.168.1.101', 67, 'app', '192.168.1.101', 1, '2024-11-15 14:30:00', '1'),
('user_003', 'wangwu', '王五', '{bcrypt}$2a$10$g9P9DxemFdIylwhkrp.m3.clkjgjiGDRcVeTSb.ZEwDk4aUWc3b8e', 'avatar/default_avatar.png', 1, '1988-12-10', '13800138003', 'wangwu@example.com', '王五', '上海市', '上海市', '浦东新区', '陆家嘴金融区', 'UI/UX设计师，追求极致的视觉体验', 'UI设计师', '阿里巴巴', '本科', '上海交通大学', 3, 5, 3200, 5600, 2500.00, 234, 156, 67, 445, '2025-08-05 16:10:00', '192.168.1.102', 145, 'web', '192.168.1.102', 1, '2024-10-20 09:15:00', '1'),
('user_004', 'zhaoliu', '赵六', '{bcrypt}$2a$10$g9P9DxemFdIylwhkrp.m3.clkjgjiGDRcVeTSb.ZEwDk4aUWc3b8e', 'avatar/default_avatar.png', 2, '1995-03-25', '13800138004', 'zhaoliu@example.com', '赵六', '浙江省', '杭州市', '西湖区', '文三路互联网小镇', '前端开发工程师，Vue.js专家', '前端工程师', '网易', '本科', '浙江大学', 1, 2, 350, 680, 300.00, 45, 78, 15, 89, '2025-08-05 13:45:00', '192.168.1.103', 34, 'app', '192.168.1.103', 0, NULL, '1'),
('user_005', 'sunqi', '孙七', '{bcrypt}$2a$10$g9P9DxemFdIylwhkrp.m3.clkjgjiGDRcVeTSb.ZEwDk4aUWc3b8e', 'avatar/default_avatar.png', 1, '1987-11-08', '13800138005', 'sunqi@example.com', '孙七', '江苏省', '南京市', '鼓楼区', '新街口商业区', '资深架构师，微服务专家', '架构师', '华为技术', '硕士', '南京大学', 4, 6, 7800, 12500, 5000.00, 567, 234, 123, 890, '2025-08-05 12:30:00', '192.168.1.104', 234, 'web', '192.168.1.104', 1, '2024-09-10 16:20:00', '1');

-- 插入用户设置数据
INSERT INTO `uc_user_settings` (`user_id`, `privacy_level`, `allow_search`, `allow_friend_request`, `allow_message`, `email_notification`, `sms_notification`, `push_notification`, `theme`, `language`, `timezone`, `created_by`) VALUES
(1, 1, 1, 1, 1, 1, 1, 1, 'default', 'zh-CN', 'Asia/Shanghai', '1'),
(2, 2, 1, 1, 0, 1, 0, 1, 'dark', 'zh-CN', 'Asia/Shanghai', '1'),
(3, 1, 1, 1, 1, 1, 1, 1, 'light', 'zh-CN', 'Asia/Shanghai', '1'),
(4, 1, 1, 1, 1, 0, 1, 1, 'default', 'zh-CN', 'Asia/Shanghai', '1'),
(5, 3, 0, 0, 0, 1, 1, 0, 'dark', 'zh-CN', 'Asia/Shanghai', '1');

-- 插入用户关注关系数据(我关注的人)
INSERT INTO `uc_user_following` (`user_id`, `following_id`, `follow_type`, `is_mutual`, `created_by`) VALUES
(1, 2, 1, 0, '1'),
(1, 3, 2, 1, '1'),
(1, 5, 1, 0, '1'),
(2, 3, 1, 0, '1'),
(2, 4, 1, 1, '1'),
(3, 1, 1, 1, '1'),
(3, 5, 2, 0, '1'),
(4, 2, 1, 1, '1'),
(4, 5, 1, 0, '1'),
(5, 3, 1, 0, '1');

-- 插入用户粉丝关系数据(关注我的人)
INSERT INTO `uc_user_follower` (`user_id`, `follower_id`, `follow_type`, `is_mutual`, `created_by`) VALUES
(2, 1, 1, 0, '1'),
(3, 1, 2, 1, '1'),
(5, 1, 1, 0, '1'),
(3, 2, 1, 0, '1'),
(4, 2, 1, 1, '1'),
(1, 3, 1, 1, '1'),
(5, 3, 2, 0, '1'),
(2, 4, 1, 1, '1'),
(5, 4, 1, 0, '1'),
(3, 5, 1, 0, '1');

-- 插入用户令牌数据
INSERT INTO `uc_user_token` (`user_id`, `token`, `token_type`, `source`, `expires_time`, `login_ip`, `login_time`, `user_agent`, `created_by`) VALUES
(1, 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyXzAwMSIsImlhdCI6MTczMzM5NDYwMCwiZXhwIjoxNzMzNDgxMDAwfQ.abc123', 'access', 'web', '2025-08-06 16:56:49', '192.168.1.100', '2025-08-05 15:30:00', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36', '1'),
(2, 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyXzAwMiIsImlhdCI6MTczMzM5MTIwMCwiZXhwIjoxNzMzNDc3NjAwfQ.def456', 'access', 'app', '2025-08-06 15:20:00', '192.168.1.101', '2025-08-05 14:20:00', 'HahaPlanet/1.0.0 (iOS 17.0; iPhone14,2)', '1'),
(3, 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyXzAwMyIsImlhdCI6MTczMzM5NjIwMCwiZXhwIjoxNzMzNDgyNjAwfQ.ghi789', 'access', 'web', '2025-08-06 17:10:00', '192.168.1.102', '2025-08-05 16:10:00', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36', '1');

-- 插入用户客户端数据
INSERT INTO `uc_user_client` (`client_key`, `client_secret`, `auth_type`, `client_type`, `active_timeout`, `timeout`, `created_by`) VALUES
('web_client_001', 'web_secret_abc123def456ghi789', '["ACCOUNT", "PHONE", "EMAIL", "SOCIAL"]', 'web', -1, 2592000, '1'),
('app_client_001', 'app_secret_jkl012mno345pqr678', '["ACCOUNT", "PHONE", "EMAIL", "SOCIAL"]', 'mobile', -1, 2592000, '1'),
('api_client_001', 'api_secret_stu901vwx234yzab567', '["client_credentials"]', 'api', -1, -1, '1');

-- 插入积分记录数据
INSERT INTO `uc_points_record` (`user_id`, `points_change`, `points_before`, `points_after`, `change_type`, `change_reason`, `related_id`, `created_by`) VALUES
(1, 100, 0, 100, 'sign', '每日签到奖励', NULL, '1'),
(1, 50, 100, 150, 'post', '发布优质内容', 'post_001', '1'),
(1, 20, 150, 170, 'like', '内容获得点赞', 'post_001', '1'),
(1, 30, 170, 200, 'comment', '发表有价值评论', 'comment_001', '1'),
(2, 100, 0, 100, 'sign', '每日签到奖励', NULL, '1'),
(2, 50, 100, 150, 'post', '发布内容', 'post_002', '1'),
(3, 100, 0, 100, 'sign', '每日签到奖励', NULL, '1'),
(3, 200, 100, 300, 'post', '发布热门内容', 'post_003', '1'),
(3, 50, 300, 350, 'like', '内容获得大量点赞', 'post_003', '1'),
(4, 100, 0, 100, 'sign', '每日签到奖励', NULL, '1'),
(5, 100, 0, 100, 'sign', '每日签到奖励', NULL, '1'),
(5, 500, 100, 600, 'post', '发布专业技术文章', 'post_005', '1');

-- 更新用户统计数据(根据关注关系更新粉丝数和关注数)
UPDATE `uc_user_info` SET `follower_count` = (
    SELECT COUNT(*) FROM `uc_user_follower` WHERE `user_id` = `uc_user_info`.`id` AND `is_deleted` = 0
);

UPDATE `uc_user_info` SET `following_count` = (
    SELECT COUNT(*) FROM `uc_user_following` WHERE `user_id` = `uc_user_info`.`id` AND `is_deleted` = 0
);