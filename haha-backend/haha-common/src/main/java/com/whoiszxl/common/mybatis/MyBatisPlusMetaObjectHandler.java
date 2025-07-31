package com.whoiszxl.common.mybatis;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.whoiszxl.common.utils.LoginHelper;
import com.whoiszxl.starter.core.exception.ServiceException;
import com.whoiszxl.starter.crud.model.entity.BaseDO;
import org.apache.ibatis.reflection.MetaObject;
import java.time.LocalDateTime;

/**
 * MyBatis Plus 元对象处理器配置（插入或修改时自动填充）
 */
public class MyBatisPlusMetaObjectHandler implements MetaObjectHandler {

    /**
     * 创建人
     */
    private static final String CREATED_BY = "createdBy";
    /**
     * 创建时间
     */
    private static final String CREATED_AT = "createAt";
    /**
     * 修改人
     */
    private static final String UPDATED_BY = "updatedBy";
    /**
     * 修改时间
     */
    private static final String UPDATED_AT = "updatedAt";

    /**
     * 插入数据时填充
     *
     * @param metaObject 元对象
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        try {
            if (null == metaObject) {
                return;
            }

            Long createAdminId = LoginHelper.getAdminId();
            LocalDateTime createdAt = LocalDateTime.now();
            if (metaObject.getOriginalObject() instanceof BaseDO baseDO) {
                baseDO.setCreatedBy(ObjectUtil.defaultIfNull(baseDO.getCreatedBy(), createAdminId));
                baseDO.setCreatedAt(ObjectUtil.defaultIfNull(baseDO.getCreatedAt(), createdAt));
            }else {
                this.fillFieldValue(metaObject, CREATED_BY, createAdminId, false);
                this.fillFieldValue(metaObject, CREATED_AT, createdAt, false);
            }
        } catch (Exception e) {
            throw new ServiceException("插入数据时自动填充异常：" + e.getMessage());
        }
    }

    /**
     * 修改数据时填充
     *
     * @param metaObject 元对象
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        try {
            if (null == metaObject) {
                return;
            }

            Long updateAdminId = LoginHelper.getAdminId();
            LocalDateTime updateTime = LocalDateTime.now();
            if (metaObject.getOriginalObject() instanceof BaseDO baseDO) {
                // 继承了 BaseDO 的类，填充修改信息
                baseDO.setUpdatedBy(updateAdminId);
                baseDO.setUpdatedAt(updateTime);
            } else {
                // 未继承 BaseDO 的类，根据类中拥有的修改信息字段进行填充，不存在修改信息字段不进行填充
                this.fillFieldValue(metaObject, UPDATED_BY, updateAdminId, true);
                this.fillFieldValue(metaObject, UPDATED_AT, updateTime, true);
            }
        } catch (Exception e) {
            throw new ServiceException("修改数据时自动填充异常：" + e.getMessage());
        }
    }

    /**
     * 填充字段值
     *
     * @param metaObject     元数据对象
     * @param fieldName      要填充的字段名
     * @param fillFieldValue 要填充的字段值
     * @param isOverride     如果字段值不为空，是否覆盖（true：覆盖；false：不覆盖）
     */
    private void fillFieldValue(MetaObject metaObject, String fieldName, Object fillFieldValue, boolean isOverride) {
        if (metaObject.hasSetter(fieldName)) {
            Object fieldValue = metaObject.getValue(fieldName);
            setFieldValByName(fieldName, null != fieldValue && !isOverride ? fieldValue : fillFieldValue, metaObject);
        }
    }
}
