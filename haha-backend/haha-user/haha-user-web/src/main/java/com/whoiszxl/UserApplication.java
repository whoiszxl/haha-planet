package com.whoiszxl;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.whoiszxl.starter.core.config.ProjectProperties;
import com.whoiszxl.starter.crud.annotation.EnableCrudRestController;
import com.whoiszxl.starter.web.annotation.EnableGlobalExceptionHandler;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * HAHA 星球服务启动程序
 * 
 * <p>企业级Spring Boot应用启动类，集成了以下特性：</p>
 * <ul>
 *   <li>应用信息展示与健康检查</li>
 *   <li>启动性能监控与统计</li>
 *   <li>系统环境信息收集</li>
 *   <li>优雅的日志输出</li>
 *   <li>Restful API端点</li>
 * </ul>
 * 
 * @author whoiszxl
 * @version 1.0.0
 * @since 2024-01-01
 */
@Slf4j
@RestController
@EnableFeignClients
@SpringBootApplication(scanBasePackages = "com.whoiszxl")
@RequiredArgsConstructor
@EnableCrudRestController
@EnableGlobalExceptionHandler
@Tag(name = "应用管理", description = "应用状态与信息管理接口")
public class UserApplication implements ApplicationRunner {

    private final ProjectProperties projectProperties;
    private final ServerProperties serverProperties;
    private final ApplicationContext applicationContext;
    private final Environment environment;
    
    /** 应用启动时间 */
    private static final long START_TIME = System.currentTimeMillis();
    
    /** 应用就绪时间 */
    private volatile long readyTime;

    /**
     * 应用程序入口点
     * 
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 设置系统属性
        System.setProperty("spring.application.admin.enabled", "true");
        System.setProperty("file.encoding", "UTF-8");
        
        log.info("🚀 正在启动 {} 应用...", "HAHA Admin");
        
        try {
            SpringApplication app = new SpringApplication(UserApplication.class);
            // 设置应用启动横幅
            app.setBannerMode(Banner.Mode.CONSOLE);
            // 启动应用
            app.run(args);
        } catch (Exception e) {
            log.error("❌ 应用启动失败: {}", e.getMessage(), e);
            System.exit(1);
        }
    }

    /**
     * 应用首页接口
     * 
     * @return 应用状态信息
     */
    @Hidden
    @SaIgnore
    @GetMapping("/")
    @Operation(summary = "应用首页", description = "获取应用基本状态信息")
    public Map<String, Object> index() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("application", projectProperties.getName());
        result.put("version", projectProperties.getVersion());
        result.put("description", projectProperties.getDescription());
        result.put("status", "running");
        result.put("timestamp", DateUtil.now());
        result.put("uptime", getUptime());
        return result;
    }
    
    /**
     * 应用信息接口
     * 
     * @return 详细应用信息
     */
    @SaIgnore
    @GetMapping("/info")
    @Operation(summary = "应用信息", description = "获取详细的应用信息")
    public Map<String, Object> info() {
        Map<String, Object> result = new LinkedHashMap<>();

        Map<String, Object> project = new LinkedHashMap<>();
        project.put("name", projectProperties.getName());
        project.put("appName", projectProperties.getAppName());
        project.put("version", projectProperties.getVersion());
        project.put("description", projectProperties.getDescription());
        project.put("basePackage", projectProperties.getBasePackage());
        
        if (projectProperties.getContact() != null) {
            Map<String, Object> contact = new LinkedHashMap<>();
            contact.put("name", projectProperties.getContact().getName());
            contact.put("email", projectProperties.getContact().getEmail());
            contact.put("url", projectProperties.getContact().getUrl());
            project.put("contact", contact);
        }
        
        result.put("project", project);
        
        // 环境信息
        Map<String, Object> env = new LinkedHashMap<>();
        env.put("activeProfiles", environment.getActiveProfiles());
        env.put("javaVersion", System.getProperty("java.version"));
        env.put("springBootVersion", SpringApplication.class.getPackage().getImplementationVersion());
        result.put("environment", env);
        
        // 运行时信息
        Map<String, Object> runtime = new LinkedHashMap<>();
        runtime.put("startTime", DateUtil.date(START_TIME));
        runtime.put("uptime", getUptime());
        runtime.put("pid", ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);
        result.put("runtime", runtime);
        
        return result;
    }

    /**
     * 应用启动完成后的回调
     * 
     * @param args 应用参数
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        printStartupInfo();
    }
    
    /**
     * 应用就绪事件监听
     * 
     * @param event 应用就绪事件
     */
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady(ApplicationReadyEvent event) {
        this.readyTime = System.currentTimeMillis();
        long startupTime = readyTime - START_TIME;
        
        log.info("🎉 应用启动完成! 总耗时: {}ms", startupTime);
        log.info("📊 Bean总数: {}, 启动线程: {}", 
                applicationContext.getBeanDefinitionCount(),
                Thread.currentThread().getName());
    }

    /**
     * 打印启动信息
     */
    private void printStartupInfo() {
        try {
            String hostAddress = getHostAddress();
            Integer port = serverProperties.getPort();
            String contextPath = getContextPath();
            String baseUrl = buildBaseUrl(hostAddress, port, contextPath);
            
            String banner = buildStartupBanner(baseUrl);
            log.info(banner);
            
        } catch (Exception e) {
            log.warn("获取启动信息时发生异常: {}", e.getMessage());
        }
    }
    
    /**
     * 构建启动横幅
     */
    private String buildStartupBanner(String baseUrl) {
        return String.format("""
                
                ╔══════════════════════════════════════════════════════════════╗
                ║                    🎯 应用启动成功                            ║
                ╠══════════════════════════════════════════════════════════════╣
                ║  应用名称: %-50s ║
                ║  应用版本: %-50s ║
                ║  启动时间: %-50s ║
                ║  运行环境: %-50s ║
                ║  Java版本: %-50s ║
                ╠══════════════════════════════════════════════════════════════╣
                ║  🌐 访问地址: %-47s ║
                ║  📚 接口文档: %-47s ║
                ║  ❤️  健康检查: %-47s ║
                ║  ℹ️  应用信息: %-47s ║
                ╚══════════════════════════════════════════════════════════════╝
                """,
                projectProperties.getName(),
                projectProperties.getVersion(),
                DateUtil.now(),
                String.join(", ", environment.getActiveProfiles()),
                System.getProperty("java.version"),
                baseUrl,
                baseUrl + "/doc.html",
                baseUrl + "/health",
                baseUrl + "/info"
        );
    }
    
    /**
     * 获取主机地址
     */
    private String getHostAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            log.warn("获取主机地址失败，使用默认值: {}", e.getMessage());
            return "localhost";
        }
    }
    
    /**
     * 获取上下文路径
     */
    private String getContextPath() {
        String contextPath = serverProperties.getServlet().getContextPath();
        return StrUtil.isBlank(contextPath) ? "" : contextPath;
    }
    
    /**
     * 构建基础URL
     */
    private String buildBaseUrl(String hostAddress, Integer port, String contextPath) {
        String protocol = serverProperties.getSsl() != null && serverProperties.getSsl().isEnabled() ? "https" : "http";
        return URLUtil.normalize(String.format("%s://%s:%s%s", protocol, hostAddress, port, contextPath));
    }
    
    /**
     * 获取应用运行时间
     */
    private String getUptime() {
        long uptime = System.currentTimeMillis() - START_TIME;
        Duration duration = Duration.ofMillis(uptime);
        
        long days = duration.toDays();
        long hours = duration.toHours() % 24;
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;
        
        if (days > 0) {
            return String.format("%d天 %d小时 %d分钟 %d秒", days, hours, minutes, seconds);
        } else if (hours > 0) {
            return String.format("%d小时 %d分钟 %d秒", hours, minutes, seconds);
        } else if (minutes > 0) {
            return String.format("%d分钟 %d秒", minutes, seconds);
        } else {
            return String.format("%d秒", seconds);
        }
    }
}
