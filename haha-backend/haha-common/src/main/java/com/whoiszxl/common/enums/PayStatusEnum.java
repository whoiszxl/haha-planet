package com.whoiszxl.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付状态枚举
 */
@Getter
@AllArgsConstructor
public enum PayStatusEnum {

    WAIT_PAY(1, "待支付"),
    PAY_SUCCESS(2, "支付成功"),
    PAY_FAIL(3, "支付失败"),
    TRADE_CLOSED(4, "交易关闭"),
    REFUND(5, "退款");

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
    public static PayStatusEnum getByCode(Integer code) {
        for (PayStatusEnum value : PayStatusEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}