package com.whoiszxl.starter.mybatis.config;

import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.DataPermissionHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DataPermissionInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.whoiszxl.starter.core.constants.PropertiesConstants;
import com.whoiszxl.starter.core.utils.CustomPropertySourceFactory;
import com.whoiszxl.starter.mybatis.datapermission.DataPermissionFilter;
import com.whoiszxl.starter.mybatis.datapermission.DataPermissionHandlerImpl;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@AutoConfiguration
@MapperScan("${mybatis-plus.extension.mapper-package}")
@EnableTransactionManagement(proxyTargetClass = true)
@EnableConfigurationProperties(MyBatisPlusExtensionProperties.class)
@ConditionalOnProperty(prefix = "mybatis-plus.extension", name = PropertiesConstants.ENABLED, havingValue = "true")
@PropertySource(value = "classpath:default-data-mybatis-plus.yml", factory = CustomPropertySourceFactory.class)
public class MybatisPlusAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(MybatisPlusAutoConfiguration.class);

    /**
     * 创建并配置MybatisPlusInterceptor Bean。
     * 只有在当前应用没有定义MybatisPlusInterceptor Bean时，才会创建并返回该Bean。
     * 这个方法会根据配置决定是否添加数据权限拦截器和分页拦截器。
     * 并且还会添加一个防止全表更新和删除的插件。
     *
     * @param properties MyBatisPlus扩展属性，用于配置数据权限和分页等属性。
     * @return 配置好的MybatisPlusInterceptor实例。
     */
    @Bean
    @ConditionalOnMissingBean
    public MybatisPlusInterceptor mybatisPlusInterceptor(MyBatisPlusExtensionProperties properties) {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 根据配置开启数据权限拦截器
        MyBatisPlusExtensionProperties.DataPermissionProperties dataPermissionProperties
                = properties.getDataPermission();
        if(dataPermissionProperties != null && dataPermissionProperties.isEnabled()) {
            interceptor.addInnerInterceptor(new DataPermissionInterceptor(SpringUtil.getBean(DataPermissionHandler.class)));
        }

        // 根据配置开启分页拦截器
        MyBatisPlusExtensionProperties.PaginationProperties paginationProperties = properties.getPagination();
        if (null != paginationProperties && paginationProperties.isEnabled()) {
            interceptor.addInnerInterceptor(this.paginationInnerInterceptor(paginationProperties));
        }

        // 添加防止全表更新和删除的插件
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());

        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return interceptor;
    }



    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnEnabledDataPermission
    public DataPermissionHandler dataPermissionHandler(DataPermissionFilter dataPermissionFilter) {
        return new DataPermissionHandlerImpl(dataPermissionFilter);
    }



    private PaginationInnerInterceptor paginationInnerInterceptor(MyBatisPlusExtensionProperties.PaginationProperties paginationProperties) {
        PaginationInnerInterceptor paginationInnerInterceptor = null != paginationProperties.getDbType()
                ? new PaginationInnerInterceptor(paginationProperties.getDbType())
                : new PaginationInnerInterceptor();
        paginationInnerInterceptor.setOverflow(paginationProperties.isOverflow());
        paginationInnerInterceptor.setMaxLimit(paginationProperties.getMaxLimit());
        return paginationInnerInterceptor;
    }
}
