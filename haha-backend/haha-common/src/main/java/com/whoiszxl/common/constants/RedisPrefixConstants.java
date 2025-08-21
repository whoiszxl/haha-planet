package com.whoiszxl.common.constants;

/**
 * @author whoiszxl
 */
public class RedisPrefixConstants {

    /**
     * 管理员端 Redis Key 前缀
     */
    public interface Admin {
        String ADMIN_CAPTCHA_IMAGE_KEY = "admin:captcha:image:";

        String ADMIN_LOGIN_KEY = "admin:login";

    }

    /**
     * C端 Redis Key 前缀
     */
    public interface User {
        String USER_CAPTCHA_IMAGE_KEY = "user:captcha:image:";

        String USER_CAPTCHA_EMAIL_KEY = "user:captcha:email:";

        String USER_CAPTCHA_PHONE_KEY = "user:captcha:phone:";

        String USER_LOGIN_KEY = "user:login";

        /**
         * 用户登录记录缓存前缀
         */
        String USER_LOGIN_RECORD_CACHE = "user:login:record:";

        /**
         * 用户登录记录缓存超时时间（秒）
         */
        Long USER_LOGIN_RECORD_CACHE_TIMEOUT = 86400L; // 24小时


    }



}
