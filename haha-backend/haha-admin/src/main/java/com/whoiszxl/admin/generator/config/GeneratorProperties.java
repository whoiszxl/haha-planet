package com.whoiszxl.admin.generator.config;

import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.map.MapUtil;
import com.whoiszxl.starter.mybatis.enums.DatabaseType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 代码生成器配置属性
 * @author whoiszxl
 */
@Data
@Component
@ConfigurationProperties(prefix = "generator")
public class GeneratorProperties {

    /**
     * 排除数据表（哪些数据表不展示在代码生成中）
     */
    private String[] excludeTables = new String[0];

    /**
     * 类型映射
     */
    private Map<DatabaseType, Map<String, List<String>>> typeMappings = MapUtil.newHashMap();

    /**
     * 模板配置
     */
    private Map<String, TemplateConfig> templateConfigs = MapUtil.newHashMap(true);

    /**
     * 模板配置
     */
    @Data
    public static class TemplateConfig {

        /**
         * 模板路径
         */
        private String templatePath;

        /**
         * 包名称
         */
        private String packageName;

        /**
         * 排除字段
         */
        private String[] excludeFields;

        /**
         * 扩展名
         */
        private String extension = FileNameUtil.EXT_JAVA;

        /**
         * 是否为后端模板
         */
        private boolean backend = true;
    }
}
