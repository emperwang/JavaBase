package com.wk.encryption.baseEncryption;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.*;
import java.security.cert.*;
import java.security.cert.Certificate;
import java.util.Date;

/**
 * 证书组件
 */
public class CertificateCoder extends Coder {
    /**
     * java密钥库(java key store)
     */
    public static final String KEY_STORE = "JKS";
    public static final String X509 = "X.509";

    /**
     * 获得keystore
     *
     * @param keyStorePath
     * @param password
     * @return
     */
    public static KeyStore getKeyStore(String keyStorePath, String password) throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException {
        FileInputStream inputStream = new FileInputStream(keyStorePath);
        KeyStore keyStore = KeyStore.getInstance(KEY_STORE);
        keyStore.load(inputStream, password.toCharArray());
        inputStream.close();
        return keyStore;
    }

    /**
     * 由KeyStore获得私钥
     *
     * @param keyStorePath
     * @param alias
     * @param password
     * @return
     */
    public static PrivateKey getPrivateKey(String keyStorePath, String alias, String password) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, UnrecoverableKeyException {
        KeyStore keyStore = getKeyStore(keyStorePath, password);
        PrivateKey key = (PrivateKey) keyStore.getKey(alias, password.toCharArray());
        return key;
    }

    /**
     *  获得certificate
     * @param certificatePath
     * @return
     * @throws CertificateException
     * @throws FileNotFoundException
     */
    public static Certificate getCertificate(String certificatePath) throws CertificateException, IOException {
        CertificateFactory certificateFactory = CertificateFactory.getInstance(X509);
        FileInputStream inputStream = new FileInputStream(certificatePath);
        Certificate certificate = certificateFactory.generateCertificate(inputStream);
        inputStream.close();
        return certificate;
    }

    /**
     *  获取publicKey
     * @param certificatePath
     * @return
     * @throws CertificateException
     * @throws FileNotFoundException
     */
    public static PublicKey getPublicKey(String certificatePath) throws CertificateException, IOException {
        Certificate certificate = getCertificate(certificatePath);
        PublicKey publicKey = certificate.getPublicKey();
        return publicKey;
    }

    /**
     *  获取certificate
     * @param keyStorePath
     * @param alias
     * @param password
     * @return
     */
    public static Certificate getCertificate(String keyStorePath,String alias,String password) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
        KeyStore keyStore = getKeyStore(keyStorePath, password);
        Certificate certificate = keyStore.getCertificate(alias);
        return certificate;
    }

    /**
     *  私钥加密
     * @param data
     * @param keyStorePath
     * @param alias
     * @param password
     * @return
     */
    public static byte[] encryptByPrivateKey(byte[] data,String keyStorePath,String alias,
                                            String password) throws UnrecoverableKeyException, CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        // 获取私钥
        PrivateKey privateKey = getPrivateKey(keyStorePath, alias, password);
        // 加密
        Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE,privateKey);
        return cipher.doFinal(data);
    }

    /**
     * 用私钥对数据进行解密
     */
    public static byte[] decryptByPrivateKey(byte[] data,String keyStorePath,String alias,String password) throws UnrecoverableKeyException, CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        // 取得私钥
        PrivateKey privateKey = getPrivateKey(keyStorePath, alias, password);
        // 对数据解密
        Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE,privateKey);
        return cipher.doFinal(data);
    }

    /**
     *  使用公钥加密
     * @param data
     * @param certificatePath
     * @return
     */
    public static byte[] encryptByPublicKey(byte[] data,String certificatePath) throws CertificateException, IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        // 取得公钥
        PublicKey publicKey = getPublicKey(certificatePath);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE,publicKey);
        return cipher.doFinal(data);
    }

    /**
     *  公钥解密
     * @param data
     * @param certificatePath
     * @return
     */
    public static byte[] decryptByPublicKey(byte[] data,String certificatePath) throws CertificateException, IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        // 获取公钥
        PublicKey publicKey = getPublicKey(certificatePath);

        // 公钥解密
        Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE,publicKey);
        return cipher.doFinal(data);
    }

    /**
     *  验证证书是否过期
     * @param cerificatePath
     * @return
     */
    public static boolean verifyCerificate(String cerificatePath) throws CertificateException, IOException {
        return  verifyCertificate(new Date(),cerificatePath);
    }

    /**
     *  验证证书是否过期
     * @param data
     * @param certificatePath
     * @return
     */
    public static boolean verifyCertificate(Date data,String certificatePath) throws CertificateException, IOException {
        // 取得证书
        Certificate certificate = getCertificate(certificatePath);
        // 验证证书是否过期
        boolean status = verifyCerificate(data,certificate);
        return status;
    }

    /**
     *  验证证书
     * @param data
     * @param certificate
     * @return
     */
    private static boolean verifyCerificate(Date data, Certificate certificate) {
        X509Certificate x509Certificate = (X509Certificate) certificate;
        boolean status = true;
        try {
            x509Certificate.checkValidity(data);
        } catch (CertificateExpiredException e) {
            status = false;
        } catch (CertificateNotYetValidException e) {
            status = false;
        }
        return true;
    }

    /**
     *  验证证书
     * @param data
     * @param keyStorePath
     * @param alias
     * @param password
     * @return
     */
    public static boolean verifyCertificate(Date data,String keyStorePath,String alias,String password) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
        Certificate certificate = getCertificate(keyStorePath, alias, password);
        return verifyCertificate(data, keyStorePath);
    }

    /**
     *  验证证书
     * @param keyStorePath
     * @param alias
     * @param password
     * @return
     * @throws CertificateException
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     * @throws IOException
     */
    public static boolean verifyCertificate(String keyStorePath,String alias,String password) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
        return verifyCertificate(new Date(),keyStorePath,alias,password);
    }
    /**
     *  签名
     * @param sign
     * @param keyStorePath
     * @param alias
     * @param password
     * @return
     */
    public static String sign(byte[] data,String keyStorePath,String alias,String password) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, UnrecoverableKeyException, InvalidKeyException, SignatureException {
        // 获得证书
        X509Certificate x509Certificate = (X509Certificate) getCertificate(keyStorePath, alias, password);
        // 获取私钥
        KeyStore keyStore = getKeyStore(keyStorePath, password);
        PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, password.toCharArray());

        // 构建签名
        Signature signature = Signature.getInstance(x509Certificate.getSigAlgName());
        signature.initSign(privateKey);
        signature.update(data);
        return encryptionBASE64(signature.sign());
    }

    /**
     *  验证签名
     * @param data
     * @param sign
     * @param certificatePath
     * @return
     */
    public static boolean verifySign(byte[] data,String sign,String certificatePath) throws CertificateException, IOException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        // 获得证书
        X509Certificate x509Certificate = (X509Certificate) getCertificate(certificatePath);

        // 获取公钥
        PublicKey publicKey = x509Certificate.getPublicKey();
        // 构建签名
        Signature signature = Signature.getInstance(x509Certificate.getSigAlgName());
        signature.initVerify(publicKey);
        signature.update(data);
        return signature.verify(decryptBASE64(sign));
    }


}
