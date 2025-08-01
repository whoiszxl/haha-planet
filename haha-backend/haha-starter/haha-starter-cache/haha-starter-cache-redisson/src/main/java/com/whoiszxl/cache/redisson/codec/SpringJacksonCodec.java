package com.whoiszxl.cache.redisson.codec;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.redisson.codec.JsonJacksonCodec;

/**
 * 使用Spring配置的ObjectMapper的Redisson编解码器
 * 解决LocalDateTime等Java 8时间类型序列化问题
 *
 * @author whoiszxl
 * @since 1.0.0
 */
public class SpringJacksonCodec extends JsonJacksonCodec {

    public SpringJacksonCodec(ObjectMapper objectMapper) {
        super(objectMapper);
    }
}