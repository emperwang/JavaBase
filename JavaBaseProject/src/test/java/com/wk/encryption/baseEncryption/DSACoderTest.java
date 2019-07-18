package com.wk.encryption.baseEncryption;

import org.junit.Test;

import java.util.Map;

public class DSACoderTest {

    @Test
    public void test() throws Exception{
        String inputStr = "abc";
        byte[] data = inputStr.getBytes();
        // 构建密钥
        Map<String, Object> keyMap = DSACoder.initKey();
        // 获得密钥
        String privateKey = DSACoder.getPrivateKey(keyMap);
        String publicKey = DSACoder.getPublicKey(keyMap);
        System.out.println("公钥:"+publicKey);
        System.out.println("私钥:"+privateKey);
        // 产生签名
        String sign = DSACoder.sign(data, privateKey);
        System.out.println("签名:"+sign);
        // 验证签名
        boolean verify = DSACoder.verify(data, publicKey, sign);
        System.out.println("验证:"+verify);
    }
}
