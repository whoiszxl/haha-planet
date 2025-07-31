package com.whoiszxl.starter.core.utils;

import cn.hutool.core.util.StrUtil;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.lang.Nullable;

import java.io.IOException;

/**
 * 自定义 yaml 配置读取工厂
 * 扩展默认的属性源工厂，使其能够处理 YAML 文件，并将 YAML 文件的内容加载为 Spring 的属性源。
 * 如果资源不是 YAML 文件，则使用默认的属性源加载方式。
 */
public class CustomPropertySourceFactory extends DefaultPropertySourceFactory {

    static String[] suffixes = new String[]{".yml", ".yaml"};

    @Override
    public PropertySource<?> createPropertySource(@Nullable String name, EncodedResource encodedResource) throws IOException {
        // 获取资源对象和资源的文件名
        Resource resource = encodedResource.getResource();
        String filename = resource.getFilename();
        // 判断文件名是否以 yml 和 yaml 结尾，如果是则使用 YAML 读取器读取
        if(StrUtil.isNotBlank(filename) && StrUtil.endWithAny(filename, suffixes)) {
            return new YamlPropertySourceLoader().load(filename, resource).get(0);
        }
        // 否则使用默认的属性源加载方式
        return super.createPropertySource(name, encodedResource);
    }
}
