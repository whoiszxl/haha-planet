package com.whoiszxl.starter.web.config.cors;

import com.whoiszxl.starter.core.constants.PropertiesConstants;
import com.whoiszxl.starter.core.constants.StringConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Lazy
@AutoConfiguration
@ConditionalOnWebApplication
@ConditionalOnProperty(prefix = PropertiesConstants.CORS, name = PropertiesConstants.ENABLED, havingValue = "true")
@EnableConfigurationProperties(CorsProperties.class)
public class CorsAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(CorsAutoConfiguration.class);

    /**
     * 创建一个CorsFilter Bean，用于处理跨域请求。
     * 该Bean只有在当前应用缺少CorsFilter Bean时才会被创建。
     *
     * @param properties CorsProperties的实例，用于配置跨域请求的属性。
     * @return CorsFilter 跨域请求过滤器实例。
     */
    @Bean
    @ConditionalOnMissingBean
    public CorsFilter corsFilter(CorsProperties properties) {
        CorsConfiguration config = new CorsConfiguration();
        // 设置预检请求的有效期为30分钟。
        config.setMaxAge(1800L);

        // 根据properties中的AllowedOrigins配置，决定是否使用 * 通配符。
        if (properties.getAllowedOrigins().contains(StringConstants.ASTERISK)) {
            config.addAllowedOriginPattern(StringConstants.ASTERISK);
        } else {
            // 如果没有使用通配符，则设置允许的来源，并开启credentials支持。
            config.setAllowCredentials(true);
            properties.getAllowedOrigins().forEach(config::addAllowedOrigin);
        }

        // 配置允许的HTTP方法和头信息。
        properties.getAllowedMethods().forEach(config::addAllowedMethod);
        properties.getAllowedHeaders().forEach(config::addAllowedHeader);
        properties.getExposedHeaders().forEach(config::addExposedHeader);

        // 使用URL为基础的配置源，并注册CorsConfiguration。
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(StringConstants.PATH_PATTERN, config);

        CorsFilter corsFilter = new CorsFilter(source);
        log.debug("CORS 过滤器初始化成功");
        return corsFilter;
    }


}
