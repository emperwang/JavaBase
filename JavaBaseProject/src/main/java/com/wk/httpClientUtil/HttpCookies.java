package com.wk.httpClientUtil;

import org.apache.http.client.CookieStore;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;

public class HttpCookies {

    // 设置和携带cookie
    private HttpClientContext context;
    // 存储cookie
    private CookieStore cookieStore;

    private HttpCookies(){
        this.context = new HttpClientContext();
        this.cookieStore = new BasicCookieStore();
        this.context.setCookieStore(cookieStore);
    }

    public static HttpCookies instance(){
        return new HttpCookies();
    }

    public HttpClientContext getContext() {
        return context;
    }

    public CookieStore getCookieStore() {
        return cookieStore;
    }
}
