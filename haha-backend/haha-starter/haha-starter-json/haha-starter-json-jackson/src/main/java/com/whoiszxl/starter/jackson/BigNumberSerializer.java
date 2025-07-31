package com.whoiszxl.starter.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

import java.io.IOException;

/**
 * JS 大数转换序列化
 * <p>
 * 检查数值是否在JavaScript的安全整数范围内。如果在范围内，使用默认的序列化方式；
 * 如果超出范围，则将数值转换为字符串进行序列化，确保在传输过程中不会因为精度问题而导致数值错误。
 */
@JacksonStdImpl
public class BigNumberSerializer extends NumberSerializer {

    public static final BigNumberSerializer INSTANCE = new BigNumberSerializer(Number.class);

    /**
     * 根据 JS 语言的 Number.MAX_SAFE_INTEGER 和 Number.MIN_SAFE_INTEGER 得来
     */
    private static final long MAX_SAFE_INTEGER = 9007199254740991L;
    private static final long MIN_SAFE_INTEGER = -9007199254740991L;

    public BigNumberSerializer(Class<? extends Number> rawType) {
        super(rawType);
    }

    @Override
    public void serialize(Number value, JsonGenerator g, SerializerProvider provider) throws IOException {
        // 如果在安全范围则使用默认序列化方式
        if (value.longValue() > MIN_SAFE_INTEGER && value.longValue() < MAX_SAFE_INTEGER) {
            super.serialize(value, g, provider);
        } else {
            // 否则将数字转换为字符串
            g.writeString(value.toString());
        }
    }
}
