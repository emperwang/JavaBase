package com.wk.concurrent.ThreadPool;

import java.util.concurrent.*;

public class Future_02 {
    public static void main(String[] args) throws Exception {
        FutureTask<Integer> task = new FutureTask<>(() -> {
            TimeUnit.SECONDS.sleep(3);
            return 1000;
        });
        new Thread(task).start();
        System.out.println(task.get());
//********************************************

        ExecutorService service = Executors.newFixedThreadPool(5);
        Future<Object> f = service.submit(new Callable() {
            @Override
            public Object call() throws Exception {
                TimeUnit.SECONDS.sleep(1);
                return 1;
            }
        });
        System.out.println(f.get());
        System.out.println(f.isDone());
    }
}
