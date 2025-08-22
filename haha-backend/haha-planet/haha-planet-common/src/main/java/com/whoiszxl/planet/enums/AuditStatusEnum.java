package com.whoiszxl.planet.enums;

import com.whoiszxl.starter.mybatis.base.IBaseEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 审核状态枚举
 * @author whoiszxl
 */
@Getter
@RequiredArgsConstructor
public enum AuditStatusEnum implements IBaseEnum<Integer> {

    /**
     * 待审核
     */
    PENDING(1, "待审核", "#faad14"),

    /**
     * 审核通过
     */
    APPROVED(2, "审核通过", "#52c41a"),

    /**
     * 审核拒绝
     */
    REJECTED(3, "审核拒绝", "#ff4d4f");

    private final Integer value;
    private final String description;
    private final String color;

    /**
     * 根据值获取枚举
     */
    public static AuditStatusEnum getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (AuditStatusEnum status : values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        return null;
    }

    /**
     * 判断是否已审核通过
     */
    public boolean isApproved() {
        return this == APPROVED;
    }

    /**
     * 判断是否被拒绝
     */
    public boolean isRejected() {
        return this == REJECTED;
    }

    /**
     * 判断是否待审核
     */
    public boolean isPending() {
        return this == PENDING;
    }
}