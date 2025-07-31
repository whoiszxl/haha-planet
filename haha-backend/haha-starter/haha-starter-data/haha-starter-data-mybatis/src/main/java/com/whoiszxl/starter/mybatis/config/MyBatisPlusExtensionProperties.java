package com.whoiszxl.starter.mybatis.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.whoiszxl.starter.mybatis.config.idgenerator.MyBatisPlusIdGeneratorProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * MyBatis Plus 扩展配置属性
 */
@Data
@ConfigurationProperties(prefix = "mybatis-plus.extension")
public class MyBatisPlusExtensionProperties {

    /**
     * 是否启用扩展
     */
    private boolean enabled = false;

    /**
     * Mapper 接口扫描包（配置时必须使用：mapper-package 键名）
     * <p>
     * e.g. com.example.**.mapper
     * </p>
     */
    private String mapperPackage;

    /**
     * ID 生成器
     */
    @NestedConfigurationProperty
    private MyBatisPlusIdGeneratorProperties idGenerator;

    /**
     * 数据权限插件配置
     */
    private DataPermissionProperties dataPermission;

    /**
     * 分页插件配置
     */
    private PaginationProperties pagination;

    /**
     * 数据权限插件配置属性
     */
    @Data
    public static class DataPermissionProperties {

        /**
         * 是否启用数据权限插件
         */
        private boolean enabled = false;

    }

    /**
     * 分页插件配置属性
     */
    @Data
    public static class PaginationProperties {

        /**
         * 是否启用分页插件
         */
        private boolean enabled = true;

        /**
         * 数据库类型
         */
        private DbType dbType;

        /**
         * 是否溢出处理
         */
        private boolean overflow = false;

        /**
         * 单页分页条数限制（默认：-1 表示无限制）
         */
        private Long maxLimit = -1L;
    }
}
