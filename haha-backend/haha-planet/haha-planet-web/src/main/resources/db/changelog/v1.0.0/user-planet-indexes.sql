-- 用户星球查询优化索引
-- 针对500万+星球规模的高效查询索引设计

-- 1. 星球表：查询用户创建的星球
-- 复合索引：owner_id + status + created_at，支持排序和过滤
CREATE INDEX idx_planet_owner_status_time ON pla_planet(owner_id, status, created_at DESC);

-- 2. 星球成员表：查询用户加入的星球
-- 复合索引：user_id + status + member_type + join_time，支持多维度查询
CREATE INDEX idx_member_user_status_type_time ON pla_planet_member(user_id, status, member_type, join_time DESC);

-- 3. 星球成员表：按星球查询成员（用于反向查询优化）
CREATE INDEX idx_member_planet_status_type ON pla_planet_member(planet_id, status, member_type);

-- 4. 星球表：按分类和状态查询（用于推荐算法）
CREATE INDEX idx_planet_category_status_hot ON pla_planet(category_id, status, hot_score DESC);

-- 5. 覆盖索引：用户星球基本信息查询（避免回表）
CREATE INDEX idx_planet_owner_cover ON pla_planet(owner_id, status, id, name, avatar, member_count, created_at);
