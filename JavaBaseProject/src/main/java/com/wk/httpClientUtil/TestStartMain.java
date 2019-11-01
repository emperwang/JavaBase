package com.wk.httpClientUtil;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 *
 */
@Slf4j
public class TestStartMain {

    private static String trustKey = "F:\\github_code\\Mine\\springbootDemo\\HTTPS_Interface\\src\\main\\resources\\clienttrust.key";
    private static String trustKey2 = "D:\\certificate\\client\\clientTrust";
    private static String trustKeyPwd="123456";
    private static Integer socketTimeOut = 10000;
    private static Integer connectTimeout = 10000;

    private static String clientKey = "F:\\github_code\\Mine\\springbootDemo\\HTTPS_Interface\\src\\main\\resources\\sslclient.key";
    private static String clientKey2 = "D:\\certificate\\client\\client.keystore";
    private static String clientKeyPwd = "123456";

    /**
     *  绕过认证测试
     */
    public static void trustAll() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        // CloseableHttpClient httpSSLClient = ClientFactory.createHttpSSLClient(socketTimeOut, connectTimeout);
        // 带连接池的测试
        CloseableHttpClient httpSSLClient = ClientFactory.createHttpClientWithPoolNoAuth(socketTimeOut, connectTimeout);
        HttpConfig config = HttpConfig.instance().client(httpSSLClient)
                .methods(HttpMethods.GET)
                .url("https://10.154.129.144:9999/getUser");
        String s = HttpClientUtil.httpGetMethod(config);
        log.info("trustAll authentication..:"+s);
    }

    /**
     *  单向认证测试
     */
    public static void authenticationOne(){
        // CloseableHttpClient httpSSLClient = ClientFactory.createHttpSSLClient(trustKey, trustKeyPwd, socketTimeOut, connectTimeout);
        // 带连接池的测试
        CloseableHttpClient httpSSLClient = ClientFactory.createHttpSSLClientOneAuthenticateAndHostNameWithPool(clientKey2, trustKeyPwd,
                                                                                                socketTimeOut, connectTimeout);

        HttpConfig config = HttpConfig.instance().client(httpSSLClient)
                .methods(HttpMethods.GET)
                .url("https://169.254.110.194:9999/getUser");
        String s = HttpClientUtil.httpGetMethod(config);
        log.info("one way authentication..:"+s);
    }

    /**
     * 双向认证测试
     */
    public static void authenticationDouble(){
        /*CloseableHttpClient httpSSLClient = ClientFactory.createHttpSSLClient(clientKey, clientKeyPwd,
                trustKey, trustKeyPwd, socketTimeOut, connectTimeout);*/
        CloseableHttpClient httpSSLClient = ClientFactory.createHttpSSLClientDoubleAuthenticateAndHostNameWithPool(clientKey2,
                clientKeyPwd,
                trustKey, trustKeyPwd, socketTimeOut, connectTimeout);
        HttpConfig config = HttpConfig.instance().client(httpSSLClient)
                .methods(HttpMethods.GET)
                .url("https://169.254.110.194:9999/getUser");
        String s = HttpClientUtil.httpGetMethod(config);
        log.info("double authentication..:"+s);
    }
    // fd15:4ba5:5a2b:1008:3511:6c4f:3082:a429
    public static void sendRequestWithIpv6() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        CloseableHttpClient httpClient = ClientFactory.createHttpClientWithPoolNoAuth(10000, 10000);
        HttpConfig httpConfig = HttpConfig.instance().client(httpClient)
                .methods(HttpMethods.GET)
                .url("http://[fd15:4ba5:5a2b:1008:3511:6c4f:3082:a429]:8989/user/getuser");
        String result = HttpClientUtil.httpGetMethod(httpConfig);
        System.out.println("result is :"+result);
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        // trustAll();
        // authenticationOne();
//        authenticationDouble();
        sendRequestWithIpv6();
    }
}
