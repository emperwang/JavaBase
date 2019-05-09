package com.wk.demo.threadDemo;

import com.google.inject.*;

// 一些注入的高级demo   目前没有理解的特别清楚
public class ThreadScopeDemo {
    static class ThreadServiceScope implements Scope{
        static ThreadLocal<Object> threadLocal = new ThreadLocal<>();
        @Override
        public <T> Provider<T> scope(Key<T> key, Provider<T> unscoped) {
            return new Provider<T>() {
                @Override
                public T get() {
                    T instance = (T) threadLocal.get();
                    if(instance == null){
                        instance = unscoped.get();
                        threadLocal.set(instance);
                    }
                    return instance;
                }
            };
        }

        @Override
        public String toString() {
            return "scope.ThreadServiceScope";
        }
    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new Module() {
            @Override
            public void configure(Binder binder) {
                binder.bind(Service.class).to(WwwService.class).in(new ThreadServiceScope());
            }
        });

        for(int i=0;i<3;i++){
            new Thread("Thread-"+i){
                @Override
                public void run() {
                    for(int m=0;m<3;m++){
                        System.out.println(String.format("%s--%d:%d",getName(),m,injector.getInstance(Service.class).hashCode()));
                    }
                }
            }.start();
        }
    }
}
