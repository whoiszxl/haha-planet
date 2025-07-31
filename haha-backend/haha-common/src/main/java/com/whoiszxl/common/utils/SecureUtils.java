package com.whoiszxl.common.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.lang.Assert;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import com.whoiszxl.common.properties.RsaProperties;
import com.whoiszxl.starter.core.utils.validate.ValidationUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author whoiszxl
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecureUtils {

    /**
     * 使用公钥加密并base64
     * @param data 需要加密的数据
     * @param publicKey 公钥
     * @return 加密后的值
     */
    public static String encryptByRsaPublicKey(String data, String publicKey) {
        return Base64.encode(SecureUtil.rsa(null, publicKey).encrypt(data, KeyType.PublicKey));
    }

    /**
     * 通过私钥进行解密
     * @param data 需要解密的数据
     * @param privateKey 私钥
     * @return 解密的结果
     */
    public static String decryptByRsaPrivateKey(String data, String privateKey) {
        return new String(SecureUtil.rsa(privateKey, null).decrypt(Base64.decode(data), KeyType.PrivateKey));
    }

    public static String decryptByRsaPrivateKey(String data) {
        String privateKey = RsaProperties.PRIVATE_KEY;
        ValidationUtils.throwIfBlank(privateKey, "RSA 私钥未配置");
        return decryptByRsaPrivateKey(data, privateKey);
    }

    public static String passwordMd5(String data, String salt) {
        return SecureUtil.md5(
                SecureUtil.md5(SecureUtil.md5(data) + salt) + salt
        );
    }


    public static void main(String[] args) {

        String s = encryptByRsaPublicKey("123456", "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA183EzqDs126Mf7mQmnvZ" +
                "ni95tmZIeHImKUJaogYEMXXr2AioadOvGhbZA0ID5xgI1aSoAqoqQrDRvgk0Jur0" +
                "7NybtPVZtSrfMIR45uMcPEaUAuI8kNhmpWYduj6+zteKmTAF96JM0OgtIWe8TYYK" +
                "F6HL7Fmui7tZwbleVQn9o3BihmXbOckmlAU6UFsCBMU0Dk5LkTMBX/hLpE3Dr1Jp" +
                "IkmOzgO0gKyStbooy6ifDBxy5juSy7VMrc3pEoa/+mSs3ei0WqcArwt2hyySOvnA" +
                "bCAa9f2TR14eIRTuBEiH+SkAPN0+t5kl1nQNcxy+E3SVwYQF8Za6VzvXDhTo7Xux" +
                "uQIDAQAB");

        System.out.println(s);

    }

}
