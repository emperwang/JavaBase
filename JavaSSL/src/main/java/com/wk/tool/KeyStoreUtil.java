package com.wk.tool;

import javax.security.auth.x500.X500Principal;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.*;

public class KeyStoreUtil {
    private static Properties prop;
    private static String keyStorePath = "";
    private static String keyPwd = "";
    private static KeyStore keyStore;
    private static String sslAlias = "";
    private static String certificatType = "X509";
    private static String certPath = "";
    static {
        try {
            prop = FilePathUtil.getProp("config.properties");
            keyStorePath = prop.getProperty("serverjks");
            keyPwd = prop.getProperty("serverjkspwd");
            sslAlias = prop.getProperty("serveralias");

            // 获取证书path
            certPath = prop.getProperty("servercrt");

            // 加载密钥
            //keyStore = KeyStore.getInstance("JKS");
            //keyStore.load(new FileInputStream(keyStorePath),keyPwd.toCharArray());
        } catch (IOException e) {
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
    // 获取证书信息
    public static void certificatInfo() throws CertificateException, FileNotFoundException {
        CertificateFactory certificateFactory = CertificateFactory.getInstance(certificatType);
        FileInputStream fileInputStream = new FileInputStream(certPath);
        X509Certificate certificate = (X509Certificate)certificateFactory.generateCertificate(fileInputStream);
        String type = certificate.getType();
        System.out.println("Certificate type :"+type);

        PublicKey publicKey = certificate.getPublicKey();
        byte[] encoded = publicKey.getEncoded();
        String s = Base64.getEncoder().encodeToString(encoded);
        System.out.println("publickey :"+s);

        int basicConstraints = certificate.getBasicConstraints();
        System.out.println("basicConstraints : "+basicConstraints);

        List<String> extendedKeyUsage = certificate.getExtendedKeyUsage();
        System.out.println("extendedKeyUsage : "+extendedKeyUsage);

        Collection<List<?>> issuerAlternativeNames = certificate.getIssuerAlternativeNames();
        System.out.println("issuerAlternativeNames : " + issuerAlternativeNames);

        Principal issuerDN = certificate.getIssuerDN();
        System.out.println("issuerDN:"+issuerDN.toString());

        X500Principal issuerX500Principal = certificate.getIssuerX500Principal();
        String issuerX500PrincipalName = issuerX500Principal.getName();
        byte[] encoded1 = issuerX500Principal.getEncoded();
        String x509principalEncoded = Base64.getEncoder().encodeToString(encoded1);
        System.out.println("issuerX500PrincipalName : "+issuerX500PrincipalName);
        System.out.println("encoded1 : "+encoded1);
        System.out.println("x509principalEncoded : "+x509principalEncoded);

        boolean[] issuerUniqueID = certificate.getIssuerUniqueID();
        System.out.println("issuerUniqueID : "+issuerUniqueID);

        boolean[] keyUsage = certificate.getKeyUsage();
        System.out.println("keyUsage :"+ keyUsage);

        BigInteger serialNumber = certificate.getSerialNumber();
        System.out.println("serialNumber : "+serialNumber);

        String sigAlgName = certificate.getSigAlgName();
        System.out.println("sigAlgName : "+sigAlgName);

        byte[] signature = certificate.getSignature();
        String signatureStr = Base64.getEncoder().encodeToString(signature);
        System.out.println("signatureStr :" +signatureStr);

        Collection<List<?>> subjectAlternativeNames = certificate.getSubjectAlternativeNames();
        System.out.println("getSubjectAlternativeNames :" + subjectAlternativeNames);

        X500Principal subjectX500Principal = certificate.getSubjectX500Principal();
        String name = subjectX500Principal.getName();

        String RFC2253Name = subjectX500Principal.getName(X500Principal.RFC2253);
        byte[] encoded2 = subjectX500Principal.getEncoded();
        String subjectX500PrincipalEncodedStr = Base64.getEncoder().encodeToString(encoded2);
        System.out.println("subjectX500Principal.getName :" + name);
        System.out.println("RFC2253Name :" + RFC2253Name);
        System.out.println("subjectX500PrincipalEncodedStr :" + subjectX500PrincipalEncodedStr);
    }

    public static void main(String[] args) throws KeyStoreException, UnrecoverableEntryException, NoSuchAlgorithmException, CertificateException, FileNotFoundException {
       // getAlias();
       // getPrivateKey();
       // getPublicKey();
        certificatInfo();

    }
}
