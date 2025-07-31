package com.whoiszxl.cache.redisson.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.Duration;
import java.util.List;

/**
 * Redisson配置属性
 *
 * @author whoiszxl
 * @since 1.0.0
 */
@Data
@Validated
@ConfigurationProperties(prefix = "haha.cache.redisson")
public class RedissonProperties {

    /**
     * 是否启用Redisson
     */
    private boolean enabled = true;

    /**
     * Redis连接模式
     */
    private Mode mode = Mode.SINGLE;

    /**
     * 单机配置
     */
    private SingleServerConfig singleServerConfig = new SingleServerConfig();

    /**
     * 集群配置
     */
    private ClusterServersConfig clusterServersConfig = new ClusterServersConfig();

    /**
     * 哨兵配置
     */
    private SentinelServersConfig sentinelServersConfig = new SentinelServersConfig();

    /**
     * 主从配置
     */
    private MasterSlaveServersConfig masterSlaveServersConfig = new MasterSlaveServersConfig();

    /**
     * 复制配置
     */
    private ReplicatedServersConfig replicatedServersConfig = new ReplicatedServersConfig();

    /**
     * 线程池配置
     */
    private ThreadPoolConfig threadPoolConfig = new ThreadPoolConfig();

    /**
     * 网络配置
     */
    private NetworkConfig networkConfig = new NetworkConfig();

    /**
     * 序列化配置
     */
    private SerializationConfig serializationConfig = new SerializationConfig();

    /**
     * Redis连接模式枚举
     */
    public enum Mode {
        SINGLE, CLUSTER, SENTINEL, MASTER_SLAVE, REPLICATED
    }

    /**
     * 单机服务器配置
     */
    @Data
    public static class SingleServerConfig {
        @NotBlank(message = "Redis地址不能为空")
        private String address = "redis://117.72.102.228:6379";
        private String password = "G2w2e2A6d1oeRfaCdvg";
        private int database = 0;
        private int connectionMinimumIdleSize = 24;
        private int connectionPoolSize = 64;
        private Duration idleConnectionTimeout = Duration.ofMinutes(10);
        private Duration connectTimeout = Duration.ofSeconds(10);
        private Duration timeout = Duration.ofSeconds(3);
        private int retryAttempts = 3;
        private Duration retryInterval = Duration.ofMillis(1500);
        private String clientName;
        private boolean keepAlive = true;
        private boolean tcpNoDelay = true;
    }

    /**
     * 集群服务器配置
     */
    @Data
    public static class ClusterServersConfig {
        @NotNull(message = "集群节点地址不能为空")
        private List<String> nodeAddresses;
        private String password;
        private int masterConnectionMinimumIdleSize = 24;
        private int masterConnectionPoolSize = 64;
        private int slaveConnectionMinimumIdleSize = 24;
        private int slaveConnectionPoolSize = 64;
        private Duration idleConnectionTimeout = Duration.ofMinutes(10);
        private Duration connectTimeout = Duration.ofSeconds(10);
        private Duration timeout = Duration.ofSeconds(3);
        private int retryAttempts = 3;
        private Duration retryInterval = Duration.ofMillis(1500);
        private int scanInterval = 1000;
        private String clientName;
        private boolean keepAlive = true;
        private boolean tcpNoDelay = true;
    }

    /**
     * 哨兵服务器配置
     */
    @Data
    public static class SentinelServersConfig {
        @NotBlank(message = "主服务器名称不能为空")
        private String masterName;
        @NotNull(message = "哨兵地址不能为空")
        private List<String> sentinelAddresses;
        private String password;
        private String sentinelPassword;
        private int database = 0;
        private int masterConnectionMinimumIdleSize = 24;
        private int masterConnectionPoolSize = 64;
        private int slaveConnectionMinimumIdleSize = 24;
        private int slaveConnectionPoolSize = 64;
        private Duration idleConnectionTimeout = Duration.ofMinutes(10);
        private Duration connectTimeout = Duration.ofSeconds(10);
        private Duration timeout = Duration.ofSeconds(3);
        private int retryAttempts = 3;
        private Duration retryInterval = Duration.ofMillis(1500);
        private String clientName;
        private boolean keepAlive = true;
        private boolean tcpNoDelay = true;
    }

    /**
     * 主从服务器配置
     */
    @Data
    public static class MasterSlaveServersConfig {
        @NotBlank(message = "主服务器地址不能为空")
        private String masterAddress;
        @NotNull(message = "从服务器地址不能为空")
        private List<String> slaveAddresses;
        private String password;
        private int database = 0;
        private int masterConnectionMinimumIdleSize = 24;
        private int masterConnectionPoolSize = 64;
        private int slaveConnectionMinimumIdleSize = 24;
        private int slaveConnectionPoolSize = 64;
        private Duration idleConnectionTimeout = Duration.ofMinutes(10);
        private Duration connectTimeout = Duration.ofSeconds(10);
        private Duration timeout = Duration.ofSeconds(3);
        private int retryAttempts = 3;
        private Duration retryInterval = Duration.ofMillis(1500);
        private String clientName;
        private boolean keepAlive = true;
        private boolean tcpNoDelay = true;
    }

    /**
     * 复制服务器配置
     */
    @Data
    public static class ReplicatedServersConfig {
        @NotNull(message = "节点地址不能为空")
        private List<String> nodeAddresses;
        private String password;
        private int database = 0;
        private int masterConnectionMinimumIdleSize = 24;
        private int masterConnectionPoolSize = 64;
        private int slaveConnectionMinimumIdleSize = 24;
        private int slaveConnectionPoolSize = 64;
        private Duration idleConnectionTimeout = Duration.ofMinutes(10);
        private Duration connectTimeout = Duration.ofSeconds(10);
        private Duration timeout = Duration.ofSeconds(3);
        private int retryAttempts = 3;
        private Duration retryInterval = Duration.ofMillis(1500);
        private int scanInterval = 1000;
        private String clientName;
        private boolean keepAlive = true;
        private boolean tcpNoDelay = true;
    }

    /**
     * 线程池配置
     */
    @Data
    public static class ThreadPoolConfig {
        @Positive(message = "线程数必须大于0")
        private int threads = Runtime.getRuntime().availableProcessors() * 2;
        @Positive(message = "Netty线程数必须大于0")
        private int nettyThreads = Runtime.getRuntime().availableProcessors() * 2;
        private String threadNamePrefix = "redisson-";
    }

    /**
     * 网络配置
     */
    @Data
    public static class NetworkConfig {
        private Duration pingConnectionInterval = Duration.ofSeconds(30);
        private boolean keepAlive = true;
        private boolean tcpNoDelay = true;
        private int pingTimeout = 1000;
    }

    /**
     * 序列化配置
     */
    @Data
    public static class SerializationConfig {
        private String codec = "org.redisson.codec.JsonJacksonCodec";
        private boolean useLinuxNativeEpoll = false;
    }
}