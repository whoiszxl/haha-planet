package com.whoiszxl.starter.core.utils;


import cn.hutool.core.text.CharSequenceUtil;

import java.util.function.Function;

public class MyStrUtil {

    private MyStrUtil() {}

    /**
     * 使用指定的连接符将多个对象连接成一个字符串。
     *
     * @param conjunction 连接符，是一个CharSequence类型的对象，用于分隔各个对象。
     * @param objs 要连接的对象数组，可以是任意类型的对象。对象会通过调用其toString()方法转换为字符串。
     * @return 返回一个字符串，其中各个对象被连接符分隔。如果objs为空或长度为0，则返回空字符串。
     */
    public static String join(CharSequence conjunction, Object... objs) {
        return cn.hutool.core.util.StrUtil.join(conjunction, objs);
    }

    /**
     * 如果字符串是{@code null}或者&quot;&quot;或者空白，则返回指定默认字符串，否则针对字符串处理后返回
     *
     * @param str          要转换的字符串
     * @param defaultValue 默认值
     * @param mapping      针对字符串的转换方法
     * @return 转换后的字符串或指定的默认字符串
     * @since 2.0.1
     */
    public static <T> T blankToDefault(CharSequence str, T defaultValue, Function<String, T> mapping) {
        return CharSequenceUtil.isBlank(str) ? defaultValue : mapping.apply(str.toString());
    }


    public static String subBefore(CharSequence string, CharSequence separator, boolean isLastSeparator) {
        return cn.hutool.core.util.StrUtil.subBefore(string, separator, isLastSeparator);
    }

    public static boolean isNotBlank(CharSequence str) {
        return cn.hutool.core.util.StrUtil.isNotBlank(str);
    }

}
