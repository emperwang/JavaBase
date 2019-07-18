package com.wk.encryption.baseEncryption;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

public class SSLCoder extends CertificateCoder {
    public static final String KEY_STORE = "JKS";

    public static final String X509 = "X.509";

    public static final String SunX509 = "SunX509";
    public static final String SSL = "SSL";

    /**
     *  获得SSLSocketFactory
     * @param password  密码
     * @param keystorepath 密钥库路径
     * @param trustKeyStorePath 信任库路径
     * @return
     */
    public static SSLSocketFactory getSSLSocketFactory(String password,String keystorepath,
                                                       String trustKeyStorePath) throws NoSuchAlgorithmException, CertificateException, KeyStoreException, IOException, UnrecoverableKeyException, KeyManagementException {
        // 初始化密钥库
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(SunX509);
        KeyStore keyStore = getKeyStore(keystorepath, password);
        keyManagerFactory.init(keyStore,password.toCharArray());
        // 初始化信任库
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(SunX509);
        KeyStore keyStore1 = getKeyStore(trustKeyStorePath, password);
        trustManagerFactory.init(keyStore1);
        // 初始化SSL上下文
        SSLContext sslContext = SSLContext.getInstance(SSL);
        sslContext.init(keyManagerFactory.getKeyManagers(),trustManagerFactory.getTrustManagers(),null);
        SSLSocketFactory socketFactory = sslContext.getSocketFactory();
        return socketFactory;

    }

    /**
     *  为HttpsURLConnection 配置SSLSocketFactory
     * @param connection
     * @param password
     * @param keyStorePath
     * @param trustKeyStorePath
     */
    public static void configSSLSocketFactory(HttpsURLConnection connection,
                                              String password,
                                              String keyStorePath,
                                              String trustKeyStorePath) throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        connection.setSSLSocketFactory(getSSLSocketFactory(password,keyStorePath,trustKeyStorePath));
    }
}
