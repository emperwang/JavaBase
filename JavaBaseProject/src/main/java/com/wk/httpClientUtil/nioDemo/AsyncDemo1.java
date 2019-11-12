package com.wk.httpClientUtil.nioDemo;

import com.wk.httpClientUtil.CustomSSLContextFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.reactor.IOReactorException;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class AsyncDemo1 {
    private static final String ClientKey = "D:\\certificate\\client\\client.keystore";
    private static final String clientKeyPass = "123456";
    private static final String clientTrust = "D:\\certificate\\client\\clientTrust";
    private static final String clientTrustPass = "123456";

    public static void main(String[] args) throws InterruptedException {
        //AsyncClient1();
       // AsyncClientWithPool();
       // AsyncHttpsClientWithPoolWithoutHostName();
        AsyncHttpsClientWithPoolTwoWithHostName();
    }

    public static void AsyncHttpsClientWithPoolTwoWithHostName() throws InterruptedException {
        String httpsUrl = "https://192.168.72.1:9999/getUser.do";
        String httpUrl = "http://192.168.72.1:8888/getUser.do";
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(100) //从连接池中获取连接时 最大时间，获取不到就报错
                .setConnectTimeout(50000)       // 创建tcp连接时的最大时间,超过则报错
                .setSocketTimeout(50000)        // 接收数据时,数据间的最大间隔
                .build();
        // 配置io线程
        IOReactorConfig reactorConfig = IOReactorConfig.custom()
                .setIoThreadCount(Runtime.getRuntime().availableProcessors())
                .setSoKeepAlive(true)
                .build();
        // 设置连接池
        DefaultConnectingIOReactor ioReactor = null;
        try {
            ioReactor = new DefaultConnectingIOReactor(reactorConfig);
        } catch (IOReactorException e) {
            e.printStackTrace();
        }
        //SSLContext contextWithOutHostName = CustomSSLContextFactory.createOneSSLContext(clientTrust, clientTrustPass);
        SSLContext twoSSLContext = CustomSSLContextFactory.createTwoSSLContext(ClientKey, clientKeyPass, clientTrust, clientTrustPass);
        PoolingNHttpClientConnectionManager poolingNHttpClientConnectionManager =
                new PoolingNHttpClientConnectionManager(ioReactor);
        poolingNHttpClientConnectionManager.setMaxTotal(10); // 最大连接数
        poolingNHttpClientConnectionManager.setDefaultMaxPerRoute(5); //

        CloseableHttpAsyncClient asyncClient = HttpAsyncClients.custom()
                .setConnectionManager(poolingNHttpClientConnectionManager)
                .setDefaultRequestConfig(requestConfig)
                .setSSLContext(twoSSLContext)
                //.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .setSSLHostnameVerifier(SSLConnectionSocketFactory.getDefaultHostnameVerifier())
                .build();
        asyncClient.start();
        CountDownLatch latch = new CountDownLatch(5);
        for (int i = 0; i< 5; i++){
            HttpGet httpGet = new HttpGet(httpsUrl);
            asyncClient.execute(httpGet, new CallBackImple(latch));
        }
        log.info("this is main thread running............., id = {}",Thread.currentThread().getId());
        log.info("main thread begin to wait client return ... ");
        latch.await();
        try {
            asyncClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void AsyncHttpsClientWithPoolWithoutHostName() throws InterruptedException {
        String httpsUrl = "https://192.168.72.1:9999/getUser.do";
        String httpUrl = "http://192.168.72.1:8888/getUser.do";
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(100) //从连接池中获取连接时 最大时间，获取不到就报错
                .setConnectTimeout(50000)       // 创建tcp连接时的最大时间,超过则报错
                .setSocketTimeout(50000)        // 接收数据时,数据间的最大间隔
                .build();
        // 配置io线程
        IOReactorConfig reactorConfig = IOReactorConfig.custom()
                .setIoThreadCount(Runtime.getRuntime().availableProcessors())
                .setSoKeepAlive(true)
                .build();
        // 设置连接池
        DefaultConnectingIOReactor ioReactor = null;
        try {
            ioReactor = new DefaultConnectingIOReactor(reactorConfig);
        } catch (IOReactorException e) {
            e.printStackTrace();
        }
        SSLContext contextWithOutHostName = CustomSSLContextFactory.createOneSSLContext(clientTrust, clientTrustPass);
        PoolingNHttpClientConnectionManager poolingNHttpClientConnectionManager =
                new PoolingNHttpClientConnectionManager(ioReactor);
        poolingNHttpClientConnectionManager.setMaxTotal(10); // 最大连接数
        poolingNHttpClientConnectionManager.setDefaultMaxPerRoute(5); //

        CloseableHttpAsyncClient asyncClient = HttpAsyncClients.custom()
                .setConnectionManager(poolingNHttpClientConnectionManager)
                .setDefaultRequestConfig(requestConfig)
                .setSSLContext(contextWithOutHostName)
                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                //.setSSLHostnameVerifier(SSLConnectionSocketFactory.getDefaultHostnameVerifier())
                .build();
        asyncClient.start();
        CountDownLatch latch = new CountDownLatch(5);
        for (int i = 0; i< 5; i++){
            HttpGet httpGet = new HttpGet(httpsUrl);
            asyncClient.execute(httpGet, new CallBackImple(latch));
        }
        log.info("this is main thread running............., id = {}",Thread.currentThread().getId());
        log.info("main thread begin to wait client return ... ");
        latch.await();
        try {
            asyncClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void AsyncClientWithPool() throws InterruptedException {
        String httpUrl = "http://192.168.72.1:8888/getUser.do";
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(100) //从连接池中获取连接时 最大时间，获取不到就报错
                .setConnectTimeout(50000)       // 创建tcp连接时的最大时间,超过则报错
                .setSocketTimeout(50000)        // 接收数据时,数据间的最大间隔
                .build();
        // 配置io线程
        IOReactorConfig reactorConfig = IOReactorConfig.custom()
                .setIoThreadCount(Runtime.getRuntime().availableProcessors())
                .setSoKeepAlive(true)
                .build();
        // 设置连接池
        DefaultConnectingIOReactor ioReactor = null;
        try {
            ioReactor = new DefaultConnectingIOReactor(reactorConfig);
        } catch (IOReactorException e) {
            e.printStackTrace();
        }
        PoolingNHttpClientConnectionManager poolingNHttpClientConnectionManager =
                    new PoolingNHttpClientConnectionManager(ioReactor);
        poolingNHttpClientConnectionManager.setMaxTotal(10); // 最大连接数
        poolingNHttpClientConnectionManager.setDefaultMaxPerRoute(5); //

        CloseableHttpAsyncClient asyncClient = HttpAsyncClients.custom()
                .setConnectionManager(poolingNHttpClientConnectionManager)
                .setDefaultRequestConfig(requestConfig)
                .build();
        asyncClient.start();
        CountDownLatch latch = new CountDownLatch(5);
        for (int i = 0; i< 5; i++){
            HttpGet httpGet = new HttpGet(httpUrl);
            asyncClient.execute(httpGet, new CallBackImple(latch));
        }
        log.info("this is main thread running............., id = {}",Thread.currentThread().getId());
        log.info("main thread begin to wait client return ... ");
        latch.await();
        try {
            asyncClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static void AsyncClient1() throws InterruptedException {
        String httpsUrl = "https://192.168.72.1:9999/getUser.do";
        String httpUrl = "http://192.168.72.1:8888/getUser.do";
        CloseableHttpAsyncClient asyncClient =
                HttpAsyncClients.custom().build();
        asyncClient.start();
        HttpGet httpGet = new HttpGet(httpUrl);
        CountDownLatch latch = new CountDownLatch(1);
        asyncClient.execute(httpGet, new FutureCallback<HttpResponse>() {
            @Override
            public void completed(HttpResponse result) {
                latch.countDown();
                log.info("current thread is : {}",Thread.currentThread().getId());
                log.info("asyncclient complete..................");
            }

            @Override
            public void failed(Exception ex) {
                latch.countDown();
                log.info("current thread is : {}",Thread.currentThread().getId());
                log.info("asyncclient failed ................");
            }

            @Override
            public void cancelled() {
                latch.countDown();
                log.info("current thread is : {}",Thread.currentThread().getId());
                log.info("asyncClient cancelled .....................");
            }
        });
        log.info("this is main thread running............., id = {}",Thread.currentThread().getId());
        log.info("main thread begin to wait client return ... ");
        latch.await();
        try {
            asyncClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
