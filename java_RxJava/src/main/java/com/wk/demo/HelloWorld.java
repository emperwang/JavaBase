package com.wk.demo;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Flowable;

/**
 * @author: Sparks
 * @Date: 2021/7/12 11:15
 * @Description
 */
public class HelloWorld {

    public static void main(String[] args) {
        method2();
    }

    public static void method1(){
        Flowable<String> helloWorld = Flowable.just("hello world").map(t -> t + " , first");

        helloWorld.subscribe(System.out::println);
    }

    public static void method2(){

        Flowable.range(1,5)
                .map(v -> v*5)
                .filter(v -> v % 3 ==0)
                .subscribe(v -> System.out.println(v));
    }
}
