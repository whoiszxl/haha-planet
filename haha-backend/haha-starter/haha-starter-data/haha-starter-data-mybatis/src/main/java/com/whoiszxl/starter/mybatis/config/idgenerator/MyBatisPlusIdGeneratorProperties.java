package com.whoiszxl.starter.mybatis.config.idgenerator;

import com.whoiszxl.starter.mybatis.enums.MyBatisPlusIdGeneratorType;
import lombok.Data;

/**
 * MyBatis ID 生成器配置属性
 */
@Data
public class MyBatisPlusIdGeneratorProperties {

    /**
     * ID 生成器类型
     */
    private MyBatisPlusIdGeneratorType type = MyBatisPlusIdGeneratorType.DEFAULT;
    
}
