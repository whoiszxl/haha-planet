package com.whoiszxl.common.utils;

import java.time.LocalDateTime;
import java.util.Calendar;

/**
 * 订单工具类
 *
 * @author whoiszxl
 * @date 2022/4/11
 */
public class OrderUtils {

    public static String makeOrderNo() {
        StringBuilder sb = new StringBuilder();
        Calendar calendar = Calendar.getInstance();
        String mills = String.valueOf(calendar.getTimeInMillis());
        String micro = LocalDateTime.now().toString();
        String random = String.valueOf(Math.random()*1000).substring(0,2);
        sb.append(calendar.get(Calendar.YEAR))
                .append(Integer.toHexString(calendar.get(Calendar.MONTH)+1).toUpperCase())
                .append(calendar.get(Calendar.DAY_OF_MONTH))
                .append(mills, mills.length()-5, mills.length())
                .append(micro, micro.length()-3, micro.length())
                .append(random);
        return sb.toString();

    }

    public static void main(String[] args) {
        System.out.println(makeOrderNo());
    }

}
