package com.whoiszxl.starter.security.password.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.whoiszxl.starter.core.constants.PropertiesConstants;
import com.whoiszxl.starter.core.utils.validate.CheckUtils;
import com.whoiszxl.starter.security.password.service.PasswordAuditService;
import com.whoiszxl.starter.security.password.service.PasswordHistoryService;
import com.whoiszxl.starter.security.password.service.PasswordStrengthService;
import com.whoiszxl.starter.security.password.service.impl.DefaultPasswordAuditService;
import com.whoiszxl.starter.security.password.service.impl.DefaultPasswordHistoryService;
import com.whoiszxl.starter.security.password.service.impl.DefaultPasswordStrengthService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 密码编码器自动配置
 *
 * <p>
 * 企业级密码安全配置类，提供多种密码编码器和安全功能：<br/>
 * 1. 多种密码编码算法支持（BCrypt、Argon2、SCrypt、PBKDF2）<br/>
 * 2. 密码强度验证<br/>
 * 3. 密码历史记录管理<br/>
 * 4. 密码过期策略<br/>
 * 5. 安全审计日志<br/>
 * 6. 自定义编码器扩展支持
 * </p>
 *
 * @author whoiszxl
 */
@Slf4j
@AutoConfiguration
@RequiredArgsConstructor
@EnableConfigurationProperties(PasswordEncoderProperties.class)
@ConditionalOnProperty(prefix = PropertiesConstants.SECURITY_PASSWORD, name = PropertiesConstants.ENABLED, havingValue = "true")
@Import({
    PasswordStrengthConfiguration.class,
    PasswordHistoryConfiguration.class,
    PasswordAuditConfiguration.class
})
public class PasswordEncoderAutoConfiguration {

    private final PasswordEncoderProperties properties;

    /**
     * 密码编码器
     *
     * @param passwordEncoderList 自定义密码编码器列表
     * @return DelegatingPasswordEncoder
     */
    @Bean
    @ConditionalOnMissingBean
    public PasswordEncoder passwordEncoder(List<PasswordEncoder> passwordEncoderList) {
        Map<String, PasswordEncoder> encoders = createDefaultEncoders();
        
        // 添加自定义的密码编解码器
        if (CollUtil.isNotEmpty(passwordEncoderList)) {
            passwordEncoderList.forEach(passwordEncoder -> {
                String simpleName = passwordEncoder.getClass().getSimpleName();
                String encoderId = CharSequenceUtil.removeSuffix(simpleName, "PasswordEncoder").toLowerCase();
                encoders.put(encoderId, passwordEncoder);
                log.debug("[HaHa Security] 注册自定义密码编码器: {}", encoderId);
            });
        }
        
        String encodingId = properties.getEncodingId();
        CheckUtils.throwIf(!encoders.containsKey(encodingId), 
            "密码编码器 {} 未找到，可用的编码器: {}", encodingId, encoders.keySet());
        
        log.info("[HaHa Security] 密码编码器初始化完成，默认编码器: {}, 可用编码器: {}", 
            encodingId, encoders.keySet());
        
        return new DelegatingPasswordEncoder(encodingId, encoders);
    }

    /**
     * 密码强度验证服务
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = PropertiesConstants.SECURITY_PASSWORD + ".strength", 
        name = "enabled", havingValue = "true", matchIfMissing = true)
    public PasswordStrengthService passwordStrengthService() {
        return new DefaultPasswordStrengthService(properties.getStrength());
    }

    /**
     * 密码历史记录服务
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = PropertiesConstants.SECURITY_PASSWORD + ".history", 
        name = "enabled", havingValue = "true")
    public PasswordHistoryService passwordHistoryService() {
        return new DefaultPasswordHistoryService(properties.getHistory());
    }

    /**
     * 密码审计服务
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = PropertiesConstants.SECURITY_PASSWORD + ".audit", 
        name = "enabled", havingValue = "true", matchIfMissing = true)
    public PasswordAuditService passwordAuditService() {
        return new DefaultPasswordAuditService(properties.getAudit());
    }

    /**
     * 创建默认的密码编码器映射
     */
    private Map<String, PasswordEncoder> createDefaultEncoders() {
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        
        // BCrypt 编码器
        encoders.put("bcrypt", new BCryptPasswordEncoder(properties.getBcrypt().getStrength()));
        
        // PBKDF2 编码器
        PasswordEncoderProperties.Pbkdf2Config pbkdf2Config = properties.getPbkdf2();
        encoders.put("pbkdf2", new Pbkdf2PasswordEncoder(
            "",
            pbkdf2Config.getSaltLength(),
            pbkdf2Config.getIterations(),
            Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256
        ));
        
        // SCrypt 编码器
        PasswordEncoderProperties.ScryptConfig scryptConfig = properties.getScrypt();
        encoders.put("scrypt", new SCryptPasswordEncoder(
            scryptConfig.getCpuCost(),
            scryptConfig.getMemoryCost(),
            scryptConfig.getParallelization(),
            scryptConfig.getKeyLength(),
            scryptConfig.getSaltLength()
        ));
        
        // Argon2 编码器
        PasswordEncoderProperties.Argon2Config argon2Config = properties.getArgon2();
        encoders.put("argon2", new Argon2PasswordEncoder(
            argon2Config.getSaltLength(),
            argon2Config.getHashLength(),
            argon2Config.getParallelism(),
            argon2Config.getMemory(),
            argon2Config.getIterations()
        ));
        
        return encoders;
    }

    @PostConstruct
    public void postConstruct() {
        log.info("[HaHa Security] 密码安全模块初始化完成");
        log.debug("[HaHa Security] 配置详情: 编码器={}, 强度验证={}, 历史记录={}, 审计日志={}",
            properties.getEncodingId(),
            properties.getStrength().isEnabled(),
            properties.getHistory().isEnabled(),
            properties.getAudit().isEnabled());
    }
}
