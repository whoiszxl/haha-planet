package com.whoiszxl.common.properties;

import cn.hutool.extra.spring.SpringUtil;

/**
 * rsa配置
 * @author whoiszxl
 */
public class RsaProperties {

    public static final String PRIVATE_KEY;

    public static final String PUBLIC_KEY;

    static {
        PRIVATE_KEY = SpringUtil.getProperty("security.crypto.private-key");
        PUBLIC_KEY = SpringUtil.getProperty("security.crypto.public-key");
    }
}
