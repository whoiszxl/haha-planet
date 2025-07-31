package com.whoiszxl.starter.docs.config;

import com.whoiszxl.starter.core.constants.PropertiesConstants;
import io.swagger.v3.oas.models.Components;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author whoiszxl
 */
@Setter
@Getter
@ConfigurationProperties(prefix = PropertiesConstants.SPRINGDOC)
public class SpringDocExtensionProperties {

    @NestedConfigurationProperty
    private Components components;

}
