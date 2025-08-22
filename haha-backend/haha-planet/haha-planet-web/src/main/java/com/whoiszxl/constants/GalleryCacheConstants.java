package com.whoiszxl.constants;

/**
 * 画廊缓存常量
 *
 * @author whoiszxl
 */
public class GalleryCacheConstants {

    // ========================= 本地缓存键 =========================
    
    /**
     * 本地缓存 - 画廊列表
     */
    public static final String LOCAL_CACHE_GALLERY_LIST = "gallery_list";

    // ========================= 分布式缓存键 =========================
    
    /**
     * 分布式缓存 - 画廊列表前缀
     */
    public static final String CACHE_GALLERY_LIST_PREFIX = "cache:gallery:list:";

    // ========================= 分布式锁键 =========================
    
    /**
     * 分布式锁 - 从数据库获取画廊列表前缀
     */
    public static final String LOCK_GET_GALLERY_LIST_FROM_DB_PREFIX = "lock:get_gallery_list_from_db:";

    // ========================= 布隆过滤器键 =========================
    
    /**
     * 布隆过滤器 - 画廊分类
     */
    public static final String BLOOM_GALLERY_CATEGORY = "bloom:gallery:category";
}