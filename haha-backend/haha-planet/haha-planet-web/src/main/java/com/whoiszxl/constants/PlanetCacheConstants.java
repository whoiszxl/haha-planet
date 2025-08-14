package com.whoiszxl.constants;

/**
 * 星球缓存常量
 *
 * @author whoiszxl
 */
public class PlanetCacheConstants {

    /**
     * 星球分类列表缓存键
     */
    public static final String CACHE_PLANET_CATEGORY_LIST = "planet:category:list";

    /**
     * 星球列表缓存键前缀
     */
    public static final String CACHE_PLANET_LIST_PREFIX = "planet:list:";

    /**
     * 星球分类列表数据库锁键
     */
    public static final String LOCK_GET_PLANET_CATEGORY_LIST_FROM_DB = "lock:planet:category:list:db";

    /**
     * 星球列表数据库锁键前缀
     */
    public static final String LOCK_GET_PLANET_LIST_FROM_DB_PREFIX = "lock:planet:list:db:";

    /**
     * 星球分类布隆过滤器键
     */
    public static final String BLOOM_PLANET_CATEGORY = "bloom:planet:category";

    /**
     * 星球布隆过滤器键
     */
    public static final String BLOOM_PLANET_LIST = "bloom:planet:list";

    /**
     * 星球详情缓存键前缀
     */
    public static final String CACHE_PLANET_DETAIL_PREFIX = "planet:detail:";

    /**
     * 星球详情数据库锁键前缀
     */
    public static final String LOCK_GET_PLANET_DETAIL_FROM_DB_PREFIX = "lock:planet:detail:db:";

    /**
     * 星球ID布隆过滤器键
     */
    public static final String BLOOM_PLANET_ID = "bloom:planet:id";

    /**
     * 本地缓存名称 - 分类列表
     */
    public static final String LOCAL_CACHE_PLANET_CATEGORY_LIST = "planetCategoryList";

    /**
     * 本地缓存名称 - 星球列表
     */
    public static final String LOCAL_CACHE_PLANET_LIST = "planetList";

    /**
     * 本地缓存名称 - 星球详情
     */
    public static final String LOCAL_CACHE_PLANET_DETAIL = "planetDetail";
}
