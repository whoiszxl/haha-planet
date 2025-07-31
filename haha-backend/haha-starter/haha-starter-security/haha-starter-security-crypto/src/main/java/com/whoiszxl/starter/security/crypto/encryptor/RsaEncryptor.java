package com.whoiszxl.starter.security.crypto.encryptor;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;

/**
 * RSA 加/解密处理器
 */
public class RsaEncryptor implements IEncryptor {

    @Override
    public String encrypt(String plaintext, String password, String publicKey) throws Exception {
        return Base64.encode(SecureUtil.rsa(null, publicKey).encrypt(plaintext, KeyType.PublicKey));
    }

    @Override
    public String decrypt(String ciphertext, String password, String privateKey) throws Exception {
        return new String(SecureUtil.rsa(privateKey, null).decrypt(Base64.decode(ciphertext), KeyType.PrivateKey));
    }
}
