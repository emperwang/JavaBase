package com.wk.httpClientUtil;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Slf4j
public class ClientFactory {

    public static final String KEY_TYPE_PKCS = "PKCS12";
    public static final String KEY_TYPE_JKS = "JKS";
    /**
     *  http请求客户端
     * @param
     * @return
     */
    public static CloseableHttpClient httpClient(Integer connectTimeout,Integer socketTime){
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(connectTimeout).setSocketTimeout(socketTime).build();
        CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
        return httpClient;
    }

    /**
     *  HTTPS 绕过证书
     * @return
     */
    public static CloseableHttpClient createHttpSSLClient(Integer socketTime,Integer connectTimeout){
        try{
            SSLContext sslContext = SSLContexts.custom()
                    .loadTrustMaterial(new TrustAllStrategy()).build();
            String[] protocols = {"TLSv1.2","TLSv1.1","SSLv3"};
            // 此处会认证主机  hostName
/*            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext,protocols,
                    null,
                    SSLConnectionSocketFactory.getDefaultHostnameVerifier());*/
            // 此不会认证主机名字
            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext,protocols,
                    null,
                    NoopHostnameVerifier.INSTANCE);
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(socketTime)
                    .setConnectTimeout(connectTimeout)
                    .build();
            CloseableHttpClient client = HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory)
                    .setDefaultRequestConfig(requestConfig)
                    .build();
            return client;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *  HTTPS单向认证
     * @return
     */
    public static CloseableHttpClient createHttpSSLClient(String trustStorePath,
                                                          String trustStorePasswd,Integer socketTimeOut,Integer connectTimeout)  {
        try{
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(new File(trustStorePath), trustStorePasswd.toCharArray())
                    .build();
            String[] protocols = {"TLSv1.2","TLSv1.1","SSLv3"};
/*            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext,protocols,
                                                                    null,
                                                                    SSLConnectionSocketFactory.getDefaultHostnameVerifier());*/

            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext,protocols,
                                                                    null,
                                                                    NoopHostnameVerifier.INSTANCE);

            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeOut)
                    .setConnectTimeout(connectTimeout).build();
            CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory)
                    .setDefaultRequestConfig(requestConfig).build();
            return httpClient;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *  https 双向认证
     * @param keyStorePath
     * @return
     */
    public static CloseableHttpClient createHttpSSLClient(String keyStorePath,String keyStorePassword,
                                                  String trustKeyStorePath,String trustKeyStorePasswd,Integer socketTimeOut,Integer connectTimeout )  {
        InputStream storeInput = null;
        try{
            storeInput = new FileInputStream(keyStorePath);
            //KeyStore keyStore = KeyStore.getInstance(KEY_TYPE_PKCS);
            KeyStore keyStore = KeyStore.getInstance(KEY_TYPE_JKS);
            keyStore.load(storeInput,keyStorePassword.toCharArray());
            SSLContext sslContext = SSLContexts.custom()
                    .loadKeyMaterial(keyStore, keyStorePassword.toCharArray())
                    .loadTrustMaterial(new File(trustKeyStorePath), trustKeyStorePasswd.toCharArray(),
                            new TrustStrategy() {
                                @Override
                                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                                    return true;
                                }
                            }).build();
            String[] protocols = {"TLSv1.2","TLSv1.1","SSLv3"};
/*            SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext,protocols,
                    null,
                    SSLConnectionSocketFactory.getDefaultHostnameVerifier());*/
            SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext,protocols,
                    null,
                    NoopHostnameVerifier.INSTANCE);
            RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(connectTimeout).setSocketTimeout(socketTimeOut).build();
            CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory)
                    .setDefaultRequestConfig(requestConfig).build();
            return httpClient;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
