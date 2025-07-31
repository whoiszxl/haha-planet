package com.whoiszxl.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付宝交易状态枚举
 */
@Getter
@AllArgsConstructor
public enum AlipayTradeStatusEnum {

    TRADE_FINISHED(1, "TRADE_FINISHED", "交易完成", false),
    TRADE_SUCCESS(2, "TRADE_SUCCESS", "支付成功", true),
    WAIT_BUYER_PAY(3, "WAIT_BUYER_PAY", "交易创建", false),
    TRADE_CLOSED(4, "TRADE_CLOSED", "交易关闭", false);

    /**
     * 交易状态码
     */
    private final Integer code;

    /**
     * 交易结果
     */
    private final String statusResult;

    /**
     * 交易状态描述
     */
    private final String desc;

    /**
     * 是否触发通知
     */
    private final boolean notifyTrigger;

    /**
     * 通过状态码获取枚举
     * @param code 状态码
     * @return 交易状态枚举
     */
    public static AlipayTradeStatusEnum getByCode(String code) {
        if (code == null) {
            return null;
        }
        for (AlipayTradeStatusEnum value : AlipayTradeStatusEnum.values()) {
            if (value.getStatusResult().equals(code)) {
                return value;
            }
        }
        return null;
    }
}