package com.whoiszxl.common.mybatis;


import com.whoiszxl.starter.security.crypto.encryptor.IEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * BCrypt 加/解密处理器（不可逆）
 */
public class BCryptEncryptor implements IEncryptor {

    private final PasswordEncoder passwordEncoder;

    public BCryptEncryptor(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String encrypt(String plaintext, String password, String publicKey) throws Exception {
        return passwordEncoder.encode(plaintext);
    }

    @Override
    public String decrypt(String ciphertext, String password, String privateKey) throws Exception {
        return ciphertext;
    }
}
