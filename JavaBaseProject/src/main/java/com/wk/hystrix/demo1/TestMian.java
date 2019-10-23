package com.wk.hystrix.demo1;

import lombok.extern.slf4j.Slf4j;
import rx.Observable;
import rx.Subscriber;

@Slf4j
public class TestMian {
    public static void main(String[] args) {
        MyService myService = new MyService();

        for (int i=0; i < 15; i++){
            MyCommand myCommand = new MyCommand("HystrixGroup", "Fishing" + (i * i));
            myCommand.setMyService(myService);
            Observable<String> observable = myCommand.toObservable();
            observable.subscribe(new Subscriber<String>() {
                @Override
                public void onCompleted() {
                    log.info("complete ......");
                }

                @Override
                public void onError(Throwable throwable) {
                    log.info("Error ......");
                    log.error(throwable.getMessage());
                }

                @Override
                public void onNext(String s) {
                    log.info("next value is :{}",s);
                }
            });
        }
    }
}
