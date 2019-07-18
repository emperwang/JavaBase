package com.wk.encryption.baseEncryption;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NullCipher;
import java.io.IOException;
import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * ECC-Elliptic Curves Cryptography，椭圆曲线密码编码学，是目前已知的公钥体制中，对每比特所提供加密强度最高的一种体制。
 * 在软件注册保护方面起到很大的作用，一般的序列号通常由该算法产生。但是关于Java实现ECC算法的资料实在是太少了，无论是国内还是国外的资料，
 无论是官方还是非官方的解释，最终只有一种答案——ECC算法在jdk1.5后加入支持，目前仅仅只能完成密钥的生成与解析。 如果想要获得ECC算法实现，
 需要调用硬件完成加密/解密（ECC算法相当耗费资源，如果单纯使用CPU进行加密/解密，效率低下），
 涉及到Java Card领域，PKCS#11。 其实，PKCS#11配置很简单，但缺乏硬件设备，无法尝试！
 */
public class ECCCoder extends Coder{
    public static final String ALGORITHM ="EC";
    private static final String PUBLIC_KEY = "ECCPublicKey";
    private static final String PRIVATE_KEY = "ECCPrivateKey";

    static {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    /**
     *  使用私钥进行解密
     * @return
     */
    public static byte[] decrypt(byte[] data,String privateKey) throws IOException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException {
        // 密钥解密
        byte[] keyBytes = decryptBASE64(privateKey);
        // 取得密钥
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        ECPrivateKey ecPrivateKey = (ECPrivateKey) keyFactory.generatePrivate(pkcs8EncodedKeySpec);

        Cipher cipher = new NullCipher();
        cipher.init(Cipher.DECRYPT_MODE,ecPrivateKey);
        return cipher.doFinal(data);
    }

    /**
     *  加密
     * @param data
     * @param publicKey
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public static byte[] encrypt(byte[] data,String publicKey) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        // 解密密钥
        byte[] keyBytes = decryptBASE64(publicKey);
        // 获取公钥
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        ECPublicKey ecPublicKey = (ECPublicKey) keyFactory.generatePublic(x509EncodedKeySpec);
        // 加密
        Cipher cipher = new NullCipher();
        cipher.init(Cipher.ENCRYPT_MODE,ecPublicKey);
        return cipher.doFinal(data);
    }

    /**
     *  初始化密钥
     * @return
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     */
    public static Map<String,Object> initKey() throws NoSuchProviderException, NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC", "BC");
        keyPairGenerator.initialize(256,new SecureRandom());
        // 生成密钥对
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        // 生成公私钥
        ECPublicKey aPublic = (ECPublicKey) keyPair.getPublic();
        ECPrivateKey aPrivate = (ECPrivateKey) keyPair.getPrivate();
        Map<String,Object> map = new HashMap<>(2);
        map.put(PUBLIC_KEY,aPublic);
        map.put(PRIVATE_KEY,aPrivate);
        return map;
    }

    /**
     *  获取公钥
     * @param keyMap
     * @return
     */
    public static String getPublicKey(Map<String,Object> keyMap){
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return encryptionBASE64(key.getEncoded());
    }

    /**
     *  获取公钥
     * @param keyMap
     * @return
     */
    public static String getPrivateKey(Map<String,Object> keyMap){
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return encryptionBASE64(key.getEncoded());
    }
}
