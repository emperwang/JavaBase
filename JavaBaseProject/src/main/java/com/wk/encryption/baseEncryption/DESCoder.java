package com.wk.encryption.baseEncryption;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

/**
 * DES-Data Encryption Standard,即数据加密算法。是IBM公司于1975年研究成功并公开发表的。
 * DES算法的入口参数有三个:Key、Data、Mode。其中Key为8个字节共64位,
 * 是DES算法的工作密钥;Data也为8个字节64位,是要被加密或被解密的数据;
 * Mode为DES的工作方式,有两种:加密或解密。
 */
public class DESCoder extends Coder {

    /**
     *  Algorithm 可替换为以下任意一种算法，同时key值的size相应改变
     *  DES    key size must be equals 56
     *  DESede(TripleDES) key size must be equals 112 or 168
     *  AES                 key size must be equals 128,192,256;but 192 and 256 bits may not be available
     *  Blowfish        key size must be multiple of 8 , and can only range from 32 to 448 (inclusive)
     *  RC2         key size must be between 40 and 1024 bits
     *  RC4         key size must be between 40 and 1024 bits
     */
    private static final String ALGORITHM ="DES";

    /**
     *  转换密钥
     * @param key
     * @return
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static Key toKey(byte[] key) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException {
        DESKeySpec desKeySpec = new DESKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        /**
         *  当使用其他对称加密算法时,如AES,Blowfish等算法时,用下述代码代替上述三行代码
         */
        //SecretKeySpec secretKeySpec = new SecretKeySpec(key, ALGORITHM);
        return secretKey;
    }

    /**
     *  加密
     * @param data
     * @param key
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws InvalidKeySpecException
     * @throws NoSuchPaddingException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static byte[] encrypt(byte[] data,String key) throws IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException {
        Key k = toKey(deccryBASE64(key));
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        // 使用加密的key和要进行的模式初始化算法
        cipher.init(Cipher.ENCRYPT_MODE,k);
        // 加密操作
        byte[] bytes = cipher.doFinal(data);
        return bytes;
    }

    /**
     *  解密
     * @param data
     * @param key
     * @return
     */
    public static byte[] decript(byte[] data,String key) throws IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException {
        Key k = toKey(deccryBASE64(key));
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE,k);
        byte[] bytes = cipher.doFinal(data);
        return bytes;
    }

    /**
     *  生成密钥
     * @return
     */
    public static String initKey() throws IOException, NoSuchAlgorithmException {
        return initKey(null);
    }

    /**
     *  生成密钥
     * @param seed
     * @return
     */
    public static String initKey(String seed) throws IOException, NoSuchAlgorithmException {
        SecureRandom secureRandom = null;
        if (seed != null){
            secureRandom = new SecureRandom(deccryBASE64(seed));
        }else {
            secureRandom = new SecureRandom();
        }

        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(secureRandom);
        SecretKey secretKey = keyGenerator.generateKey();
        return encryptionBASE64(secretKey.getEncoded());
    }
    /**
     *   其实DES有很多同胞兄弟，如DESede(TripleDES)、AES、Blowfish、RC2、RC4(ARCFOUR)。
     *   大同小异，只要换掉ALGORITHM换成对应的值，同时做一个代码替换
     *   SecretKey secretKey = new SecretKeySpec(key, ALGORITHM);就可以了，此外就是密钥长度不同了。
     */
}
