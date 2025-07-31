package com.whoiszxl.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付状态枚举
 */
@Getter
@AllArgsConstructor
public enum TradeChannelEnum {

    ALI_PAY(1, "支付宝"),
    WEIXIN_PAY(2, "微信");

    /**
     * 状态码
     */
    private final Integer code;

    /**
     * 状态描述
     */
    private final String desc;

    /**
     * 通过状态码获取枚举
     * @param code 状态码
     * @return 支付状态枚举
     */
    public static TradeChannelEnum getByCode(Integer code) {
        for (TradeChannelEnum value : TradeChannelEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}