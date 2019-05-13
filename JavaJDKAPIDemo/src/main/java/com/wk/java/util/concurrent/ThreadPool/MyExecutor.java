package com.wk.java.util.concurrent.ThreadPool;

import java.util.concurrent.Executor;

public class MyExecutor implements Executor {
    @Override
    public void execute(Runnable command) {
        command.run();
    }

    public static void main(String[] args) {
        new MyExecutor().execute(()-> System.out.println("hello executor"));
    }
}
