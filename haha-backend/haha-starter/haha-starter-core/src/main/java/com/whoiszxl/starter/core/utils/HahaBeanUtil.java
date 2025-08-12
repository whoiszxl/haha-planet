package com.whoiszxl.starter.core.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.DynaBean;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.bean.copier.ValueProvider;
import cn.hutool.core.lang.Editor;
import lombok.extern.slf4j.Slf4j;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Bean工具类 - 基于hutool BeanUtil的封装
 * 提供更简洁的Bean操作方法，包含异常处理和中文日志
 *
 * @author whoiszxl
 */
@Slf4j
public class HahaBeanUtil {

    // ================================ 属性复制 ================================

    /**
     * 按照Bean对象属性创建对应的Class对象，并忽略某些属性
     *
     * @param <T>              对象类型
     * @param source           源Bean对象
     * @param tClass           目标Class
     * @param ignoreProperties 不拷贝的属性列表
     * @return 目标对象，复制失败返回null
     */
    public static <T> T copyProperties(Object source, Class<T> tClass, String... ignoreProperties) {
        try {
            return BeanUtil.copyProperties(source, tClass, ignoreProperties);
        } catch (Exception e) {
            log.error("Bean属性复制失败，目标类型: {}", tClass != null ? tClass.getName() : "null", e);
            return null;
        }
    }

    /**
     * 复制Bean对象属性
     *
     * @param source           源Bean对象
     * @param target           目标Bean对象
     * @param ignoreProperties 不拷贝的属性列表
     */
    public static void copyProperties(Object source, Object target, String... ignoreProperties) {
        try {
            BeanUtil.copyProperties(source, target, ignoreProperties);
        } catch (Exception e) {
            log.error("Bean属性复制失败", e);
        }
    }

    /**
     * 复制Bean对象属性
     *
     * @param source     源Bean对象
     * @param target     目标Bean对象
     * @param ignoreCase 是否忽略大小写
     */
    public static void copyProperties(Object source, Object target, boolean ignoreCase) {
        try {
            BeanUtil.copyProperties(source, target, ignoreCase);
        } catch (Exception e) {
            log.error("Bean属性复制失败", e);
        }
    }

    /**
     * 复制Bean对象属性
     *
     * @param source      源Bean对象
     * @param target      目标Bean对象
     * @param copyOptions 拷贝选项
     */
    public static void copyProperties(Object source, Object target, CopyOptions copyOptions) {
        try {
            BeanUtil.copyProperties(source, target, copyOptions);
        } catch (Exception e) {
            log.error("Bean属性复制失败", e);
        }
    }

    // ================================ 集合转换 ================================

    /**
     * 复制集合中的Bean属性
     *
     * @param collection  原Bean集合
     * @param targetType  目标Bean类型
     * @param copyOptions 拷贝选项
     * @param <T>         Bean类型
     * @return 复制后的List，转换失败返回null
     */
    public static <T> List<T> copyToList(Collection<?> collection, Class<T> targetType, CopyOptions copyOptions) {
        try {
            return BeanUtil.copyToList(collection, targetType, copyOptions);
        } catch (Exception e) {
            log.error("集合Bean属性复制失败，目标类型: {}", targetType != null ? targetType.getName() : "null", e);
            return null;
        }
    }

    /**
     * 复制集合中的Bean属性
     *
     * @param collection 原Bean集合
     * @param targetType 目标Bean类型
     * @param <T>        Bean类型
     * @return 复制后的List，转换失败返回null
     */
    public static <T> List<T> copyToList(Collection<?> collection, Class<T> targetType) {
        try {
            return BeanUtil.copyToList(collection, targetType);
        } catch (Exception e) {
            log.error("集合Bean属性复制失败，目标类型: {}", targetType != null ? targetType.getName() : "null", e);
            return null;
        }
    }

    // ================================ Bean转换 ================================

    /**
     * 对象或Map转Bean
     *
     * @param <T>    转换的Bean类型
     * @param source Bean对象或Map
     * @param clazz  目标的Bean类型
     * @return Bean对象，转换失败返回null
     */
    public static <T> T toBean(Object source, Class<T> clazz) {
        try {
            return BeanUtil.toBean(source, clazz);
        } catch (Exception e) {
            log.error("对象转Bean失败，目标类型: {}", clazz != null ? clazz.getName() : "null", e);
            return null;
        }
    }

    /**
     * 对象或Map转Bean，忽略字段转换时发生的异常
     *
     * @param <T>    转换的Bean类型
     * @param source Bean对象或Map
     * @param clazz  目标的Bean类型
     * @return Bean对象，转换失败返回null
     */
    public static <T> T toBeanIgnoreError(Object source, Class<T> clazz) {
        try {
            return BeanUtil.toBeanIgnoreError(source, clazz);
        } catch (Exception e) {
            log.error("对象转Bean失败（忽略错误模式），目标类型: {}", clazz != null ? clazz.getName() : "null", e);
            return null;
        }
    }

    /**
     * 对象或Map转Bean
     *
     * @param <T>     转换的Bean类型
     * @param source  Bean对象或Map
     * @param clazz   目标的Bean类型
     * @param options 属性拷贝选项
     * @return Bean对象，转换失败返回null
     */
    public static <T> T toBean(Object source, Class<T> clazz, CopyOptions options) {
        try {
            return BeanUtil.toBean(source, clazz, options);
        } catch (Exception e) {
            log.error("对象转Bean失败，目标类型: {}", clazz != null ? clazz.getName() : "null", e);
            return null;
        }
    }

    // ================================ Bean转Map ================================

    /**
     * 将bean的部分属性转换成map
     *
     * @param bean       bean对象
     * @param properties 需要拷贝的属性值，null或空表示拷贝所有值
     * @return Map，转换失败返回null
     */
    public static Map<String, Object> beanToMap(Object bean, String... properties) {
        try {
            return BeanUtil.beanToMap(bean, properties);
        } catch (Exception e) {
            log.error("Bean转Map失败", e);
            return null;
        }
    }

    /**
     * 对象转Map
     *
     * @param bean              bean对象
     * @param isToUnderlineCase 是否转换为下划线模式
     * @param ignoreNullValue   是否忽略值为空的字段
     * @return Map，转换失败返回null
     */
    public static Map<String, Object> beanToMap(Object bean, boolean isToUnderlineCase, boolean ignoreNullValue) {
        try {
            return BeanUtil.beanToMap(bean, isToUnderlineCase, ignoreNullValue);
        } catch (Exception e) {
            log.error("Bean转Map失败", e);
            return null;
        }
    }

    // ================================ 字段操作 ================================

    /**
     * 获得字段值，通过反射直接获得字段值
     *
     * @param bean             Bean对象
     * @param fieldNameOrIndex 字段名或序号
     * @return 字段值，获取失败返回null
     */
    public static Object getFieldValue(Object bean, String fieldNameOrIndex) {
        try {
            return BeanUtil.getFieldValue(bean, fieldNameOrIndex);
        } catch (Exception e) {
            log.error("获取字段值失败，字段: {}", fieldNameOrIndex, e);
            return null;
        }
    }

    /**
     * 设置字段值，通过反射设置字段值
     *
     * @param bean             Bean对象
     * @param fieldNameOrIndex 字段名或序号
     * @param value            值
     * @return bean，设置失败返回原对象
     */
    public static Object setFieldValue(Object bean, String fieldNameOrIndex, Object value) {
        try {
            return BeanUtil.setFieldValue(bean, fieldNameOrIndex, value);
        } catch (Exception e) {
            log.error("设置字段值失败，字段: {}", fieldNameOrIndex, e);
            return bean;
        }
    }

    // ================================ 工具方法 ================================

    /**
     * 判断是否为Bean对象
     *
     * @param clazz 待测试类
     * @return 是否为Bean对象
     */
    public static boolean isBean(Class<?> clazz) {
        try {
            return BeanUtil.isBean(clazz);
        } catch (Exception e) {
            log.error("判断是否为Bean失败，类型: {}", clazz != null ? clazz.getName() : "null", e);
            return false;
        }
    }

    /**
     * 判断Bean是否为空对象
     *
     * @param bean             Bean对象
     * @param ignoreFieldNames 忽略检查的字段名
     * @return 是否为空
     */
    public static boolean isEmpty(Object bean, String... ignoreFieldNames) {
        try {
            return BeanUtil.isEmpty(bean, ignoreFieldNames);
        } catch (Exception e) {
            log.error("判断Bean是否为空失败", e);
            return true;
        }
    }

    /**
     * 判断Bean是否为非空对象
     *
     * @param bean             Bean对象
     * @param ignoreFieldNames 忽略检查的字段名
     * @return 是否为非空
     */
    public static boolean isNotEmpty(Object bean, String... ignoreFieldNames) {
        return !isEmpty(bean, ignoreFieldNames);
    }

    /**
     * 创建动态Bean
     *
     * @param bean 普通Bean或Map
     * @return DynaBean，创建失败返回null
     */
    public static DynaBean createDynaBean(Object bean) {
        try {
            return BeanUtil.createDynaBean(bean);
        } catch (Exception e) {
            log.error("创建动态Bean失败", e);
            return null;
        }
    }

}
