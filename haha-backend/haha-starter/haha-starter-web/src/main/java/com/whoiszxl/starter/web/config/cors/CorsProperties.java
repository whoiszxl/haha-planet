package com.whoiszxl.starter.web.config.cors;

import com.whoiszxl.starter.core.constants.PropertiesConstants;
import com.whoiszxl.starter.core.constants.StringConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ConfigurationProperties(PropertiesConstants.CORS)
public class CorsProperties {

    private boolean enabled = false;

    private static final List<String> ALL = Collections.singletonList(StringConstants.ASTERISK);

    private List<String> allowedOrigins = new ArrayList<>(ALL);

    private List<String> allowedMethods = new ArrayList<>(ALL);

    private List<String> allowedHeaders = new ArrayList<>(ALL);

    private List<String> exposedHeaders = new ArrayList<>();

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public List<String> getAllowedOrigins() {
        return allowedOrigins;
    }

    public void setAllowedOrigins(List<String> allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    public List<String> getAllowedMethods() {
        return allowedMethods;
    }

    public void setAllowedMethods(List<String> allowedMethods) {
        this.allowedMethods = allowedMethods;
    }

    public List<String> getAllowedHeaders() {
        return allowedHeaders;
    }

    public void setAllowedHeaders(List<String> allowedHeaders) {
        this.allowedHeaders = allowedHeaders;
    }

    public List<String> getExposedHeaders() {
        return exposedHeaders;
    }

    public void setExposedHeaders(List<String> exposedHeaders) {
        this.exposedHeaders = exposedHeaders;
    }
}
