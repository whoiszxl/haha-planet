package com.whoiszxl.starter.core.constants;

/**
 * 配置属性常量集
 */
public interface PropertiesConstants {

    String ENABLED = "enabled";

    String WEB = "web";

    String CORS = WEB + StringConstants.DOT + "cors";

    String XSS = WEB + StringConstants.DOT + "XSS";

    String TRACE = WEB + StringConstants.DOT + "trace";


    String SPRINGDOC = "springdoc";
    String SPRINGDOC_SWAGGER_UI = SPRINGDOC + StringConstants.DOT + "swagger-ui";

    String CAPTCHA_GRAPHIC = "captcha.graphic";


    String SECURITY_CRYPTO = "security.crypto";
    String SECURITY_PASSWORD = "security.password";


    String LOG = "log";
}
