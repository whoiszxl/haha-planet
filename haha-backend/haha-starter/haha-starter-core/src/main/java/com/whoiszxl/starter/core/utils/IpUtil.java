package com.whoiszxl.starter.core.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.HtmlUtil;
import com.whoiszxl.starter.core.constants.StringConstants;
import net.dreamlu.mica.ip2region.core.Ip2regionSearcher;
import net.dreamlu.mica.ip2region.core.IpInfo;

import java.util.Set;

/**
 * @author whoiszxl
 */
public class IpUtil {
    private IpUtil() {}

    /**
     * 根据IP地址获取地址信息。
     *
     * @param ip 输入的IP地址字符串。
     * @return 如果是内网IP，则返回"内网IP"；如果能够查询到外网IP的信息，则返回包含地区和ISP的信息字符串；如果查询不到任何信息，则返回null。
     */
    public static String getAddress(String ip) {
        // 检查是否为内网IP
        if (isInnerIp(ip)) {
            return "内网IP";
        }
        // 通过Spring工具类获取Ip2regionSearcher实例，用于查询IP信息
        Ip2regionSearcher ip2regionSearcher = SpringUtil.getBean(Ip2regionSearcher.class);
        // 使用Ip2regionSearcher查询IP信息
        IpInfo ipInfo = ip2regionSearcher.memorySearch(ip);
        if (null != ipInfo) {
            // 构建地区和ISP的集合，去除空值
            Set<String> regionSet = CollUtil.newLinkedHashSet(ipInfo.getAddress(), ipInfo.getIsp());
            regionSet.removeIf(CharSequenceUtil::isBlank);
            // 将地区和ISP信息合并为一个字符串，返回
            return String.join(StringConstants.S_SPACE, regionSet);
        }
        // 如果查询不到任何信息，返回null
        return null;
    }

    /**
     * 检查提供的IP地址是否为内部IP地址。
     *
     * @param ip 待检查的IP地址，可以是IPv4或IPv6格式的字符串。
     * @return 如果IP地址是内部IP地址，则返回true；否则返回false。
     */
    public static boolean isInnerIp(String ip) {
        return NetUtil.isInnerIP("0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : HtmlUtil.cleanHtmlTag(ip));
    }
}
