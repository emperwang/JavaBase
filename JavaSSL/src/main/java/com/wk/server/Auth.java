package com.wk.server;


import com.wk.tool.FilePathUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Properties;

public class Auth {
    private Logger log = LoggerFactory.getLogger(Auth.class);
    public static SSLContext getSSLContext() throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException {
        Properties prop = FilePathUtil.getProp("config.properties");
        String protocol = prop.getProperty("protocol");
        String serverCer = prop.getProperty("serverjks");
        String serverCerPwd = prop.getProperty("servercerpwd");
        String serverTrustKey = prop.getProperty("servertrust");
        String serverTrustKeyPwd = prop.getProperty("servertrustpwd");
        int authority = Integer.parseInt(prop.getProperty("authority"));

        // 服务器端证书
        KeyStore certKeyStore = KeyStore.getInstance("JKS");
        certKeyStore.load(new FileInputStream(serverCer),
                serverCerPwd.toCharArray());

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        keyManagerFactory.init(certKeyStore,
                    serverCerPwd.toCharArray());
        KeyManager[] keyManagers = keyManagerFactory.getKeyManagers();

        TrustManager[] trustManagers={};
        if (authority == 2) {
            // server trust keystore
            KeyStore trustKeyStore = KeyStore.getInstance("JKS");
            trustKeyStore.load(new FileInputStream(serverTrustKey),
                    serverTrustKeyPwd.toCharArray());
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
            trustManagerFactory.init(trustKeyStore);
            trustManagers = trustManagerFactory.getTrustManagers();
        }
        SSLContext sslContext = SSLContext.getInstance("TLSV1.2");
        sslContext.init(keyManagers,trustManagers,null);
        return sslContext;
    }

}
