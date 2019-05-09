package com.wk.encrypt;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class EncryptUtil {
    //java.security.MessageDigest 包中的算法
    public static final String MD5 = "MD5";
    public static final String SHA1 = "SHA-1";
    public static final String SHA256 = "SHA-256";
    //javax.crypto.Cipher
    public static final String AES= "AES";
    public static final String DES = "DES";
    public static final String DESede = "DESede";
    //javax.crypto.Mac
    public static final String HmacSHA1 = "HmacSHA1";
    public static final String HmacMD5 = "HmacMD5";
    public static final String HmacSHA256 = "HmacSHA256";

    //十六进制的字符
    private final static String[] hexDigits = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
    /*默认的编码格式*/
    public static String charset = "UTF-8";
    //des加密长度
    private static int keysizeDES = 0;
    // aes 加密长度
    private static int keysizeAES = 128;
    private static int keysizeDESede = 112;   // 这里的长度可以为112 或者是168

    /**
     * base64 加密
     * @param res
     * @return
     */
    public static byte[] base64Encode(String res){
        byte[] bytes;
        try {
            bytes = charset == null?res.getBytes():res.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(),e);
        }
        byte[] encode = Base64.getEncoder().encode(bytes);
        return encode;
    }

    /**
     * base64 加密
     * @param res
     * @return
     */
    public static String base64EncodeString(String res){
        byte[] bytes;
        try {
            bytes = charset == null?res.getBytes():res.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(),e);
        }
        String encodeToString = Base64.getEncoder().encodeToString(bytes);
        return encodeToString;
    }

    /**
     * base64 解密
     * @param bytes
     * @return
     */
    public static String base64Decode(byte[] bytes){
        byte[] decode = Base64.getDecoder().decode(bytes);
        return new String(decode);
    }

    /**
     *  base64 解密
     * @param str
     * @return
     */
    public static String base64Decode(String str){
        byte[] decode = Base64.getDecoder().decode(str);
        return new String(decode);
    }

    /**
     *  使用MessageDigest 进行单向加密  MD5 SHA-1  SHA-256
     * @param res  要进行加密的文本
     * @param algorithm  算法名称
     * @return
     */
    private static byte[] messageDigest(String res,String algorithm){
        try {
            MessageDigest instance = MessageDigest.getInstance(algorithm);
            byte[] bytes = charset == null ? res.getBytes() : res.getBytes(charset);
            byte[] digest = instance.digest(bytes);
            return digest;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage(),e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    /**
     *  MD5 进行加密
     * @param res 加密文本
     * @return
     */
    public static String MD5(String res){
        byte[] bytes = messageDigest(res, MD5);
        return byteArrayToHexString(bytes);
    }

    /**
     *  sha-1 加密
     * @param res 要加密的文本
     * @return
     */
    public static String sha1Encode(String res){
        byte[] bytes = messageDigest(res, SHA1);
        return byteArrayToHexString(bytes);
    }

    /**
     * sha-256 加密
     * @param res 要加密的文本
     * @return
     */
    public static String sha256Encode(String res){
        byte[] bytes = messageDigest(res, SHA256);
        return byteArrayToHexString(bytes);
    }
    /**
     * 将给定的字节数组转换为字符串
     * @param digest
     * @return
     */
    public static String byteArrayToHexString(byte[] digest) {
        StringBuffer resultSb = new StringBuffer();
        for(int i=0;i<digest.length;i++){
            resultSb.append(byteToHexString(digest[i]));
        }
        return resultSb.toString();
    }

    public static String byteToHexString(byte b) {
        int n = b;
        if(n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1]+hexDigits[d2];
    }

    /**
     * 16进制数转换为二进制数
     * @param str
     * @return
     */
    public static byte[] hex2Bytes(String str) {
        if(str == null || str.trim().equals("")) {
            return new byte[0];
        }
        byte[] bytes = new byte[str.length() / 2];
        for(int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }
        return bytes;
    }

    /**
     * 使用keyGenerator进行单向/双向加密
     * @param res  被加密的原文
     * @param algorithm 加密使用的算法
     * @param key 加密使用的密钥
     * @return
     */
    private static byte[] keyGeneratorMac(String res,String algorithm,String key){
        try {
            SecretKey sk = null;
            if(key == null){
                KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
                sk = keyGenerator.generateKey();
            }else {
                byte[] keyBytes = charset == null ? key.getBytes() : key.getBytes(charset);
                sk = new SecretKeySpec(keyBytes,algorithm);
            }
            Mac mac = Mac.getInstance(algorithm);
            mac.init(sk);
            byte[] bytes = mac.doFinal(charset == null ? res.getBytes() : res.getBytes(charset));
            return bytes;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage(),e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e.getMessage(),e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    /**
     *  MD5加密算法加密
     * @param res 加密的原文
     * @param key 密钥
     * @return
     */
    public static String MD5(String res,String key){
        byte[] bytes = keyGeneratorMac(res, HmacMD5, key);
        String s = byteArrayToHexString(bytes);
        return s;
    }

    /**
     *  HmacSHA1 算法加密
     * @param res
     * @param key
     * @return
     */
    public static String HmacSHA1(String res,String key){
        byte[] bytes = keyGeneratorMac(res, HmacSHA1, key);
        String s = byteArrayToHexString(bytes);
        return s;
    }

    /**
     *  HmacSHA256加密
     * @param res
     * @param key
     * @return
     */
    public static String HmacSHA256(String res,String key){
        byte[] bytes = keyGeneratorMac(res, HmacSHA256, key);
        String s = byteArrayToHexString(bytes);
        return s;
    }

    /**
     *  这里使用keyGenerator双向加密,DES/AES
     * @param res  加密原文
     * @param algorithm  加密算法
     * @param key  加密密钥
     * @param keySize 没有提供密钥时,使用的密钥长度
     * @param idEncode  加密还是解密标志
     * @return
     */
    private static String keyGeneratorES(String res,String algorithm,String key,int keySize,boolean idEncode){
        String result = null ;
        try{
            KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
            if(keySize == 0){
                byte[] bytes = charset == null ? key.getBytes() : key.getBytes(charset);
                keyGenerator.init(new SecureRandom(bytes));
            }else if(key == null){
                keyGenerator.init(keySize);
            }else{
                byte[] bytes = charset == null ? key.getBytes() : key.getBytes(charset);
                keyGenerator.init(keySize,new SecureRandom(bytes));
            }

            SecretKey sk = keyGenerator.generateKey();
            SecretKeySpec secretKeySpec = new SecretKeySpec(sk.getEncoded(), algorithm);
            Cipher cipher = Cipher.getInstance(algorithm);
            if(idEncode){
                cipher.init(Cipher.ENCRYPT_MODE,secretKeySpec);
                byte[] bytes = charset == null ? res.getBytes() : res.getBytes(charset);
                byte[] bytes1 = cipher.doFinal(bytes);
                result = byteArrayToHexString(bytes1);
                return result;
            }else{
                cipher.init(Cipher.DECRYPT_MODE,secretKeySpec);
                byte[] bytes = hex2Bytes(res);
                byte[] bytes1 = cipher.doFinal(bytes);
                result = new String(bytes1);
                return result;
            }

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage(),e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(),e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e.getMessage(),e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e.getMessage(),e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e.getMessage(),e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    /**
     *  DES 加密
     * @param res 原文
     * @param key 密钥
     * @return
     */
    public static String DESEncode(String res,String key){
        String s = keyGeneratorES(res, DES, key, keysizeDES, true);
        return s;
    }

    /**
     * DES解密
     * @param res 原文
     * @param key 密钥
     * @return
     */
    public static String DESDecode(String res,String key){
        String s = keyGeneratorES(res, DES, key, keysizeDES, false);
        return s;
    }

    /**
     * AES加密
     * @param res 原文
     * @param key  密钥
     * @return
     */
    public static String AESEncode(String res,String key){
        String s = keyGeneratorES(res, AES, key, keysizeAES, true);
        return s;
    }

    /**
     *  AES 解密
     * @param res 原文
     * @param key 密钥
     * @return
     */
    public static String AESDecode(String res,String key){
        String s = keyGeneratorES(res, AES, key, keysizeAES, false);
        return s;
    }

    public static String DESedeEncode(String res,String key){
        String s = keyGeneratorES(res, DESede, key, keysizeDESede, true);
        return s;
    }

    public static String DESedeDecode(String res,String key){
        String s = keyGeneratorES(res, DESede, key, keysizeDESede, false);
        return s;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        // base64加密
        /*String str = "qwer";
        System.out.println(new String(base64Encode(str)));
        System.out.println(base64EncodeString(str));
        System.out.println(base64Decode("cXdlcg=="));*/

        //测试二进制转换
/*        String str = "binary test";
        byte[] bytes1 = str.getBytes("UTF-8");
        String s = byteArrayToHexString(bytes1);
        System.out.println(s);
        System.out.println(hex2Bytes(s));
        System.out.println(new String(hex2Bytes(s)));*/


        String md5test = "md5-test-text";
        String key = "keys_init_value";
        System.out.println(DESEncode(md5test,key));
        System.out.println(DESDecode("37DA3C97D479C5622FDB9E6044B5921F", key));
        System.out.println(AESEncode(md5test,key));
        System.out.println(AESDecode("FE62682C89B65ACF7C93D13AD21CBBC9",key));
        System.out.println(DESedeEncode(md5test,key));
        System.out.println(DESedeDecode("7E7513FCC10CB47327E874EAF514EA05",key));
/*        String s = MD5(md5test);
        System.out.println(s);
        System.out.println(sha1Encode(md5test));
        System.out.println(sha256Encode(md5test));*/
/*        System.out.println(MD5(md5test,key));
        System.out.println(MD5(md5test,null));*/
/*        System.out.println(HmacSHA1(md5test,key));
        System.out.println(HmacSHA1(md5test,null));
        System.out.println(HmacSHA256(md5test,key));
        System.out.println(HmacSHA256(md5test,null));*/
    }
}
