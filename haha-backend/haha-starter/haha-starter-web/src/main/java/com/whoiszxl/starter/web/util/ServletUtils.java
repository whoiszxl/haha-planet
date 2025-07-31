package com.whoiszxl.starter.web.util;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * Servlet 工具类
 */
public class ServletUtils {

    private ServletUtils() {}

    public static HttpServletRequest getRequest() {
        return getServletRequestAttributes().getRequest();
    }

    public static HttpServletResponse getResponse() {
        return getServletRequestAttributes().getResponse();
    }

    /**
     * 获取客户端浏览器的信息。
     * 该方法通过解析HTTP请求头中的User-Agent字段来判断客户端使用的浏览器。
     *
     * @param request HttpServletRequest对象，用于获取HTTP请求信息。
     * @return 返回客户端浏览器的信息字符串；如果传入的请求对象为null，则返回null。
     */
    public static String getBrowser(HttpServletRequest request) {
        if (null == request) {
            return null;
        }
        return getBrowser(request.getHeader("User-Agent"));
    }

    /**
     * 通过用户代理字符串获取浏览器信息。
     *
     * @param userAgentString 用户代理字符串，通常来源于HTTP请求头，用于识别客户端的浏览器类型和版本。
     * @return 返回浏览器名称和版本号的字符串，格式为“浏览器名称 版本号”。
     */
    public static String getBrowser(String userAgentString) {
        UserAgent userAgent = UserAgentUtil.parse(userAgentString);
        return userAgent.getBrowser().getName() + StrUtil.SPACE + userAgent.getVersion();
    }

    /**
     * 获取客户端操作系统的信息。
     * 该方法通过解析HTTP请求头中的User-Agent信息来推断客户端的操作系统。
     *
     * @param request 客户端的HTTP请求对象。如果请求对象为null，则直接返回null。
     * @return 客户端操作系统的字符串表示。如果无法确定操作系统或输入的请求对象为null，则返回null。
     */
    public static String getOs(HttpServletRequest request) {
        if (null == request) {
            return null;
        }
        return getOs(request.getHeader("User-Agent"));
    }

    /**
     * 从用户代理字符串中获取操作系统名称。
     *
     * @param userAgentString 用户代理字符串，通常来源于HTTP请求头，用于识别发起请求的客户端操作系统。
     * @return 操作系统名称，例如Windows、Mac、Linux等。
     */
    public static String getOs(String userAgentString) {
        UserAgent userAgent = UserAgentUtil.parse(userAgentString);
        return userAgent.getOs().getName();
    }

    /**
     * 获取当前线程绑定的Servlet请求属性。
     * 这个方法通过调用RequestContextHolder的getRequestAttributes方法来获取请求属性，
     * 并且强制转换为ServletRequestAttributes类型。
     *
     * @return ServletRequestAttributes 当前线程的Servlet请求属性对象。
     *         该对象不可为null，如果请求上下文不存在，则抛出NullPointerException。
     */
    private static ServletRequestAttributes getServletRequestAttributes() {
        return (ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes());
    }

    public static Map<String, String> getHeaderMap(HttpServletResponse response) {
        final Collection<String> headerNames = response.getHeaderNames();
        final Map<String, String> headerMap = MapUtil.newHashMap(headerNames.size(), true);
        for (String name : headerNames) {
            headerMap.put(name, response.getHeader(name));
        }
        return headerMap;
    }
}
