package com.whoiszxl.starter.web.config.trace;

import com.yomahub.tlog.id.TLogIdGenerator;
import com.yomahub.tlog.id.snowflake.UniqueIdGenerator;

/**
 * TLog ID 生成器
 */
public class TraceIdGenerator extends TLogIdGenerator {
    @Override
    public String generateTraceId() {
        return String.valueOf(UniqueIdGenerator.generateId());
    }
}