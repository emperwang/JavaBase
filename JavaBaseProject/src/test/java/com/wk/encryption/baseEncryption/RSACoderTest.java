package com.wk.encryption.baseEncryption;

/**
 *  简要总结一下，使用公钥加密、私钥解密，完成了乙方到甲方的一次数据传递，
 *  通过私钥加密、公钥解密，同时通过私钥签名、公钥验证签名，
 *  完成了一次甲方到乙方的数据传递与验证，两次数据传递完成一整套的数据交互！
 */

import org.junit.Before;
import org.junit.Test;

import java.util.Map;

/**
 * 数字信封
 　　数字信封用加密技术来保证只有特定的收信人才能阅读信的内容。
 流程：
 信息发送方采用对称密钥来加密信息，然后再用接收方的公钥来加密此对称密钥（这部分称为数字信封），
 再将它和信息一起发送给接收方；接收方先用相应的私钥打开数字信封，得到对称密钥，然后使用对称密钥再解开信息。
 */
public class RSACoderTest {
    private String publicKey;
    private String privateKey;

    @Before
    public void setUp() throws Exception {
        Map<String, Object> keyMap = RSACoder.initKey();
        privateKey = RSACoder.getPrivateKey(keyMap);
        publicKey = RSACoder.getPublicKey(keyMap);
        System.out.println("公钥:"+publicKey);
        System.out.println("私钥:"+privateKey);
    }

    @Test
    public void test() throws Exception{
        System.out.println("公钥加密---私钥解密");
        String inputStr = "abc";
        byte[] data = inputStr.getBytes();
        byte[] bytes = RSACoder.encryptByPublicKey(data, publicKey);
        byte[] decrypt = RSACoder.decryptByPrivateKey(bytes, privateKey);
        String outputStr = new String(decrypt);
        System.out.println("加密前:"+inputStr +",,解密后:"+outputStr);
    }

    @Test
    public void testSign() throws Exception{
        System.out.println("私钥加密---公钥解密");
        String inputStr = "sign";
        byte[] data = inputStr.getBytes();

        byte[] encryptData = RSACoder.encryptByPrivateKey(data, privateKey);

        byte[] decrypt = RSACoder.decryptByPublicKey(encryptData, publicKey);
        System.out.println("加密前:"+inputStr +",,解密后:"+new String(decrypt));

        System.out.println("私钥签名---公钥验证签名");
        // 产生签名
        String sign = RSACoder.sign(encryptData, privateKey);
        System.out.println("签名:"+sign);
        // 验证签名
        boolean verify = RSACoder.verify(encryptData, publicKey, sign);
        System.out.println("验证结果:"+verify);

    }
}
