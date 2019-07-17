package com.wk.encryption.baseEncryption;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Random;

/**
 * PBE——Password-based encryption（基于密码加密）。其特点在于口令由用户自己掌管，
 * 不借助任何物理媒体；采用随机数（这里我们叫做盐）杂凑多重加密等方法保证数据的安全性。
 * 是一种简便的加密方式。
 */
public class PBECoder extends Coder {
    /**
     *  支持算法:
     * PBEWithMD5AndDES
     * PBEWithMD5AndTripleDES
     * PBEWithSHA1AndDESede
     * PBEWithSHA1AndRC2_40
     */
    public static final String ALGORITHM = "PBEWITHMD5andDES";

    /**
     *  盐初始化
     * @return
     */
    public static byte[] initSalt(){
        byte[] salt = new byte[8];
        Random random = new Random();
        random.nextBytes(salt);
        return salt;
    }

    /**
     *  转换密钥
     * @param password
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static Key toKey(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        SecretKey secretKey = keyFactory.generateSecret(pbeKeySpec);
        return secretKey;
    }

    /**
     *  加密
     * @param data
     * @param password
     * @param salt
     * @return
     * @throws InvalidKeySpecException
     */
    public static byte[] encrypt(byte[] data,String password,byte[] salt) throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Key key = toKey(password);
        PBEParameterSpec pbeParameterSpec = new PBEParameterSpec(salt, 100);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE,key,pbeParameterSpec);
        byte[] bytes = cipher.doFinal(data);
        return bytes;
    }

    /**
     *  解密
     * @param data
     * @param password
     * @param salt
     * @return
     */
    public static byte[] decrypt(byte[] data,String password,byte[] salt) throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Key key = toKey(password);
        PBEParameterSpec pbeParameterSpec = new PBEParameterSpec(salt, 100);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE,key,pbeParameterSpec);
        byte[] bytes = cipher.doFinal(data);
        return bytes;
    }
}
