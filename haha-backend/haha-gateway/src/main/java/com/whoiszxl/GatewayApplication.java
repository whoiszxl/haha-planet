package com.whoiszxl;

import cn.hutool.core.util.URLUtil;
import cn.hutool.extra.spring.EnableSpringUtil;
import com.whoiszxl.starter.core.config.ProjectProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;

/**
 * 网关，启动！
 * @author whoiszxl
 */
@Slf4j
@RestController
@SpringBootApplication
@RequiredArgsConstructor
@EnableSpringUtil
public class GatewayApplication implements ApplicationRunner {

    private final ProjectProperties projectProperties;

    private final ServerProperties serverProperties;

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @GetMapping("/")
    public String index() {
        return String.format("%s 服务启动成功", projectProperties.getName());
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        String hostAddress = InetAddress.getLocalHost().getHostAddress();
        Integer port = serverProperties.getPort();
        String contextPath = serverProperties.getServlet().getContextPath();
        String baseUrl = URLUtil.normalize("%s:%s%s".formatted(hostAddress, port, contextPath));

        log.info("""
                
                ----------------------------------------------------------
                Application '{}' is running! Access URLs:
                Api Address: {}
                Doc Address: {}/doc.html
                ----------------------------------------------------------

                """, projectProperties.getName(), baseUrl, baseUrl);
    }
}
