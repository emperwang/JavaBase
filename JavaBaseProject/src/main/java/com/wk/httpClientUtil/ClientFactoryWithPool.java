package com.wk.httpClientUtil;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Set;

/**
 * descripiton:
 *  设置httpclient pool
 * @author: wk
 * @time: 14:20 2019/12/17
 * @modifier:
 */

@Slf4j
public class ClientFactoryWithPool {
    // 池化管理
    private static PoolingHttpClientConnectionManager poolConnManager;
    // 是线程安全的，所有的线程可以使用它一起发送http 请求
    private static CloseableHttpClient httpClient;

    static {
        // https
        SSLContextBuilder contextBuilder = new SSLContextBuilder();
        try {
            contextBuilder.loadTrustMaterial(null,new TrustSelfSignedStrategy());
            SSLConnectionSocketFactory sslFactory = new SSLConnectionSocketFactory(contextBuilder.build());
            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create().register("http",
                    PlainConnectionSocketFactory.getSocketFactory())
                    .register("https", sslFactory).build();
            poolConnManager = new PoolingHttpClientConnectionManager(registry);
            poolConnManager.setMaxTotal(400);
            poolConnManager.setDefaultMaxPerRoute(200);
            httpClient = getConnection();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

    private static CloseableHttpClient getConnection(){

        RequestConfig config = RequestConfig.custom().setConnectTimeout(50000).setSocketTimeout(50000).setConnectionRequestTimeout(50000)
                .build();
        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(config)
                .setConnectionManager(poolConnManager)
                .setRetryHandler(new DefaultHttpRequestRetryHandler(2, false)).build();

        return httpClient;
    }


    public static CloseableHttpClient getHttpClient() {
        Set<HttpRoute> routes = poolConnManager.getRoutes();
        for (HttpRoute route : routes) {
            String s = route.toString();
            log.info("route : {}", s);
        }
        return httpClient;
    }
}