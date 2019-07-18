package com.wk.encryption.baseEncryption;


import org.junit.Test;

import java.util.Map;

public class ECCCoderTest{

    @Test
    public void test() throws Exception {
        String inputStr = "abc";
        byte[] data = inputStr.getBytes();
        // 初始化密钥
        Map<String, Object> keyMap = ECCCoder.initKey();
        String privateKey = ECCCoder.getPrivateKey(keyMap);
        String publicKey = ECCCoder.getPublicKey(keyMap);
        System.out.println("公钥:"+publicKey);
        System.out.println("私钥:"+privateKey);
        // 加密
        byte[] encrypt = ECCCoder.encrypt(data, publicKey);
        // 解密
        byte[] decrypt = ECCCoder.decrypt(encrypt, privateKey);
        System.out.println("解密后:"+new String(decrypt));
    }

}
