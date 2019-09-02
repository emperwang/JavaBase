package com.wk.httpClientUtil;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HeaderElement;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

/**
 *  此报错就跟主机名认证有关
 * 用HttpClient发送HTTPS请求报SSLException: Certificate for <域名>
 *      doesn't match any of the subject alternative names
 */
@Slf4j
public class ClientFactory {

    public static final String KEY_TYPE_PKCS = "PKCS12";
    public static final String KEY_TYPE_JKS = "JKS";

    private static boolean shutdown = false;

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

    /**
     * 设置连接池的第一种方式
     * 绕过认证,并设置连接池
     */
    public static CloseableHttpClient createHttpClientWithPoolNoAuth(Integer socketTime,Integer connectTimeout) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socketTime)
                .setConnectTimeout(connectTimeout)
                .build();
        String[] protocols = {"TLSv1.2","TLSv1.1","SSLv3"};
        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(new TrustAllStrategy()).build();
        // 不认证host
        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext,
                                                                protocols,
                                                                null,
                                                                NoopHostnameVerifier.INSTANCE);
        // 连接池
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                                                    .register("http", new PlainConnectionSocketFactory())
                                                    .register("https", sslConnectionSocketFactory)
                                                    .build();
        PoolingHttpClientConnectionManager pcm = new PoolingHttpClientConnectionManager(registry);
        // 设置最大连接数
        pcm.setMaxTotal(1000);
        // 设置每个路由基础的连接
        pcm.setDefaultMaxPerRoute(20);
        CloseableHttpClient httpClient = HttpClients.custom()
                                                    .setConnectionManager(pcm)
                                                    .setDefaultRequestConfig(requestConfig)
                                                    .build();
        return httpClient;
    }
    /**
     * 设置连接池的第二种方式
     * 绕过认证,并设置连接池
     */
    public static CloseableHttpClient createHttpClientWithPoolNoAuth2(Integer socketTime,Integer connectTimeout) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socketTime)
                .setConnectTimeout(connectTimeout)
                .build();
        String[] protocols = {"TLSv1.2","TLSv1.1","SSLv3"};
        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(new TrustAllStrategy()).build();
        // 不认证host
        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext,
                protocols,
                null,
                NoopHostnameVerifier.INSTANCE);
        // 连接池
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", new PlainConnectionSocketFactory())
                .register("https", sslConnectionSocketFactory)
                .build();
        PoolingHttpClientConnectionManager pcm = new PoolingHttpClientConnectionManager(registry);
        // 设置最大连接数
        pcm.setMaxTotal(1000);
        // 设置每个路由基础的连接
        pcm.setDefaultMaxPerRoute(20);

        // 设置保持活跃策略
        ConnectionKeepAliveStrategy aliveStrategy = new ConnectionKeepAliveStrategy() {
            @Override
            public long getKeepAliveDuration(HttpResponse response,
                                             HttpContext context) {
                // 获取 keep-alive http报文头
                BasicHeaderElementIterator iterator = new BasicHeaderElementIterator(response.headerIterator(HTTP.CONN_KEEP_ALIVE));
                while (iterator.hasNext()){
                    HeaderElement headerElement = iterator.nextElement();
                    String paramName = headerElement.getName();
                    String headerValue = headerElement.getValue();
                    if (null != headerValue && "timeout".equalsIgnoreCase(paramName)){
                        return Long.parseLong(headerValue) * 1000;
                    }
                }
                // 默认保持20s
                return 20*1000;
            }
        };
        //
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                                                          .setConnectionManager(pcm)
                                                          .setKeepAliveStrategy(aliveStrategy)
                                                          .setDefaultRequestConfig(requestConfig)
                                                          .build();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!shutdown){
                    synchronized (this){
                        // 关闭过期的连接
                        pcm.closeExpiredConnections();
                        // 关闭超过40s的空闲连接
                        pcm.closeIdleConnections(40, TimeUnit.SECONDS);
                    }
                }
            }
        }).start();

        return httpClient;
    }

    /**
     *  HTTPS单向认证,并设置连接池
     * @return
     */
    public static CloseableHttpClient createHttpSSLClientOneAuthenticateWithPool(String trustStorePath,
                                                          String trustStorePasswd,Integer socketTimeOut,Integer connectTimeout)  {
        try{
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeOut)
                    .setConnectTimeout(connectTimeout).build();
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(new File(trustStorePath), trustStorePasswd.toCharArray())
                    .build();
            String[] protocols = {"TLSv1.2","TLSv1.1","SSLv3"};
            /*SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext,protocols,
                                                                    null,
                                                                    SSLConnectionSocketFactory.getDefaultHostnameVerifier());*/
            // 不做主机名认证
            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext,protocols,
                    null,
                    NoopHostnameVerifier.INSTANCE);

            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                                                                        .register("http",new PlainConnectionSocketFactory())
                                                                        .register("https",sslConnectionSocketFactory)
                                                                        .build();
            PoolingHttpClientConnectionManager pcm = new PoolingHttpClientConnectionManager(registry);
            CloseableHttpClient httpClient = HttpClients.custom()
                                                        .setConnectionManager(pcm)
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
    public static CloseableHttpClient createHttpSSLClientDoubleAuthenticateWithPool(String keyStorePath,String keyStorePassword,
                                                          String trustKeyStorePath,String trustKeyStorePasswd,Integer socketTimeOut,Integer connectTimeout )  {
        InputStream storeInput = null;
        try{
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(connectTimeout)
                    .setSocketTimeout(socketTimeOut).build();
            // load keyStore
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
            String[] protocols = {"TLSv1.2","TLSv1.1","SSLv3"};
            /*SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext,protocols,
                    null,
                    SSLConnectionSocketFactory.getDefaultHostnameVerifier());*/
            // 非主机认证
            SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext,protocols,
                                                                                        null,
                                                                                        NoopHostnameVerifier.INSTANCE);
            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                                                                        .register("http",new PlainConnectionSocketFactory())
                                                                        .register("https",socketFactory)
                                                                        .build();
            PoolingHttpClientConnectionManager pcm = new PoolingHttpClientConnectionManager(registry);
            pcm.setMaxTotal(1000);
            CloseableHttpClient httpClient = HttpClients.custom()
                                                        .setConnectionManager(pcm)
                                                        .setDefaultRequestConfig(requestConfig)
                                                        .build();
            return httpClient;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
