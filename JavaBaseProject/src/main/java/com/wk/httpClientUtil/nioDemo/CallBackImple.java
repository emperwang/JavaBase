package com.wk.httpClientUtil.nioDemo;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.concurrent.FutureCallback;

import java.util.concurrent.CountDownLatch;

@Slf4j
public class CallBackImple implements FutureCallback<HttpResponse> {

    private CountDownLatch latch;

    public CallBackImple(CountDownLatch latch) {
        this.latch = latch;
    }

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
}
