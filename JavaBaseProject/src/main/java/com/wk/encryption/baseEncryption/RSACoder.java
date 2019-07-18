package com.wk.encryption.baseEncryption;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * RSA
 这种算法1978年就出现了，它是第一个既能用于数据加密也能用于数字签名的算法。它易于理解和操作，也很流行。算法的名字以发明者的名字命名：Ron Rivest, AdiShamir 和Leonard Adleman。
 这种加密算法的特点主要是密钥的变化，上文我们看到DES只有一个密钥。相当于只有一把钥匙，如果这把钥匙丢了，数据也就不安全了。RSA同时有两把钥匙，公钥与私钥。同时支持数字签名。数字签名的意义在于，对传输过来的数据进行校验。确保数据在传输工程中不被修改。

 流程分析：

 甲方构建密钥对儿，将公钥公布给乙方，将私钥保留。
 甲方使用私钥加密数据，然后用私钥对加密后的数据签名，发送给乙方签名以及加密后的数据；乙方使用公钥、签名来验证待解密数据是否有效，如果有效使用公钥对数据解密。
 乙方使用公钥加密数据，向甲方发送经过加密后的数据；甲方获得加密数据，通过私钥解密。
 */
public class RSACoder extends Coder{

    public static final String KEY_ALGORITHM = "RSA";

    public static final String Signature_Algorithm = "MD5withRSA";

    public static final String PUBLIC_KEY = "RSAPublicKey";

    public static final String Private_Key = "RSAPrivateKey";

    /**
     *  用私钥信息生成数字签名
     * @param data
     * @param privateKey
     * @return
     */
    public static String sign(byte[] data,String privateKey) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {
        // 解密由base64编码的私钥
        byte[] keyByte = decryptBASE64(privateKey);

        // 构造PKCS8EncodeKeySpec对象
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyByte);

        // 指定的加密算法的key工厂
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        // 获取私钥对象
        PrivateKey privateKey1 = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        // 用私钥对信息生成数字签名
        Signature signature = Signature.getInstance(Signature_Algorithm);
        signature.initSign(privateKey1);
        signature.update(data);
        String signatures = encryptionBASE64(signature.sign());
        return signatures;
    }

    /**
     *  校验数字签名
     * @param data
     * @param publicKey
     * @param sign
     * @return
     */
    public static boolean verify(byte[] data,String publicKey,String sign) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {
        // 解密由base64编码的公钥
        byte[] keyBytes = decryptBASE64(publicKey);
        // 构造X509EncodedKeySpec对象
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);

        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

        // 获取公钥对象
        PublicKey publicKey1 = keyFactory.generatePublic(x509EncodedKeySpec);
        Signature signature = Signature.getInstance(Signature_Algorithm);
        signature.initVerify(publicKey1);
        signature.update(data);

        // 验证签名是否正常
        boolean verify = signature.verify(decryptBASE64(sign));
        return verify;
    }

    /**
     *  用私钥解密
     */
    public static byte[] decryptByPrivateKey(byte[] data,String key) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        // 对密钥解密
        byte[] keyByte = decryptBASE64(key);
        // 取得私钥
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyByte);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

        // 对数据进行解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE,privateKey);
        byte[] bytes = cipher.doFinal(data);
        return bytes;
    }

    /**
     *  使用公钥进行解密
     */
    public static byte[] decryptByPublicKey(byte[] data,String key) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        // 对密钥解密
        byte[] keyBtes = decryptBASE64(key);

        // 取得密钥
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBtes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

        // 对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE,publicKey);
        byte[] bytes = cipher.doFinal(data);
        return bytes;
    }

    /**
     *  用公钥加密
     * @param data
     * @param key
     * @return
     */
    public static byte[] encryptByPublicKey(byte[] data,String key) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        // 公钥解密
        byte[] keyBytes = decryptBASE64(key);

        // 取得公钥
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE,publicKey);
        byte[] bytes = cipher.doFinal(data);
        return bytes;
    }

    /**
     *  使用私钥加密
     */
    public static byte[] encryptByPrivateKey(byte[] data,String key) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        // 对密钥加密
        byte[] keyBytes = decryptBASE64(key);

        // 取得私钥
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE,privateKey);
        byte[] bytes = cipher.doFinal(data);
        return bytes;
    }

    /**
     *  取得私钥
     * @param keyMap
     * @return
     */
    public static String getPrivateKey(Map<String,Object> keyMap){
        Key key = (Key) keyMap.get(Private_Key);
        String s = encryptionBASE64(key.getEncoded());
        return s;
    }

    /**
     *  取得私钥
     * @param keyMap
     * @return
     */
    public static String getPublicKey(Map<String,Object> keyMap){
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        String s = encryptionBASE64(key.getEncoded());
        return s;
    }

    /**
     *  初始化密钥
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static Map<String,Object> initKey() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        // 公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        // 私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        Map<String,Object> keyMap = new HashMap<>(2);
        keyMap.put(PUBLIC_KEY,publicKey);
        keyMap.put(Private_Key,privateKey);
        return keyMap;
    }
}
