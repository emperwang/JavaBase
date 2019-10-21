package com.wk.rxjava;


import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class BaseDemo {
    // 实例一
    public static void demostration1(){
       // 被观察者
        Observable observable = Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(ObservableEmitter observableEmitter) throws Exception {
                observableEmitter.onNext(1);
                observableEmitter.onNext(2);
                observableEmitter.onNext(3);
                observableEmitter.onComplete();
            }
        });

        // 创建观察者
        Observer<Integer> observer = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable disposable) {

            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("onNext is :"+integer);
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {
                System.out.println("Complete");
            }
        };
        // 事件订阅
        observable.subscribe(observer);
    }

    public static void demostration2(){
        // 链式结构
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> observableEmitter) throws Exception {
                observableEmitter.onNext(1);
                observableEmitter.onNext(2);
                observableEmitter.onNext(3);
                observableEmitter.onNext(4);
                observableEmitter.onNext(5);
                observableEmitter.onNext(6);
                observableEmitter.onComplete();
            }
        })
         .subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable disposable) {
                System.out.println("begin subscribe event");
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("onNext :"+integer);
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("onError :"+throwable.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        });
    }

    public static void demostation3(){
        /**
         *  .just 输入数据
         *  filter 进行过滤操作
         */
        Observable.just(1,2,3,4,5,6,7,8,9)
                .filter((value)->{
                    return value % 2 == 1;
                })
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        System.out.println("subscribe event");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println("onNext is :"+integer);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        System.out.println("Error");
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("Complete");
                    }
                });
    }

    public static void demostration4(){
        Observable.just(1,2,3,4,5,6,7)
                .filter((value) ->{
                    return value % 2==1;
                })
                .map((value) -> {  // 进行平方
                    return Math.sqrt(value);
                })
                .subscribe(new Observer<Double>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                        System.out.println("subscribe event");
                    }

                    @Override
                    public void onNext(Double aDouble) {
                        System.out.println("next value is :"+aDouble);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        System.out.println("Error");
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("Complete");
                    }
                });
    }

    public static void main(String[] args) {
        //demostration1();
        //demostration2();
        //demostation3();
        demostration4();
    }
}
