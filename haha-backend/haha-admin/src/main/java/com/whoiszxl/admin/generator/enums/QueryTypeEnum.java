package com.whoiszxl.admin.generator.enums;

import com.whoiszxl.starter.mybatis.base.IBaseEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 查询类型枚举
 * @author whoiszxl
 */
@Getter
@RequiredArgsConstructor
public enum QueryTypeEnum implements IBaseEnum<Integer> {

    /**
     * 等于 =，例如：WHERE age = 18
     */
    EQ(1, "="),

    /**
     * 不等于 !=，例如：WHERE age != 18
     */
    NE(2, "!="),

    /**
     * 大于 >，例如：WHERE age > 18
     */
    GT(3, ">"),

    /**
     * 大于等于 >= ，例如：WHERE age >= 18
     */
    GE(4, ">="),

    /**
     * 小于 <，例如：WHERE age < 18
     */
    LT(5, "<"),

    /**
     * 小于等于 <=，例如：WHERE age <= 18
     */
    LE(6, "<="),

    /**
     * 范围查询，例如：WHERE age BETWEEN 10 AND 18
     */
    BETWEEN(7, "BETWEEN"),

    /**
     * LIKE '%值%'，例如：WHERE nickname LIKE '%s%'
     */
    LIKE(8, "LIKE '%s%'"),

    /**
     * LIKE '%值'，例如：WHERE nickname LIKE '%s'
     */
    LIKE_LEFT(9, "LIKE '%s'"),

    /**
     * LIKE '值%'，例如：WHERE nickname LIKE 's%'
     */
    LIKE_RIGHT(10, "LIKE 's%'"),

    /**
     * 包含查询，例如：WHERE age IN (10, 20, 30)
     */
    IN(11, "IN"),

    /**
     * 不包含查询，例如：WHERE age NOT IN (20, 30)
     */
    NOT_IN(12, "NOT IN"),

    /**
     * 空查询，例如：WHERE email IS NULL
     */
    IS_NULL(13, "IS NULL"),

    /**
     * 非空查询，例如：WHERE email IS NOT NULL
     */
    IS_NOT_NULL(14, "IS NOT NULL"),;

    private final Integer value;
    private final String description;
}
