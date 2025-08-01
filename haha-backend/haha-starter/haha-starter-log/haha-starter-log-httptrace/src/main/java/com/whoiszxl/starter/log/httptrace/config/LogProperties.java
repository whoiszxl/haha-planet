package com.whoiszxl.starter.log.httptrace.config;

import com.whoiszxl.starter.core.constants.PropertiesConstants;
import com.whoiszxl.starter.log.core.enums.Include;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashSet;
import java.util.Set;

/**
 * 日志配置属性
 */
@ConfigurationProperties(PropertiesConstants.LOG)
public class LogProperties {

    /**
     * 是否启用日志
     */
    private boolean enabled = true;

    /**
     * 是否打印日志，开启后可打印访问日志（类似于 Nginx access log）
     */
    private Boolean isPrint = false;

    /**
     * 包含信息
     */
    private Set<Include> includes = new HashSet<>(Include.defaultIncludes());

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getIsPrint() {
        return isPrint;
    }

    public void setIsPrint(Boolean print) {
        isPrint = print;
    }

    public Set<Include> getIncludes() {
        return includes;
    }

    public void setIncludes(Set<Include> includes) {
        this.includes = includes;
    }
}
