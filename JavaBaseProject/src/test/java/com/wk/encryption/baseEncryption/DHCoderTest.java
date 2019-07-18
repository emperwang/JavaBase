package com.wk.encryption.baseEncryption;

import org.junit.Test;

import java.util.Map;

public class DHCoderTest {
    @Test
    public void test() throws Exception{
        // 生成甲方密钥对
        Map<String, Object> akeyMap = DHCoder.initKey();
        String aPublickey = DHCoder.getPublicKey(akeyMap);
        String aPrivateKey = DHCoder.getPrivateKey(akeyMap);
        System.out.println("甲方公钥:"+aPublickey);
        System.out.println("甲方密钥:"+aPrivateKey);
        // 由甲方公钥产生本地密钥对
        Map<String, Object> bkeyMap = DHCoder.initKey(aPublickey);
        String bpublicKey = DHCoder.getPublicKey(bkeyMap);
        String bprivateKey = DHCoder.getPrivateKey(bkeyMap);
        System.out.println("乙方公钥:"+bpublicKey);
        System.out.println("乙方私钥:"+bprivateKey);

        String aInput = "abc";

        // 由甲方公钥，乙方私钥构建密文
        byte[] encrypt = DHCoder.encrypt(aInput.getBytes(), aPublickey, bprivateKey);

        // 由乙方公钥，甲方私钥解密
        byte[] decript = DHCoder.decript(encrypt, bpublicKey, aPrivateKey);

        System.out.println("解密:"+new String(decript));
        System.out.println("=====================反过来加解密================================");
        // 由乙方公钥，甲方私钥构建密文
        byte[] encrypt1 = DHCoder.encrypt(aInput.getBytes(), bpublicKey, aPrivateKey);

        // 甲方公钥,乙方私钥解密
        byte[] decript1 = DHCoder.decript(encrypt1, aPublickey, bprivateKey);
        System.out.println("解密:"+new String(decript1));
    }
}
