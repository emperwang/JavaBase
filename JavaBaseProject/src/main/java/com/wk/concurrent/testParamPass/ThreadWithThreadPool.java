package com.wk.concurrent.testParamPass;

import com.wk.concurrent.ThreadPoolUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *  发现一个问题: 在一个线程中使用线程池,然后线程池中任务调用外部线程的list,进行clear操作,没有生效
 *  此是验证二:
 *      1.在线程池中调用外部线程传递的list, 并进行clear操作. 结果: 生效
 */
public class ThreadWithThreadPool extends Thread{
    private static ExecutorService service = ThreadPoolUtil.fixedPool(2);
    private static List<String> lists = new ArrayList<>();

    @Override
    public void run() {
        lists.add("first");
        lists.add("seconds");

        service.submit(new task(lists));
        service.shutdown();
        try {
            service.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("thread param is :" + lists.toString());
    }

    static class task implements Runnable{
        List<String> lists;
        public task(List<String> lists) {
            this.lists = lists;
        }

        @Override
        public void run() {
            System.out.println("task param is :" + lists.toString());
            lists.clear();
        }
    }

}
