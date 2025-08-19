package com.whoiszxl.planet.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 星球成员类型枚举
 * @author whoiszxl
 */
@Getter
@AllArgsConstructor
public enum PlanetMemberTypeEnum {
    
    /**
     * 普通成员
     */
    NORMAL_MEMBER(1, "普通成员"),
    
    /**
     * 管理员
     */
    ADMIN(2, "管理员"),
    
    /**
     * 星球主
     */
    OWNER(3, "星球主");
    
    private final Integer code;
    private final String name;
    
    /**
     * 根据代码获取枚举
     */
    public static PlanetMemberTypeEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (PlanetMemberTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}
