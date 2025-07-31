package com.whoiszxl.common.config.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.whoiszxl.starter.mybatis.base.IBaseEnum;

import java.io.IOException;

/**
 * 通用枚举接口 IBaseEnum 序列化器
 */
@JacksonStdImpl
public class BaseEnumSerializer extends JsonSerializer<IBaseEnum> {

    /**
     * 静态实例
     */
    public static final BaseEnumSerializer SERIALIZER_INSTANCE = new BaseEnumSerializer();

    @Override
    public void serialize(IBaseEnum value, JsonGenerator generator, SerializerProvider serializers) throws IOException {
        generator.writeObject(value.getValue());
    }
}
