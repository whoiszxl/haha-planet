package com.whoiszxl.starter.security.crypto.enums;

import com.whoiszxl.starter.security.crypto.encryptor.*;
import lombok.Getter;

/**
 * 加密/解密算法枚举
 */
@Getter
public enum Algorithm {

    /**
     * AES
     */
    AES(AesEncryptor.class),

    /**
     * DES
     */
    DES(DesEncryptor.class),

    /**
     * PBEWithMD5AndDES
     */
    PBEWithMD5AndDES(PbeWithMd5AndDesEncryptor.class),

    /**
     * RSA
     */
    RSA(RsaEncryptor.class),

    /**
     * Base64
     */
    BASE64(Base64Encryptor.class),;

    /**
     * 加密/解密处理器
     */
    private final Class<? extends IEncryptor> encryptor;

    Algorithm(Class<? extends IEncryptor> encryptor) {
        this.encryptor = encryptor;
    }

}
