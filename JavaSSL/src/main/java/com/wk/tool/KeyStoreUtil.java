package com.wk.tool;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Base64;
import java.util.Enumeration;
import java.util.Properties;

public class KeyStoreUtil {
    private static Properties prop;
    private static String keyStorePath = "";
    private static String keyPwd = "";
    private static KeyStore keyStore;
    private static String sslAlias = "";
    static {
        try {
            prop = FilePathUtil.getProp("config.properties");
            keyStorePath = prop.getProperty("serverjks");
            keyPwd = prop.getProperty("serverjkspwd");
            sslAlias = prop.getProperty("serveralias");

            // 加载密钥
            keyStore = KeyStore.getInstance("JKS");
            keyStore.load(new FileInputStream(keyStorePath),keyPwd.toCharArray());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
    }

    /**
     *  获取keyStore的中的别名
     */
    public static void getAlias() throws KeyStoreException {
        Enumeration<String> aliases = keyStore.aliases();
        while (aliases.hasMoreElements()){
            String s = aliases.nextElement();
            System.out.println(s);
        }
    }

    /**
     *  获取私钥
     */
    public static void getPrivateKey() throws KeyStoreException {
        Certificate certificate = keyStore.getCertificate(sslAlias);
        String type = certificate.getType();
        System.out.println(type);

        PublicKey publicKey = certificate.getPublicKey();
        String publicKeys = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        System.out.println("Public Key is:"+ publicKeys);
    }

    /**
     *  获取公钥
     */
    public static void getPublicKey() throws KeyStoreException, UnrecoverableEntryException, NoSuchAlgorithmException {
        KeyStore.PrivateKeyEntry entry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(sslAlias,
                new KeyStore.PasswordProtection(keyPwd.toCharArray()));
        PrivateKey privateKey = entry.getPrivateKey();
        String s = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        System.out.println("privateKey is: "+ s);
    }



    public static void main(String[] args) throws KeyStoreException, UnrecoverableEntryException, NoSuchAlgorithmException {
        getAlias();
        getPrivateKey();
        getPublicKey();
    }
}
