package com.whoiszxl.starter.core.utils;

import com.whoiszxl.starter.core.constants.StringConstants;

public class LogFormatUtil {

    private LogFormatUtil() {}

    private static final String FORMAT = "%s|%s|%s";


    public static String format(Object o, String desc, Object... params) {
        String paramsStr = MyStrUtil.join(StringConstants.COMMA, params);
        return String.format(FORMAT, o.getClass().getName(), desc, paramsStr);
    }
}
