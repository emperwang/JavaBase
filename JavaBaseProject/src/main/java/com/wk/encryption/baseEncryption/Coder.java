package com.wk.encryption.baseEncryption;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *  基础加密组件
 */

/**
 *
 * HMAC(Hash Message Authentication Code，散列消息鉴别码，
 * 基于密钥的Hash算法的认证协议。消息鉴别码实现鉴别的原理是，
 * 用公开函数和密钥产生一个固定长度的值作为认证标识，用这个标识鉴别消息的完整性。
 * 使用一个密钥生成一个固定大小的小数据块，即MAC，并将其加入到消息中，然后传输。
 * 接收方利用与发送方共享的密钥进行鉴别认证等。
 */
public class Coder {
    private static final String KEY_MD5="MD5";

    private static final String KEY_SHA="SHA";


    /**
     *  加密
     * @param key
     * @return
     */
    public static String encryptionBASE64(byte[] key){
        String encode = new BASE64Encoder().encode(key);
        return encode;
    }

    /**
     *  加密
     * @param key
     * @return
     * @throws IOException
     */
    public static byte[] deccryBASE64(String key) throws IOException {
        byte[] bytes = new BASE64Decoder().decodeBuffer(key);
        return bytes;
    }


    /**
     * MD5加密
     * MD5 -- message-digest algorithm 5:信息-摘要算法
     * @param data
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static byte[] encryptMD5(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest MD5 = MessageDigest.getInstance(KEY_MD5);
        MD5.update(data);
        byte[] digest = MD5.digest();
        return digest;
    }


    /**
     *  SHA(Secure Hash Algorithm ,安全散列算法)
     * @param data
     * @return
     */
    public static byte[] encryptSHA(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest SHA = MessageDigest.getInstance(KEY_SHA);
        SHA.update(data);
        byte[] digest = SHA.digest();
        return digest;
    }

    /**
     * MAC 算法有以下多种算法
     *  HmacMD5
     *  HmacSHA1
     *  HmacSHA256
     *  HmacSHA384
     *  HmacSHA512
     */
    private static final String KEY_MAC="HmacMD5";

    /**
     *  初始化密码
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String initMacKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_MAC);
        SecretKey secretKey = keyGenerator.generateKey();
        return encryptionBASE64(secretKey.getEncoded());
    }

    /**
     *  HMAC加密
     * @param data
     * @param key
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    public static byte[] encryptHMAC(byte[] data,String key) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec keySpec = new SecretKeySpec(deccryBASE64(key), KEY_MAC);
        Mac mac = Mac.getInstance(keySpec.getAlgorithm());
        mac.init(keySpec);
        byte[] bytes = mac.doFinal(data);
        return bytes;
    }
}
