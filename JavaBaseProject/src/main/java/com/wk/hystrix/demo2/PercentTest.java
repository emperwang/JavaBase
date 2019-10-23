package com.wk.hystrix.demo2;

import lombok.extern.slf4j.Slf4j;
import rx.Observable;
import rx.Subscriber;

@Slf4j
public class PercentTest {

    public static void main(String[] args) {

        PercentService percentService = new PercentService();
        for (int i=0;i<15;i++){
            PercentCommand command = new PercentCommand("testGroup", i);
            command.setService(percentService);
            Observable<String> observable = command.toObservable();

            observable.subscribe(new Subscriber<String>() {
                @Override
                public void onCompleted() {
                    log.info("Complete");
                }

                @Override
                public void onError(Throwable throwable) {
                    log.error("error,msg is: {}",throwable.getMessage());
                }

                @Override
                public void onNext(String s) {
                    log.info("next value is :{}",s);
                }
            });
        }
    }
}
