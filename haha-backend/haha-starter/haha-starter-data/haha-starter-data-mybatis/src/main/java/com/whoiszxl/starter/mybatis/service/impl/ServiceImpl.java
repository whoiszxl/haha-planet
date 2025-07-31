package com.whoiszxl.starter.mybatis.service.impl;

import cn.hutool.core.util.ClassUtil;
import com.whoiszxl.starter.core.utils.ReflectUtils;
import com.whoiszxl.starter.core.utils.validate.CheckUtils;
import com.whoiszxl.starter.mybatis.base.BaseMapper;
import com.whoiszxl.starter.mybatis.service.IService;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

/**
 * 通用业务实现类
 *
 * @param <M> Mapper 接口
 * @param <T> 实体类型
 */
public class ServiceImpl<M extends BaseMapper<T>, T> extends com.baomidou.mybatisplus.extension.service.impl.ServiceImpl<M, T> implements IService<T> {

    protected final List<Field> entityFields = ReflectUtils.getNonStaticFields(this.entityClass);

    @Override
    public T getById(Serializable id) {
        return this.getById(id, true);
    }

    /**
     * 根据 ID 查询
     *
     * @param id            ID
     * @param isCheckExists 是否检查存在
     * @return 实体信息
     */
    protected T getById(Serializable id, boolean isCheckExists) {
        T entity = baseMapper.selectById(id);
        if (isCheckExists) {
            CheckUtils.throwIfNotExists(entity, ClassUtil.getClassName(entityClass, true), "ID", id);
        }
        return entity;
    }
}