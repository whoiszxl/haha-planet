package com.whoiszxl.common.config.jackson;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.type.ClassKey;
import lombok.extern.slf4j.Slf4j;

/**
 * @author whoiszxl
 */
@Slf4j
public class SimpleDeserializersWrapper extends SimpleDeserializers {

    @Override
    public JsonDeserializer<?> findEnumDeserializer(Class<?> type,
                                                    DeserializationConfig config,
                                                    BeanDescription beanDesc) throws JsonMappingException {
        JsonDeserializer<?> deser = super.findEnumDeserializer(type, config, beanDesc);
        if (null != deser) {
            return deser;
        }

        // 重写增强：开始查找指定枚举类型的接口的反序列化器（例如：GenderEnum 枚举类型，则是找它的接口 BaseEnum 的反序列化器）
        for (Class<?> typeInterface : type.getInterfaces()) {
            deser = this._classMappings.get(new ClassKey(typeInterface));
            if (null != deser) {
                return deser;
            }
        }
        return null;
    }
}
