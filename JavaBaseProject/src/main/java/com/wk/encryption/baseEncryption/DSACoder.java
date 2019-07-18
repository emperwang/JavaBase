package com.wk.encryption.baseEncryption;

import java.io.IOException;
import java.security.*;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * DSA
 DSA-Digital Signature Algorithm 是Schnorr和ElGamal签名算法的变种，被美国NIST作为DSS(DigitalSignature Standard)。
 简单的说，这是一种更高级的验证方式，用作数字签名。不单单只有公钥、私钥，还有数字签名。
 私钥加密生成数字签名，公钥验证数据及签名。如果数据和签名不匹配则认为验证失败！
 数字签名的作用就是校验数据在传输过程中不被修改。数字签名，是单向加密的升级
 */
public class DSACoder extends Coder{

    public static final String ALGORITHM = "DSA";
    // 默认密钥字节长度
    private static final int KEY_SIZE = 1024;
    // 默认种子
    private static final String DEFAULT_SEED = "0f22507a10bbddd07d8a3082122966e3";
    // key值
    private static final String PUBLIC_KEY = "DSAPublicKey";
    private static final String PRIVATE_KEY = "DSAPrivateKey";

    /**
     *  生成密钥
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static Map<String,Object> initKey(String seed) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
        // 初始化随机产生器
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.setSeed(seed.getBytes());
        keyPairGenerator.initialize(KEY_SIZE,secureRandom);
        KeyPair keyPair = keyPairGenerator.genKeyPair();

        DSAPublicKey publicKey = (DSAPublicKey) keyPair.getPublic();
        DSAPrivateKey privateKey = (DSAPrivateKey) keyPair.getPrivate();

        Map<String,Object> map = new HashMap<>(2);
        map.put(PUBLIC_KEY,publicKey);
        map.put(PRIVATE_KEY,privateKey);
        return map;
    }

    /**
     *  默认生成密钥
     */
    public static Map<String,Object> initKey() throws NoSuchAlgorithmException {
        return initKey(DEFAULT_SEED);
    }

    /**
     *  使用私钥对信息生成数字签名
     * @param data
     * @param privateKey
     * @return
     */
    public static String sign(byte[] data,String privateKey) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {
        // 解密私钥
        byte[] keyBytes = decryptBASE64(privateKey);
        // 构造PKCS8EncodedKeySpec对象
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        // 制定加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        // 获取私钥对象
        PrivateKey privateKey1 = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

        // 用私钥对信息生成数字签名
        Signature signature = Signature.getInstance(keyFactory.getAlgorithm());
        signature.initSign(privateKey1);
        signature.update(data);
        return encryptionBASE64(signature.sign());
    }

    /**
     * 验证数字签名
     * @param data
     * @param publicKey
     * @param sign
     * @return
     */
    public static boolean verify(byte[] data,String publicKey,String sign) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {
        // 解密私钥
        byte[] keyBytes = decryptBASE64(publicKey);

        // 构造x509对象
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        // 指定加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        // 获取公钥
        PublicKey publicKey1 = keyFactory.generatePublic(x509EncodedKeySpec);
        Signature signature = Signature.getInstance(keyFactory.getAlgorithm());
        signature.initVerify(publicKey1);
        signature.update(data);
        // 验证签名是否正常
        return signature.verify(decryptBASE64(sign));
    }

    /**
     *  获取私钥
     * @param keyMap
     * @return
     */
    public static String getPrivateKey(Map<String,Object> keyMap){
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return encryptionBASE64(key.getEncoded());
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
}

