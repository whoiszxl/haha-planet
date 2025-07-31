package com.whoiszxl.admin.config;

import com.whoiszxl.admin.mapper.AdminLogMapper;
import com.whoiszxl.admin.service.IAdminService;
import com.whoiszxl.starter.log.core.dao.LogDao;
import com.whoiszxl.starter.log.httptrace.config.ConditionalOnEnabledLog;
import com.whoiszxl.starter.web.config.trace.TraceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author whoiszxl
 */
@Configuration
@ConditionalOnEnabledLog
public class LogConfiguration {

    @Bean
    public LogDao logDao(IAdminService adminService, AdminLogMapper logMapper, TraceProperties traceProperties) {
        return new LogDaoLocalImpl(adminService, logMapper, traceProperties);
    }
}
