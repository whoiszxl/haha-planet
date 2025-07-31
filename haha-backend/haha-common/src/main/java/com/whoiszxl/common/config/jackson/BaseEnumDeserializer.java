package com.whoiszxl.common.config.jackson;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.whoiszxl.starter.mybatis.base.IBaseEnum;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * 通用枚举接口 IBaseEnum 反序列化器
 * @author whoiszxl
 */
@JacksonStdImpl
public class BaseEnumDeserializer extends JsonDeserializer<IBaseEnum> {

    /**
     * 静态实例
     */
    public static final BaseEnumDeserializer SERIALIZER_INSTANCE = new BaseEnumDeserializer();

    @Override
    public IBaseEnum deserialize(JsonParser jsonParser,
                                 DeserializationContext deserializationContext) throws IOException {
        Class<?> targetClass = jsonParser.currentValue().getClass();
        String fieldName = jsonParser.currentName();
        String value = jsonParser.getText();
        return this.getEnum(targetClass, value, fieldName);
    }

    /**
     * 通过某字段对应值获取枚举实例，获取不到时为 {@code null}
     *
     * @param targetClass 目标类型
     * @param value       字段值
     * @param fieldName   字段名
     * @return 对应枚举实例 ，获取不到时为 {@code null}
     */
    private IBaseEnum getEnum(Class<?> targetClass, String value, String fieldName) {
        Field field = ReflectUtil.getField(targetClass, fieldName);
        Class<?> fieldTypeClass = field.getType();
        Object[] enumConstants = fieldTypeClass.getEnumConstants();
        for (Object enumConstant : enumConstants) {
            if (ClassUtil.isAssignable(IBaseEnum.class, fieldTypeClass)) {
                IBaseEnum baseEnum = (IBaseEnum)enumConstant;
                if (baseEnum.getValue().equals(Integer.valueOf(value))) {
                    return baseEnum;
                }
            }
        }
        return null;
    }
}
