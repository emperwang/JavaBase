package com.wk.httpClientUtil;

import lombok.Getter;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;

import java.util.Map;

@Getter
public class HttpConfig {

    public static final String ConnectionTimeOut = "httpclient_connection_timeout";
    public static final String SocketTimeOut = "httpclient_socket_timeout";

    private HttpConfig(){}

    public static HttpConfig instance(){
        return new HttpConfig();
    }

    /**
     *  请求
     */
    private HttpClient client;
    /**
     *  请求头
     */
    private Header[] headers;
    /**
     *  请求设置
     */
    private RequestConfig requestConfig;
    /**
     *  post 还是get等请求
     */
    private HttpMethods methods;
    /**
     *  设置cookie使用
     */
    private HttpClientContext context;
    /**
     *  请求的参数
     */
    private Map<String,String> paramMap;
    /**
     *  bean参数,JSON格式
     */
    private String beanParam;

    private String url;

    public HttpConfig url(String url){
        this.url = url;
        return this;
    }

    public HttpConfig beanParam(String beanParam){
        this.beanParam = beanParam;
        return this;
    }

    public HttpConfig header(Header[] headers){
        this.headers = headers;
        return this;
    }

    public HttpConfig requestConfig(RequestConfig config){
        this.requestConfig = config;
        return this;
    }

    public HttpConfig client(HttpClient client){
        this.client = client;
        return this;
    }


    public HttpConfig methods(HttpMethods methods){
        this.methods = methods;
        return this;
    }

    public HttpConfig context(HttpClientContext context){
        this.context = context;
        return this;
    }

    public HttpConfig paramMap(Map<String,String> map){
        this.paramMap.putAll(map);
        return this;
    }

}
