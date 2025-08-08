package com.whoiszxl.starter.auth.justauth;

import com.whoiszxl.cache.redisson.util.RedissonUtil;
import com.whoiszxl.starter.core.constants.PropertiesConstants;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import me.zhyd.oauth.cache.AuthStateCache;
import org.redisson.client.RedisClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * JustAuth 自动配置
 * @author whoiszxl
 */
@RequiredArgsConstructor
@AutoConfiguration(before = com.xkcoding.justauth.autoconfigure.JustAuthAutoConfiguration.class)
@ConditionalOnProperty(prefix = "justauth", name = PropertiesConstants.ENABLED, havingValue = "true", matchIfMissing = true)
public class JustAuthAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(JustAuthAutoConfiguration.class);

    /**
     * State 缓存 Redis 实现（默认）
     */
    @Bean
    @ConditionalOnClass(RedissonUtil.class)
    @ConditionalOnProperty(prefix = "justauth.cache", name = "type", havingValue = "redis")
    public AuthStateCache authStateCache(RedissonUtil redissonUtil) {
        return new AuthStateCacheRedisDefaultImpl(redissonUtil);
    }

    @PostConstruct
    public void postConstruct() {
        log.debug("JustAuth完成初始化");
    }
}