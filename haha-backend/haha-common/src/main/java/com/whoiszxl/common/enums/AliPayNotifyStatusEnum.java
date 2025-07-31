package com.whoiszxl.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付状态枚举
 */
@Getter
@AllArgsConstructor
public enum AliPayNotifyStatusEnum {

    SUCCESS("success", "成功"),
    FAIL("fail", "失败");

    /**
     * 状态码
     */
    private final String code;

    /**
     * 状态描述
     */
    private final String desc;

    /**
     * 通过状态码获取枚举
     * @param code 状态码
     * @return 支付状态枚举
     */
    public static AliPayNotifyStatusEnum getByCode(String code) {
        for (AliPayNotifyStatusEnum value : AliPayNotifyStatusEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}