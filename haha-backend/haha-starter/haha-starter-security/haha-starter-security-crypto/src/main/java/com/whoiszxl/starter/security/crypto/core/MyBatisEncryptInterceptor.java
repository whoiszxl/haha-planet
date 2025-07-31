package com.whoiszxl.starter.security.crypto.core;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.whoiszxl.starter.security.crypto.annotation.FieldEncrypt;
import com.whoiszxl.starter.security.crypto.config.CryptoProperties;
import com.whoiszxl.starter.security.crypto.encryptor.IEncryptor;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.SimpleTypeRegistry;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 字段加密拦截器
 */
@Intercepts({@Signature(method = "update", type = Executor.class, args = {MappedStatement.class, Object.class}),
    @Signature(method = "query", type = Executor.class, args = {MappedStatement.class, Object.class, RowBounds.class,
        ResultHandler.class, CacheKey.class, BoundSql.class}),
    @Signature(method = "query", type = Executor.class, args = {MappedStatement.class, Object.class, RowBounds.class,
        ResultHandler.class})})
public class MyBatisEncryptInterceptor extends AbstractMyBatisInterceptor {

    private CryptoProperties properties;

    public MyBatisEncryptInterceptor(CryptoProperties properties) {
        this.properties = properties;
    }

    public MyBatisEncryptInterceptor() {
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement)args[0];
        Object parameter = args[1];
        if (!this.isEncryptRequired(parameter, mappedStatement.getSqlCommandType())) {
            return invocation.proceed();
        }
        // 使用 @Param 注解的场景
        if (parameter instanceof HashMap parameterMap) {
            this.encryptMap(parameterMap, mappedStatement);
        } else {
            this.doEncrypt(this.getEncryptFields(parameter), parameter);
        }
        return invocation.proceed();
    }

    /**
     * 是否需要加密处理
     *
     * @param parameter      参数
     * @param sqlCommandType SQL 类型
     * @return true：是；false：否
     */
    private boolean isEncryptRequired(Object parameter, SqlCommandType sqlCommandType) {
        if (ObjectUtil.isEmpty(parameter)) {
            return false;
        }
        if (!(SqlCommandType.UPDATE == sqlCommandType || SqlCommandType.INSERT == sqlCommandType || SqlCommandType.SELECT == sqlCommandType)) {
            return false;
        }
        return !SimpleTypeRegistry.isSimpleType(parameter.getClass());
    }

    /**
     * 加密 Map 类型数据（使用 @Param 注解的场景）
     *
     * @param parameterMap    参数
     * @param mappedStatement 映射语句
     * @throws Exception /
     */
    private void encryptMap(HashMap<String, Object> parameterMap, MappedStatement mappedStatement) throws Exception {
        Map<String, FieldEncrypt> encryptParamMap = super.getEncryptParams(mappedStatement.getId());
        for (Map.Entry<String, FieldEncrypt> encryptParamEntry : encryptParamMap.entrySet()) {
            String parameterName = encryptParamEntry.getKey();
            if (parameterName.startsWith(Constants.ENTITY)) {
                // 兼容 MyBatis Plus 封装的 update 相关方法，updateById、update
                Object entity = parameterMap.getOrDefault(parameterName, null);
                this.doEncrypt(this.getEncryptFields(entity), entity);
            } else {
                FieldEncrypt fieldEncrypt = encryptParamEntry.getValue();
                parameterMap.put(parameterName, this.doEncrypt(parameterMap.get(parameterName), fieldEncrypt));
            }
        }
    }

    /**
     * 处理加密
     *
     * @param parameterValue 参数值
     * @param fieldEncrypt   字段加密注解
     * @throws Exception /
     */
    private Object doEncrypt(Object parameterValue, FieldEncrypt fieldEncrypt) throws Exception {
        if (null == parameterValue) {
            return null;
        }
        IEncryptor encryptor = super.getEncryptor(fieldEncrypt);
        // 优先获取自定义对称加密算法密钥，获取不到时再获取全局配置
        String password = ObjectUtil.defaultIfBlank(fieldEncrypt.password(), properties.getPassword());
        return encryptor.encrypt(parameterValue.toString(), password, properties.getPublicKey());
    }

    /**
     * 处理加密
     *
     * @param fieldList 加密字段列表
     * @param entity    实体
     * @throws Exception /
     */
    private void doEncrypt(List<Field> fieldList, Object entity) throws Exception {
        for (Field field : fieldList) {
            IEncryptor encryptor = super.getEncryptor(field.getAnnotation(FieldEncrypt.class));
            Object fieldValue = ReflectUtil.getFieldValue(entity, field);
            // 优先获取自定义对称加密算法密钥，获取不到时再获取全局配置
            String password = ObjectUtil.defaultIfBlank(field.getAnnotation(FieldEncrypt.class).password(), properties
                .getPassword());
            String ciphertext = encryptor.encrypt(fieldValue.toString(), password, properties.getPublicKey());
            ReflectUtil.setFieldValue(entity, field, ciphertext);
        }
    }
}
