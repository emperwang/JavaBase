package com.wk.concurrent.testParamPass;

public class Test2 {
    public static void main(String[] args) {
        ThreadWithThreadPool threadPool = new ThreadWithThreadPool();
        threadPool.run();

        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
