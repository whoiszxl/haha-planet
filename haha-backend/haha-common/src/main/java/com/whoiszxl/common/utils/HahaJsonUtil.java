package com.whoiszxl.common.utils;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.List;

/**
 * JSON工具类 - 基于hutool JSONUtil的封装
 * 提供更简洁的JSON处理方法
 * 
 * @author whoiszxl
 */
@Slf4j
public class HahaJsonUtil {


    // ================================ 对象转JSON字符串 ================================

    /**
     * 将对象转换为JSON字符串
     *
     * @param obj 要转换的对象
     * @return JSON字符串，如果对象为null则返回null
     */
    public static String toJson(Object obj) {
        try {
            return JSONUtil.toJsonStr(obj);
        } catch (Exception e) {
            log.error("对象转JSON字符串失败", e);
            return null;
        }
    }

    /**
     * 将对象转换为格式化的JSON字符串（带缩进）
     *
     * @param obj 要转换的对象
     * @return 格式化的JSON字符串
     */
    public static String toJsonPretty(Object obj) {
        try {
            return JSONUtil.toJsonPrettyStr(obj);
        } catch (Exception e) {
            log.error("对象转格式化JSON字符串失败", e);
            return null;
        }
    }

    /**
     * 将对象转换为JSON字符串并写入Writer
     *
     * @param obj    要转换的对象
     * @param writer 写入目标
     */
    public static void toJson(Object obj, Writer writer) {
        try {
            JSONUtil.toJsonStr(obj, writer);
        } catch (Exception e) {
            log.error("对象转JSON写入Writer失败", e);
        }
    }

    // ================================ JSON字符串转对象 ================================

    /**
     * JSON字符串转换为指定类型的对象
     *
     * @param <T>       目标类型
     * @param jsonStr   JSON字符串
     * @param beanClass 目标类型Class
     * @return 转换后的对象，转换失败返回null
     */
    public static <T> T fromJson(String jsonStr, Class<T> beanClass) {
        if(jsonStr == null || jsonStr.isEmpty()) {
            return null;
        }
        try {
            return JSONUtil.toBean(jsonStr, beanClass);
        } catch (Exception e) {
            log.error("JSON字符串转对象失败，JSON: {}, 目标类型: {}", jsonStr, beanClass.getName(), e);
            return null;
        }
    }

    /**
     * JSON字符串转换为指定类型的对象（支持泛型）
     *
     * @param <T>           目标类型
     * @param jsonStr       JSON字符串
     * @param typeReference 类型引用
     * @return 转换后的对象，转换失败返回null
     */
    public static <T> T fromJson(String jsonStr, TypeReference<T> typeReference) {
        try {
            return JSONUtil.toBean(jsonStr, typeReference, false);
        } catch (Exception e) {
            log.error("JSON字符串转泛型对象失败，JSON: {}", jsonStr, e);
            return null;
        }
    }

    /**
     * JSON字符串转换为指定类型的对象，支持忽略错误
     *
     * @param <T>         目标类型
     * @param jsonStr     JSON字符串
     * @param beanType    目标类型
     * @param ignoreError 是否忽略转换错误
     * @return 转换后的对象
     */
    public static <T> T fromJson(String jsonStr, Type beanType, boolean ignoreError) {
        try {
            return JSONUtil.toBean(jsonStr, beanType, ignoreError);
        } catch (Exception e) {
            if (!ignoreError) {
                log.error("JSON字符串转对象失败，JSON: {}, 目标类型: {}", jsonStr, beanType.getTypeName(), e);
            }
            return null;
        }
    }

    // ================================ JSON数组处理 ================================

    /**
     * JSON数组字符串转换为List
     *
     * @param <T>         元素类型
     * @param jsonArray   JSON数组字符串
     * @param elementType 元素类型Class
     * @return List对象，转换失败返回null
     */
    public static <T> List<T> fromJsonArray(String jsonArray, Class<T> elementType) {
        try {
            return JSONUtil.toList(jsonArray, elementType);
        } catch (Exception e) {
            log.error("JSON数组转List失败，JSON: {}, 元素类型: {}", jsonArray, elementType.getName(), e);
            return null;
        }
    }

    /**
     * JSONArray对象转换为List
     *
     * @param <T>         元素类型
     * @param jsonArray   JSONArray对象
     * @param elementType 元素类型Class
     * @return List对象
     */
    public static <T> List<T> fromJsonArray(JSONArray jsonArray, Class<T> elementType) {
        try {
            return JSONUtil.toList(jsonArray, elementType);
        } catch (Exception e) {
            log.error("JSONArray转List失败，元素类型: {}", elementType.getName(), e);
            return null;
        }
    }

    // ================================ JSON对象创建和解析 ================================

    /**
     * 创建空的JSONObject
     *
     * @return JSONObject实例
     */
    public static JSONObject createObject() {
        return JSONUtil.createObj();
    }

    /**
     * 创建空的JSONArray
     *
     * @return JSONArray实例
     */
    public static JSONArray createArray() {
        return JSONUtil.createArray();
    }

    /**
     * 解析JSON字符串为JSONObject
     *
     * @param jsonStr JSON字符串
     * @return JSONObject，解析失败返回null
     */
    public static JSONObject parseObject(String jsonStr) {
        try {
            return JSONUtil.parseObj(jsonStr);
        } catch (Exception e) {
            log.error("解析JSON字符串为JSONObject失败，JSON: {}", jsonStr, e);
            return null;
        }
    }

    /**
     * 解析JSON字符串为JSONArray
     *
     * @param jsonStr JSON字符串
     * @return JSONArray，解析失败返回null
     */
    public static JSONArray parseArray(String jsonStr) {
        try {
            return JSONUtil.parseArray(jsonStr);
        } catch (Exception e) {
            log.error("解析JSON字符串为JSONArray失败，JSON: {}", jsonStr, e);
            return null;
        }
    }

    /**
     * 将对象解析为JSONObject
     *
     * @param obj 要解析的对象
     * @return JSONObject
     */
    public static JSONObject parseObject(Object obj) {
        try {
            return JSONUtil.parseObj(obj);
        } catch (Exception e) {
            log.error("对象解析为JSONObject失败", e);
            return null;
        }
    }

    /**
     * 将对象解析为JSONArray
     *
     * @param arrayOrCollection 数组或集合对象
     * @return JSONArray
     */
    public static JSONArray parseArray(Object arrayOrCollection) {
        try {
            return JSONUtil.parseArray(arrayOrCollection);
        } catch (Exception e) {
            log.error("对象解析为JSONArray失败", e);
            return null;
        }
    }

    // ================================ 文件操作 ================================

    /**
     * 从文件读取JSON
     *
     * @param file    JSON文件
     * @param charset 编码
     * @return JSON对象，读取失败返回null
     */
    public static JSON readJson(File file, Charset charset) {
        try {
            return JSONUtil.readJSON(file, charset);
        } catch (Exception e) {
            log.error("从文件读取JSON失败，文件: {}", file.getPath(), e);
            return null;
        }
    }

    /**
     * 从文件读取JSONObject
     *
     * @param file    JSON文件
     * @param charset 编码
     * @return JSONObject，读取失败返回null
     */
    public static JSONObject readJsonObject(File file, Charset charset) {
        try {
            return JSONUtil.readJSONObject(file, charset);
        } catch (Exception e) {
            log.error("从文件读取JSONObject失败，文件: {}", file.getPath(), e);
            return null;
        }
    }

    /**
     * 从文件读取JSONArray
     *
     * @param file    JSON文件
     * @param charset 编码
     * @return JSONArray，读取失败返回null
     */
    public static JSONArray readJsonArray(File file, Charset charset) {
        try {
            return JSONUtil.readJSONArray(file, charset);
        } catch (Exception e) {
            log.error("从文件读取JSONArray失败，文件: {}", file.getPath(), e);
            return null;
        }
    }

    // ================================ 路径表达式操作 ================================

    /**
     * 通过路径表达式获取JSON中嵌套的对象
     * <p>
     * 表达式示例：
     * - person.name
     * - persons[3]
     * - person.friends[5].name
     *
     * @param json       JSON对象
     * @param expression 路径表达式
     * @return 获取到的对象，未找到返回null
     */
    public static Object getByPath(JSON json, String expression) {
        try {
            return JSONUtil.getByPath(json, expression);
        } catch (Exception e) {
            log.error("通过路径表达式获取JSON值失败，表达式: {}", expression, e);
            return null;
        }
    }

    /**
     * 通过路径表达式获取JSON中嵌套的对象，支持默认值
     *
     * @param <T>          值类型
     * @param json         JSON对象
     * @param expression   路径表达式
     * @param defaultValue 默认值
     * @return 获取到的对象，未找到返回默认值
     */
    public static <T> T getByPath(JSON json, String expression, T defaultValue) {
        try {
            return JSONUtil.getByPath(json, expression, defaultValue);
        } catch (Exception e) {
            log.error("通过路径表达式获取JSON值失败，表达式: {}", expression, e);
            return defaultValue;
        }
    }

    /**
     * 设置路径表达式指定位置的值
     *
     * @param json       JSON对象
     * @param expression 路径表达式
     * @param value      要设置的值
     */
    public static void putByPath(JSON json, String expression, Object value) {
        try {
            JSONUtil.putByPath(json, expression, value);
        } catch (Exception e) {
            log.error("通过路径表达式设置JSON值失败，表达式: {}", expression, e);
        }
    }

    // ================================ 工具方法 ================================

    /**
     * 判断字符串是否为JSON格式
     *
     * @param str 字符串
     * @return 是否为JSON格式
     */
    public static boolean isJson(String str) {
        return JSONUtil.isTypeJSON(str);
    }

    /**
     * 判断字符串是否为JSONObject格式
     *
     * @param str 字符串
     * @return 是否为JSONObject格式
     */
    public static boolean isJsonObject(String str) {
        return JSONUtil.isTypeJSONObject(str);
    }

    /**
     * 判断字符串是否为JSONArray格式
     *
     * @param str 字符串
     * @return 是否为JSONArray格式
     */
    public static boolean isJsonArray(String str) {
        return JSONUtil.isTypeJSONArray(str);
    }

    /**
     * 判断对象是否为null（包括JSONNull）
     *
     * @param obj 对象
     * @return 是否为null
     */
    public static boolean isNull(Object obj) {
        return JSONUtil.isNull(obj);
    }

    /**
     * 格式化JSON字符串
     *
     * @param jsonStr JSON字符串
     * @return 格式化后的JSON字符串
     */
    public static String format(String jsonStr) {
        try {
            return JSONUtil.formatJsonStr(jsonStr);
        } catch (Exception e) {
            log.error("格式化JSON字符串失败，JSON: {}", jsonStr, e);
            return jsonStr;
        }
    }

    /**
     * 对字符串进行JSON转义
     *
     * @param str 要转义的字符串
     * @return 转义后的字符串
     */
    public static String quote(String str) {
        return JSONUtil.quote(str);
    }

    /**
     * XML转JSONObject
     *
     * @param xml XML字符串
     * @return JSONObject，转换失败返回null
     */
    public static JSONObject xmlToJson(String xml) {
        try {
            return JSONUtil.xmlToJson(xml);
        } catch (Exception e) {
            log.error("XML转JSON失败，XML: {}", xml, e);
            return null;
        }
    }
}
