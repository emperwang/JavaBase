package com.wk.encryption.baseEncryption;

/**
 * DH
 Diffie-Hellman算法(D-H算法)，密钥一致协议。是由公开密钥密码体制的奠基人Diffie和Hellman所提出的一种思想。
 简单的说就是允许两名用户在公开媒体上交换信息以生成"一致"的、可以共享的密钥。
 换句话说，就是由甲方产出一对密钥（公钥、私钥），乙方依照甲方公钥产生乙方密钥对（公钥、私钥）。
 以此为基线，作为数据传输保密基础，同时双方使用同一种对称加密算法构建本地密钥（SecretKey）对数据加密。
 这样，在互通了本地密钥（SecretKey）算法后，甲乙双方公开自己的公钥，使用对方的公钥和刚才产生的私钥加密数据，
 同时可以使用对方的公钥和自己的私钥对数据解密。不单单是甲乙双方两方，可以扩展为多方共享数据通讯，
 这样就完成了网络交互数据的安全通讯！该算法源于中国的同余定理——中国馀数定理
 */

import javax.crypto.*;
import javax.crypto.interfaces.DHPrivateKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * 流程分析：
 1.甲方构建密钥对儿，将公钥公布给乙方，将私钥保留；双方约定数据加密算法；乙方通过甲方公钥构建密钥对儿，
    将公钥公布给甲方，将私钥保留。
 2.甲方使用私钥、乙方公钥、约定数据加密算法构建本地密钥，然后通过本地密钥加密数据，发送给乙方加密后的数据；
    乙方使用私钥、甲方公钥、约定数据加密算法构建本地密钥，然后通过本地密钥对数据解密。
 3.乙方使用私钥、甲方公钥、约定数据加密算法构建本地密钥，然后通过本地密钥加密数据，发送给甲方加密后的数据；
    甲方使用私钥、乙方公钥、约定数据加密算法构建本地密钥，然后通过本地密钥对数据解密。
 */

/**
 *  需要配置参数：-Djdk.crypto.KeyAgreement.legacyKDF=true
 *  因为DH密钥至少为512，但是DES没有那么长
 */
public class DHCoder extends Coder {
    /**
     *  默认密钥字节数
     */
    private static final int KEY_SIZE = 1024;
    /**
     * DH加密需要一种对称加密算法对数据加密，这里使用DES，也可以使用其他对称加密算法
     */
    public static final String ALGORITHM = "DH";
    public static final String SECRET_ALGORITHM = "DES";
    public static final String PUBLIC_KEY = "DHPublicKey";
    public static final String PRIVATE_KEY = "DHPrivateKey";

    /**
     *  初始化甲方密钥
     * @return
     */
    public static Map<String,Object> initKey() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
        keyPairGenerator.initialize(KEY_SIZE);;
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        // 甲方公钥
        DHPublicKey publicKey = (DHPublicKey) keyPair.getPublic();
        // 甲方私钥
        DHPrivateKey privateKey = (DHPrivateKey) keyPair.getPrivate();

        Map<String,Object> keyMap = new HashMap<>(2);
        keyMap.put(PUBLIC_KEY,publicKey);
        keyMap.put(PRIVATE_KEY,privateKey);
        return keyMap;
    }

    /**
     *  初始化乙方密钥
     * @param key
     * @return
     */
    public static Map<String,Object> initKey(String key) throws IOException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeySpecException {
        // 解析甲方公钥
        byte[] keyByte = decryptBASE64(key);
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyByte);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

        // 由甲方公钥构建乙方私钥
        DHParameterSpec dhParameterSpec = ((DHPublicKey) publicKey).getParams();
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(keyFactory.getAlgorithm());
        keyPairGenerator.initialize(dhParameterSpec);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        // 乙方公钥
        DHPublicKey dhPublicKey = (DHPublicKey) keyPair.getPublic();

        // 乙方私钥
        DHPrivateKey dhPrivateKey = (DHPrivateKey) keyPair.getPrivate();
        Map<String,Object> keyMap = new HashMap<>(2);
        keyMap.put(PUBLIC_KEY,dhPublicKey);
        keyMap.put(PRIVATE_KEY,dhPrivateKey);
        return keyMap;
    }

    /**
     *  构建密钥
     * @param publicKey
     * @param privateKey
     * @return
     */
    public static SecretKey getSecretKey(String publicKey,String privateKey) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException {
        // 初始化公钥
        byte[] pubKeyByte = decryptBASE64(publicKey);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(pubKeyByte);
        PublicKey publicKey1 = keyFactory.generatePublic(x509EncodedKeySpec);

        // 初始化私钥
        byte[] privateKeyByte = decryptBASE64(privateKey);
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKeyByte);
        PrivateKey privateKey1 = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

        KeyAgreement keyAgreement = KeyAgreement.getInstance(keyFactory.getAlgorithm());
        keyAgreement.init(privateKey1);
        keyAgreement.doPhase(publicKey1,true);

        // 生成本地密钥
        SecretKey secretKey = keyAgreement.generateSecret(SECRET_ALGORITHM);
        return secretKey;
    }

    /**
     *  加密
     * @param data
     * @param publicKey  甲方公钥
     * @param privateKey 乙方私钥
     * @return
     */
    public static byte[] encrypt(byte[] data,String publicKey,String privateKey) throws InvalidKeySpecException, NoSuchAlgorithmException, InvalidKeyException, IOException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException {
        // 生成本地密钥
        SecretKey secretKey = getSecretKey(publicKey, privateKey);

        // 数据加密
        Cipher cipher = Cipher.getInstance(secretKey.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE,secretKey);
        return cipher.doFinal(data);
    }

    /**
     *  解密
     * @param data
     * @param publicKey 乙方公钥
     * @param privateKey 乙方私钥
     * @return
     */
    public static byte[] decript(byte[] data,String publicKey,String privateKey) throws InvalidKeySpecException, NoSuchAlgorithmException, InvalidKeyException, IOException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException {
        // 生成本地密钥
        SecretKey secretKey = getSecretKey(publicKey, privateKey);
        // 数据解密
        Cipher cipher = Cipher.getInstance(secretKey.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE,secretKey);
        return cipher.doFinal(data);
    }

    /**
     *  取得私钥
     * @param keyMap
     * @return
     */
    public static String getPrivateKey(Map<String,Object> keyMap){
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return encryptionBASE64(key.getEncoded());
    }

    /**
     *  取得公钥
     * @param keyMap
     * @return
     */
    public static String getPublicKey(Map<String,Object> keyMap){
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return encryptionBASE64(key.getEncoded());
    }
}
