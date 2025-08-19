package com.whoiszxl.constants;

/**
 * 帖子缓存常量
 *
 * @author whoiszxl
 */
public class PostCacheConstants {

    // ========================= 本地缓存键 =========================
    
    /**
     * 本地缓存 - 帖子列表
     */
    public static final String LOCAL_CACHE_POST_LIST = "post_list";

    // ========================= 分布式缓存键 =========================
    
    /**
     * 分布式缓存 - 帖子列表前缀
     */
    public static final String CACHE_POST_LIST_PREFIX = "cache:post:list:";

    // ========================= 分布式锁键 =========================
    
    /**
     * 分布式锁 - 从数据库获取帖子列表前缀
     */
    public static final String LOCK_GET_POST_LIST_FROM_DB_PREFIX = "lock:get_post_list_from_db:";

    // ========================= 布隆过滤器键 =========================
    
    /**
     * 布隆过滤器 - 星球ID
     */
    public static final String BLOOM_PLANET_ID = "bloom:planet:id";
}
