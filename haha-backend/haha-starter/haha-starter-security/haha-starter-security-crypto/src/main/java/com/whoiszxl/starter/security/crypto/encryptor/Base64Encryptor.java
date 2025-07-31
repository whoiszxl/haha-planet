package com.whoiszxl.starter.security.crypto.encryptor;

import cn.hutool.core.codec.Base64;

/**
 * Base64 加/解密处理器
 */
public class Base64Encryptor implements IEncryptor {

    @Override
    public String encrypt(String plaintext, String password, String publicKey) throws Exception {
        return Base64.encode(plaintext);
    }

    @Override
    public String decrypt(String ciphertext, String password, String privateKey) throws Exception {
        return Base64.decodeStr(ciphertext);
    }
}
