package com.wk.client;

import com.wk.tool.FilePathUtil;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Properties;

public class Auth {
    private static SSLContext sslContext;

    public static SSLContext getSslContext() throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException {
        Properties prop = FilePathUtil.getProp("config.properties");
        String clientTrustCertFile = prop.getProperty("clienttrust");
        String clientTrustCertPwd = prop.getProperty("clienttrustpwd");
        String clientCer = prop.getProperty("clientjks");
        String clientCerPwd = prop.getProperty("clientcerpwd");
        String protocol = prop.getProperty("protocol");
        int authority = Integer.parseInt(prop.getProperty("authority"));
        // TrustKeyStore
        KeyStore trustKeyStore = KeyStore.getInstance("JKS");
        trustKeyStore.load(new FileInputStream(clientTrustCertFile),
                clientTrustCertPwd.toCharArray());

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
        trustManagerFactory.init(trustKeyStore);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();

        // clientKeyStore
        KeyManager[] keyManagers = {};
        if (authority == 2) {// 客户端证书，服务端认证使用
            KeyStore clientCerKeyStore = KeyStore.getInstance("JKS");
            clientCerKeyStore.load(new FileInputStream(clientCer),
                    clientCerPwd.toCharArray());

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            keyManagerFactory.init(clientCerKeyStore, clientCerPwd.toCharArray());
            keyManagers = keyManagerFactory.getKeyManagers();
        }
        SSLContext sslContext = SSLContext.getInstance("TLSV1.2");
        sslContext.init(keyManagers,trustManagers,null);

        return sslContext;
    }
}
