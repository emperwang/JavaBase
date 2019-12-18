package com.wk.httpClientUtil.pooled;

import com.wk.concurrent.ThreadPoolUtil;
import com.wk.httpClientUtil.HttpConfig;
import com.wk.httpClientUtil.HttpHeader;
import com.wk.httpClientUtil.HttpMethods;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.impl.client.CloseableHttpClient;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * descripiton:
 *
 * @author: wk
 * @time: 16:10 2019/12/18
 * @modifier:
 */
@Slf4j
public class testPoolClient {
    private static ExecutorService service = ThreadPoolUtil.fixedPool(3);
    private static String[] urls = {"http://192.168.72.1:8989/user/getuser", "http://192.168.72.1:8988/user/getuser",
            "http://192.168.72.1:8987/user/getuser"};
    public static void testPoolClient(){
        for (int i = 0; i < 4; i++) {
            cmdExecutor executor = new cmdExecutor(urls);
            service.submit(executor);
        }

        service.shutdown();
        try {
            service.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        testPoolClient();

        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static class cmdExecutor implements Runnable{
        private String[] urls;
        public cmdExecutor(String[] url){
            this.urls = url;
        }

        @Override
        public void run() {
            Header[] headers = HttpHeader.instance().contentType(HttpHeader.Headers.TEXT_JSON_UTF8).build();
            for (int i=0; i < urls.length; i++) {
                log.info("count : {}, name:{}",i , Thread.currentThread().getName());
                CloseableHttpClient httpClient = ClientFactoryPool.httpClientPooled(5000, 5000);
                HttpConfig config = HttpConfig.instance().url(urls[i]).client(httpClient).header(headers)
                        .methods(HttpMethods.GET);
                Map<String, String> map = HttpClientUtilPooled.httpGetMethodWithStatusCode(config);
                log.info("name:{}, result :{}" ,Thread.currentThread().getName(), map.toString());
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
