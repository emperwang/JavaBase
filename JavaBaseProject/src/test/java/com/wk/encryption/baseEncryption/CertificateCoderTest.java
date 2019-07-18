package com.wk.encryption.baseEncryption;

import org.junit.Test;

/**
 * D:\Programe_files\java8\JDK\bin>keytool.exe -genkey -validity 360 -alias www.wk.
 org -keyalg RSA -keystore d:\wk.keystore
 输入密钥库口令:
 再次输入新口令:
 您的名字与姓氏是什么?
 [Unknown]:  wk
 您的组织单位名称是什么?
 [Unknown]:  enper
 您的组织名称是什么?
 [Unknown]:  wkk
 您所在的城市或区域名称是什么?
 [Unknown]:  bj
 您所在的省/市/自治区名称是什么?
 [Unknown]:  bj
 该单位的双字母国家/地区代码是什么?
 [Unknown]:  CN
 CN=wk, OU=enper, O=wkk, L=bj, ST=bj, C=CN是否正确?
 [否]:  y

 输入 <www.wk.org> 的密钥口令
 (如果和密钥库口令相同, 按回车): 123456
 再次输入新口令:	123456
 */
public class CertificateCoderTest {
    private String password="123456";
    private String alias = "www.wk.org";
    private String certificatePath = "D:/wk.cer";
    private String keyStorePath = "D:/wk.keystore";
    @Test
    public void test() throws Exception{
        System.out.println("公钥加密----私钥解密");
        String inputStr = "Certificate";
        byte[] data = inputStr.getBytes();

        byte[] encryptData = CertificateCoder.encryptByPublicKey(data,certificatePath);
        byte[] decryptData = CertificateCoder.decryptByPrivateKey(encryptData, keyStorePath, alias, password);
        System.out.println("解密后:"+new String(decryptData));
    }
    @Test
    public void testSign() throws Exception{
        System.out.println("私钥加密---公钥解密");
        String inputStr = "sign";
        byte[] data = inputStr.getBytes();
        byte[] encryptByPrivateKey = CertificateCoder.encryptByPrivateKey(data, keyStorePath, alias, password);
        byte[] decryptByPublicKey = CertificateCoder.decryptByPublicKey(encryptByPrivateKey, certificatePath);
        System.out.println("解密后:"+new String(decryptByPublicKey));

        System.out.println("私钥签名---公钥验证签名");
        // 产生签名
        String sign = CertificateCoder.sign(encryptByPrivateKey, keyStorePath, alias, password);
        System.out.println("签名:"+sign);
        // 验证签名
        boolean b = CertificateCoder.verifySign(encryptByPrivateKey, sign, certificatePath);
        System.out.println("验证签名:"+b);
    }
}
