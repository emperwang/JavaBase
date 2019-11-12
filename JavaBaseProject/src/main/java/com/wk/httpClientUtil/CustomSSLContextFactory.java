package com.wk.httpClientUtil;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Slf4j
public class CustomSSLContextFactory {
    private static final String KEY_TYPE_PKCS = "PKCS12";
    private static final String KEY_TYPE_JKS = "JKS";
    private static final String[] protocols = {"TLSv1.2","TLSv1.1","SSLv3"};
    /**
     *  trustAll   without hostname
     * @return
     */
    public static SSLConnectionSocketFactory createTrustAllSSLContextFactoryWithHostName(){
        SSLContext sslContext = null;
        SSLConnectionSocketFactory sslConnectionSocketFactory = null;
        try {
            sslContext = SSLContexts.custom()
                    .loadTrustMaterial(new TrustAllStrategy()).build();
            // 此不会认证主机名字
            sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext,protocols,
                    null,
                    NoopHostnameVerifier.INSTANCE);
        } catch (NoSuchAlgorithmException e) {
            log.error("createSSLContextWithHostName NoSuchAlgorithmException , error msg is: {}",e.getMessage());
        } catch (KeyManagementException e) {
            log.error("createSSLContextWithHostName KeyManagementException, error msg is: {}",e.getMessage());
        } catch (KeyStoreException e) {
            log.error("createSSLContextWithHostName KeyStoreException, error msg is: {}",e.getMessage());
        }
        return sslConnectionSocketFactory;
    }

    /**
     *  trustAll  SSlContext  for asyncHttpClient
     * @return
     */
    public static SSLContext createTrustAllSSLContext(){
        SSLContext sslContext = null;
        try {
            sslContext = SSLContexts.custom()
                    .loadTrustMaterial(new TrustAllStrategy()).build();
        } catch (NoSuchAlgorithmException e) {
            log.error("createSSLContextWithHostName NoSuchAlgorithmException , error msg is: {}",e.getMessage());
        } catch (KeyManagementException e) {
            log.error("createSSLContextWithHostName KeyManagementException, error msg is: {}",e.getMessage());
        } catch (KeyStoreException e) {
            log.error("createSSLContextWithHostName KeyStoreException, error msg is: {}",e.getMessage());
        }
        return sslContext;
    }


    /**
     *  单向认证  且 认证hostName
     * @param trustStorePath
     * @param trustStorePasswd
     * @return
     */
    public static SSLConnectionSocketFactory createOneSSLContextFactoryWithHostName(String trustStorePath,String trustStorePasswd){
        SSLContext sslContext = null;
        SSLConnectionSocketFactory sslConnectionSocketFactory = null;
        try {
            sslContext = SSLContexts.custom().loadTrustMaterial(new File(trustStorePath), trustStorePasswd.toCharArray())
                    .build();
            //  需要做主机认证
            sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext,protocols,
                    null,
                    SSLConnectionSocketFactory.getDefaultHostnameVerifier());
        } catch (NoSuchAlgorithmException e) {
            log.error("createSSLContextWithHostName NoSuchAlgorithmException , error msg is: {}",e.getMessage());
        } catch (KeyManagementException e) {
            log.error("createSSLContextWithHostName KeyManagementException , error msg is: {}",e.getMessage());
        } catch (KeyStoreException e) {
            log.error("createSSLContextWithHostName KeyStoreException , error msg is: {}",e.getMessage());
        } catch (CertificateException e) {
            log.error("createSSLContextWithHostName CertificateException , error msg is: {}",e.getMessage());
        } catch (IOException e) {
            log.error("createSSLContextWithHostName IOException , error msg is: {}",e.getMessage());
        }
        return sslConnectionSocketFactory;
    }

    public static SSLContext createOneSSLContext(String trustStorePath,String trustStorePasswd){
        SSLContext sslContext = null;
        try {
            sslContext = SSLContexts.custom().loadTrustMaterial(new File(trustStorePath), trustStorePasswd.toCharArray())
                    .build();
                  }
        catch (NoSuchAlgorithmException e) {
            log.error("createSSLContextWithHostName NoSuchAlgorithmException , error msg is: {}",e.getMessage());
        } catch (KeyManagementException e) {
            log.error("createSSLContextWithHostName KeyManagementException , error msg is: {}",e.getMessage());
        } catch (KeyStoreException e) {
            log.error("createSSLContextWithHostName KeyStoreException , error msg is: {}",e.getMessage());
        } catch (CertificateException e) {
            log.error("createSSLContextWithHostName CertificateException , error msg is: {}",e.getMessage());
        } catch (IOException e) {
            log.error("createSSLContextWithHostName IOException , error msg is: {}",e.getMessage());
        }
        return sslContext;
    }

    /**
     *  单向认证 且  without hostName
     * @param trustStorePath
     * @param trustStorePasswd
     * @return
     */
    public static SSLConnectionSocketFactory createOneSSLContextFactoryWithOutHostName(String trustStorePath,String trustStorePasswd){
        SSLContext sslContext = null;
        SSLConnectionSocketFactory sslConnectionSocketFactory = null;
        try {
            sslContext = SSLContexts.custom().loadTrustMaterial(new File(trustStorePath), trustStorePasswd.toCharArray())
                    .build();
            //  需要做主机认证
            sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext,protocols,
                    null,
                    NoopHostnameVerifier.INSTANCE);
        } catch (NoSuchAlgorithmException e) {
            log.error("createSSLContextWithHostName NoSuchAlgorithmException , error msg is: {}",e.getMessage());
        } catch (KeyManagementException e) {
            log.error("createSSLContextWithHostName KeyManagementException , error msg is: {}",e.getMessage());
        } catch (KeyStoreException e) {
            log.error("createSSLContextWithHostName KeyStoreException , error msg is: {}",e.getMessage());
        } catch (CertificateException e) {
            log.error("createSSLContextWithHostName CertificateException , error msg is: {}",e.getMessage());
        } catch (IOException e) {
            log.error("createSSLContextWithHostName IOException , error msg is: {}",e.getMessage());
        }
        return sslConnectionSocketFactory;
    }

    /**
     *  双向认证 without hostname
     * @param keyStorePath
     * @param keyStorePassword
     * @param trustKeyStorePath
     * @param trustKeyStorePasswd
     * @return
     */
    public static SSLConnectionSocketFactory createTwoSSLContextFactoryWithOutHostName(String keyStorePath,String keyStorePassword,
                                                                                String trustKeyStorePath,String trustKeyStorePasswd){
        InputStream storeInput = null;
        SSLConnectionSocketFactory socketFactory = null;
        // load keyStore
        try {
            storeInput = new FileInputStream(keyStorePath);
            //KeyStore keyStore = KeyStore.getInstance(KEY_TYPE_PKCS);
            KeyStore keyStore = KeyStore.getInstance(KEY_TYPE_JKS);
            keyStore.load(storeInput,keyStorePassword.toCharArray());

            SSLContext sslContext = SSLContexts.custom()
                    // load keyStore
                    .loadKeyMaterial(keyStore, keyStorePassword.toCharArray())
                    // load TrustKeyStore
                    .loadTrustMaterial(new File(trustKeyStorePath), trustKeyStorePasswd.toCharArray(),
                            new TrustStrategy() { // 设置信任策略
                                @Override
                                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                                    return true;
                                }
                            }).build();
            // 非主机认证
            socketFactory = new SSLConnectionSocketFactory(sslContext,protocols,
                    null,
                    NoopHostnameVerifier.INSTANCE);
        } catch (FileNotFoundException e) {
            log.error("createSSLContextWithHostName FileNotFoundException , error msg is: {}",e.getMessage());
        } catch (IOException e) {
            log.error("createSSLContextWithHostName IOException , error msg is: {}",e.getMessage());
        } catch (CertificateException e) {
            log.error("createSSLContextWithHostName CertificateException , error msg is: {}",e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            log.error("createSSLContextWithHostName NoSuchAlgorithmException , error msg is: {}",e.getMessage());
        } catch (UnrecoverableKeyException e) {
            log.error("createSSLContextWithHostName UnrecoverableKeyException , error msg is: {}",e.getMessage());
        } catch (KeyStoreException e) {
            log.error("createSSLContextWithHostName KeyStoreException , error msg is: {}",e.getMessage());
        } catch (KeyManagementException e) {
            log.error("createSSLContextWithHostName KeyManagementException , error msg is: {}",e.getMessage());
        }
        return socketFactory;
    }

    public static SSLContext createTwoSSLContext(String keyStorePath,String keyStorePassword,
                                                                                String trustKeyStorePath,String trustKeyStorePasswd){
        InputStream storeInput = null;
        SSLConnectionSocketFactory socketFactory = null;
        SSLContext sslContext = null;
        // load keyStore
        try {
            storeInput = new FileInputStream(keyStorePath);
            //KeyStore keyStore = KeyStore.getInstance(KEY_TYPE_PKCS);
            KeyStore keyStore = KeyStore.getInstance(KEY_TYPE_JKS);
            keyStore.load(storeInput,keyStorePassword.toCharArray());

            sslContext = SSLContexts.custom()
                    // load keyStore
                    .loadKeyMaterial(keyStore, keyStorePassword.toCharArray())
                    // load TrustKeyStore
                    .loadTrustMaterial(new File(trustKeyStorePath), trustKeyStorePasswd.toCharArray(),
                            new TrustStrategy() { // 设置信任策略
                                @Override
                                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                                    return true;
                                }
                            }).build();
        } catch (FileNotFoundException e) {
            log.error("createSSLContextWithHostName FileNotFoundException , error msg is: {}",e.getMessage());
        } catch (IOException e) {
            log.error("createSSLContextWithHostName IOException , error msg is: {}",e.getMessage());
        } catch (CertificateException e) {
            log.error("createSSLContextWithHostName CertificateException , error msg is: {}",e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            log.error("createSSLContextWithHostName NoSuchAlgorithmException , error msg is: {}",e.getMessage());
        } catch (UnrecoverableKeyException e) {
            log.error("createSSLContextWithHostName UnrecoverableKeyException , error msg is: {}",e.getMessage());
        } catch (KeyStoreException e) {
            log.error("createSSLContextWithHostName KeyStoreException , error msg is: {}",e.getMessage());
        } catch (KeyManagementException e) {
            log.error("createSSLContextWithHostName KeyManagementException , error msg is: {}",e.getMessage());
        }
        return sslContext;
    }

    /**
     *  双向认证  with  hostName
     * @param keyStorePath
     * @param keyStorePassword
     * @param trustKeyStorePath
     * @param trustKeyStorePasswd
     * @return
     */
    public static SSLConnectionSocketFactory createTwoSSLContextFactoryWithHostName(String keyStorePath,String keyStorePassword,
                                                                                String trustKeyStorePath,String trustKeyStorePasswd){
        InputStream storeInput = null;
        SSLConnectionSocketFactory socketFactory = null;
        // load keyStore
        try {
            storeInput = new FileInputStream(keyStorePath);
            //KeyStore keyStore = KeyStore.getInstance(KEY_TYPE_PKCS);
            KeyStore keyStore = KeyStore.getInstance(KEY_TYPE_JKS);
            keyStore.load(storeInput,keyStorePassword.toCharArray());

            SSLContext sslContext = SSLContexts.custom()
                    // load keyStore
                    .loadKeyMaterial(keyStore, keyStorePassword.toCharArray())
                    // load TrustKeyStore
                    .loadTrustMaterial(new File(trustKeyStorePath), trustKeyStorePasswd.toCharArray(),
                            new TrustStrategy() { // 设置信任策略
                                @Override
                                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                                    return true;
                                }
                            }).build();
            // 主机认证
            socketFactory = new SSLConnectionSocketFactory(sslContext,protocols,
                    null,
                    SSLConnectionSocketFactory.getDefaultHostnameVerifier());
        } catch (FileNotFoundException e) {
            log.error("createSSLContextWithHostName FileNotFoundException , error msg is: {}",e.getMessage());
        } catch (IOException e) {
            log.error("createSSLContextWithHostName IOException , error msg is: {}",e.getMessage());
        } catch (CertificateException e) {
            log.error("createSSLContextWithHostName CertificateException , error msg is: {}",e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            log.error("createSSLContextWithHostName NoSuchAlgorithmException , error msg is: {}",e.getMessage());
        } catch (UnrecoverableKeyException e) {
            log.error("createSSLContextWithHostName UnrecoverableKeyException , error msg is: {}",e.getMessage());
        } catch (KeyStoreException e) {
            log.error("createSSLContextWithHostName KeyStoreException , error msg is: {}",e.getMessage());
        } catch (KeyManagementException e) {
            log.error("createSSLContextWithHostName KeyManagementException , error msg is: {}",e.getMessage());
        }
        return socketFactory;
    }
}
