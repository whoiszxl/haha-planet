package com.whoiszxl.cache.redisson.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whoiszxl.cache.redisson.codec.SpringJacksonCodec;
import com.whoiszxl.cache.redisson.properties.RedissonProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * Redisson自动配置类
 *
 * @author whoiszxl
 * @since 1.0.0
 */
@Slf4j
@AutoConfiguration
@RequiredArgsConstructor
@ConditionalOnClass(RedissonClient.class)
@EnableConfigurationProperties(RedissonProperties.class)
@ConditionalOnProperty(prefix = "haha.cache.redisson", name = "enabled", havingValue = "true", matchIfMissing = true)
public class RedissonAutoConfiguration {

    private final RedissonProperties redissonProperties;
    private final ObjectMapper objectMapper;

    /**
     * 创建RedissonClient Bean
     */
    @Bean
    @Primary
    @ConditionalOnMissingBean
    public RedissonClient redissonClient() {
        Config config = createRedissonConfig();
        RedissonClient redissonClient = Redisson.create(config);
        log.info("Redisson客户端初始化完成，连接模式: {}", redissonProperties.getMode());
        return redissonClient;
    }

    /**
     * 创建Redisson配置
     */
    private Config createRedissonConfig() {
        Config config = new Config();
        
        // 设置编解码器
        String codecClassName = redissonProperties.getSerializationConfig().getCodec();
        if ("org.redisson.codec.JsonJacksonCodec".equals(codecClassName)) {
            // 使用Spring配置的ObjectMapper
            config.setCodec(new SpringJacksonCodec(objectMapper));
            log.info("使用SpringJacksonCodec，支持LocalDateTime等Java 8时间类型");
        } else {
            try {
                Class<?> codecClass = Class.forName(codecClassName);
                config.setCodec((org.redisson.client.codec.Codec) codecClass.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                log.warn("无法加载指定的编解码器: {}, 使用SpringJacksonCodec", codecClassName, e);
                config.setCodec(new SpringJacksonCodec(objectMapper));
            }
        }
        
        // 设置线程池
        RedissonProperties.ThreadPoolConfig threadPoolConfig = redissonProperties.getThreadPoolConfig();
        config.setThreads(threadPoolConfig.getThreads());
        config.setNettyThreads(threadPoolConfig.getNettyThreads());
        
        // 根据模式配置连接
        switch (redissonProperties.getMode()) {
            case SINGLE -> configureSingleServer(config);
            case CLUSTER -> configureClusterServers(config);
            case SENTINEL -> configureSentinelServers(config);
            case MASTER_SLAVE -> configureMasterSlaveServers(config);
            case REPLICATED -> configureReplicatedServers(config);
            default -> throw new IllegalArgumentException("不支持的Redis连接模式: " + redissonProperties.getMode());
        }
        
        return config;
    }

    /**
     * 配置单机模式
     */
    private void configureSingleServer(Config config) {
        RedissonProperties.SingleServerConfig singleConfig = redissonProperties.getSingleServerConfig();
        
        config.useSingleServer()
                .setAddress(singleConfig.getAddress())
                .setPassword(singleConfig.getPassword())
                .setDatabase(singleConfig.getDatabase())
                .setConnectionMinimumIdleSize(singleConfig.getConnectionMinimumIdleSize())
                .setConnectionPoolSize(singleConfig.getConnectionPoolSize())
                .setIdleConnectionTimeout((int) singleConfig.getIdleConnectionTimeout().toMillis())
                .setConnectTimeout((int) singleConfig.getConnectTimeout().toMillis())
                .setTimeout((int) singleConfig.getTimeout().toMillis())
                .setRetryAttempts(singleConfig.getRetryAttempts())
                .setRetryInterval((int) singleConfig.getRetryInterval().toMillis())
                .setClientName(singleConfig.getClientName())
                .setKeepAlive(singleConfig.isKeepAlive())
                .setTcpNoDelay(singleConfig.isTcpNoDelay());
    }

    /**
     * 配置集群模式
     */
    private void configureClusterServers(Config config) {
        RedissonProperties.ClusterServersConfig clusterConfig = redissonProperties.getClusterServersConfig();
        
        config.useClusterServers()
                .addNodeAddress(clusterConfig.getNodeAddresses().toArray(new String[0]))
                .setPassword(clusterConfig.getPassword())
                .setMasterConnectionMinimumIdleSize(clusterConfig.getMasterConnectionMinimumIdleSize())
                .setMasterConnectionPoolSize(clusterConfig.getMasterConnectionPoolSize())
                .setSlaveConnectionMinimumIdleSize(clusterConfig.getSlaveConnectionMinimumIdleSize())
                .setSlaveConnectionPoolSize(clusterConfig.getSlaveConnectionPoolSize())
                .setIdleConnectionTimeout((int) clusterConfig.getIdleConnectionTimeout().toMillis())
                .setConnectTimeout((int) clusterConfig.getConnectTimeout().toMillis())
                .setTimeout((int) clusterConfig.getTimeout().toMillis())
                .setRetryAttempts(clusterConfig.getRetryAttempts())
                .setRetryInterval((int) clusterConfig.getRetryInterval().toMillis())
                .setScanInterval(clusterConfig.getScanInterval())
                .setClientName(clusterConfig.getClientName())
                .setKeepAlive(clusterConfig.isKeepAlive())
                .setTcpNoDelay(clusterConfig.isTcpNoDelay());
    }

    /**
     * 配置哨兵模式
     */
    private void configureSentinelServers(Config config) {
        RedissonProperties.SentinelServersConfig sentinelConfig = redissonProperties.getSentinelServersConfig();
        
        config.useSentinelServers()
                .setMasterName(sentinelConfig.getMasterName())
                .addSentinelAddress(sentinelConfig.getSentinelAddresses().toArray(new String[0]))
                .setPassword(sentinelConfig.getPassword())
                .setSentinelPassword(sentinelConfig.getSentinelPassword())
                .setDatabase(sentinelConfig.getDatabase())
                .setMasterConnectionMinimumIdleSize(sentinelConfig.getMasterConnectionMinimumIdleSize())
                .setMasterConnectionPoolSize(sentinelConfig.getMasterConnectionPoolSize())
                .setSlaveConnectionMinimumIdleSize(sentinelConfig.getSlaveConnectionMinimumIdleSize())
                .setSlaveConnectionPoolSize(sentinelConfig.getSlaveConnectionPoolSize())
                .setIdleConnectionTimeout((int) sentinelConfig.getIdleConnectionTimeout().toMillis())
                .setConnectTimeout((int) sentinelConfig.getConnectTimeout().toMillis())
                .setTimeout((int) sentinelConfig.getTimeout().toMillis())
                .setRetryAttempts(sentinelConfig.getRetryAttempts())
                .setRetryInterval((int) sentinelConfig.getRetryInterval().toMillis())
                .setClientName(sentinelConfig.getClientName())
                .setKeepAlive(sentinelConfig.isKeepAlive())
                .setTcpNoDelay(sentinelConfig.isTcpNoDelay());
    }

    /**
     * 配置主从模式
     */
    private void configureMasterSlaveServers(Config config) {
        RedissonProperties.MasterSlaveServersConfig masterSlaveConfig = redissonProperties.getMasterSlaveServersConfig();
        
        config.useMasterSlaveServers()
                .setMasterAddress(masterSlaveConfig.getMasterAddress())
                .addSlaveAddress(masterSlaveConfig.getSlaveAddresses().toArray(new String[0]))
                .setPassword(masterSlaveConfig.getPassword())
                .setDatabase(masterSlaveConfig.getDatabase())
                .setMasterConnectionMinimumIdleSize(masterSlaveConfig.getMasterConnectionMinimumIdleSize())
                .setMasterConnectionPoolSize(masterSlaveConfig.getMasterConnectionPoolSize())
                .setSlaveConnectionMinimumIdleSize(masterSlaveConfig.getSlaveConnectionMinimumIdleSize())
                .setSlaveConnectionPoolSize(masterSlaveConfig.getSlaveConnectionPoolSize())
                .setIdleConnectionTimeout((int) masterSlaveConfig.getIdleConnectionTimeout().toMillis())
                .setConnectTimeout((int) masterSlaveConfig.getConnectTimeout().toMillis())
                .setTimeout((int) masterSlaveConfig.getTimeout().toMillis())
                .setRetryAttempts(masterSlaveConfig.getRetryAttempts())
                .setRetryInterval((int) masterSlaveConfig.getRetryInterval().toMillis())
                .setClientName(masterSlaveConfig.getClientName())
                .setKeepAlive(masterSlaveConfig.isKeepAlive())
                .setTcpNoDelay(masterSlaveConfig.isTcpNoDelay());
    }

    /**
     * 配置复制模式
     */
    private void configureReplicatedServers(Config config) {
        RedissonProperties.ReplicatedServersConfig replicatedConfig = redissonProperties.getReplicatedServersConfig();
        
        config.useReplicatedServers()
                .addNodeAddress(replicatedConfig.getNodeAddresses().toArray(new String[0]))
                .setPassword(replicatedConfig.getPassword())
                .setDatabase(replicatedConfig.getDatabase())
                .setMasterConnectionMinimumIdleSize(replicatedConfig.getMasterConnectionMinimumIdleSize())
                .setMasterConnectionPoolSize(replicatedConfig.getMasterConnectionPoolSize())
                .setSlaveConnectionMinimumIdleSize(replicatedConfig.getSlaveConnectionMinimumIdleSize())
                .setSlaveConnectionPoolSize(replicatedConfig.getSlaveConnectionPoolSize())
                .setIdleConnectionTimeout((int) replicatedConfig.getIdleConnectionTimeout().toMillis())
                .setConnectTimeout((int) replicatedConfig.getConnectTimeout().toMillis())
                .setTimeout((int) replicatedConfig.getTimeout().toMillis())
                .setRetryAttempts(replicatedConfig.getRetryAttempts())
                .setRetryInterval((int) replicatedConfig.getRetryInterval().toMillis())
                .setScanInterval(replicatedConfig.getScanInterval())
                .setClientName(replicatedConfig.getClientName())
                .setKeepAlive(replicatedConfig.isKeepAlive())
                .setTcpNoDelay(replicatedConfig.isTcpNoDelay());
    }
}