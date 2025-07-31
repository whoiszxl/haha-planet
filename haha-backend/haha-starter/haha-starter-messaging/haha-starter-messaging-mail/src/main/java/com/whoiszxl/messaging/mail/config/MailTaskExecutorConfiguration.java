package com.whoiszxl.messaging.mail.config;

import com.whoiszxl.messaging.mail.properties.MailProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 邮件任务执行器配置
 *
 * @author whoiszxl
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "haha.messaging.mail.async", name = "enabled", havingValue = "true", matchIfMissing = true)
public class MailTaskExecutorConfiguration {

    private final MailProperties mailProperties;

    /**
     * 邮件异步任务执行器
     */
    @Bean(name = "mailTaskExecutor")
    public Executor mailTaskExecutor() {
        MailProperties.Async asyncConfig = mailProperties.getAsync();
        
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        
        // 核心线程数
        executor.setCorePoolSize(asyncConfig.getCorePoolSize());
        
        // 最大线程数
        executor.setMaxPoolSize(asyncConfig.getMaxPoolSize());
        
        // 队列容量
        executor.setQueueCapacity(asyncConfig.getQueueCapacity());
        
        // 线程名前缀
        executor.setThreadNamePrefix(asyncConfig.getThreadNamePrefix());
        
        // 线程空闲时间（秒）
        executor.setKeepAliveSeconds((int) asyncConfig.getKeepAliveTime().getSeconds());
        
        // 拒绝策略：由调用线程处理该任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        
        // 等待时间（秒）
        executor.setAwaitTerminationSeconds(60);
        
        // 初始化
        executor.initialize();
        
        log.info("[HaHa Mail] 邮件异步任务执行器初始化完成 - 核心线程数: {}, 最大线程数: {}, 队列容量: {}", 
                asyncConfig.getCorePoolSize(), 
                asyncConfig.getMaxPoolSize(), 
                asyncConfig.getQueueCapacity());
        
        return executor;
    }
}