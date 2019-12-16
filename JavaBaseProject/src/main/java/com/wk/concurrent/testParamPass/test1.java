package com.wk.concurrent.testParamPass;

import com.wk.concurrent.ThreadPoolUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *  发现一个问题: 在一个线程中使用线程池,然后线程池中任务调用外部线程的list,进行clear操作,没有生效
 *  此是验证一:
 *      1.在线程池中调用外部传递的list, 并进行clear操作. 结果: clear操作生效了.
 */
public class test1 {
    private static ExecutorService service = ThreadPoolUtil.fixedPool(2);
    private static List<String> lists = new ArrayList<>();
    public static void main(String[] args) throws InterruptedException {
        lists.add("first");
        lists.add("seconds");

        service.submit(new task(lists));
        service.shutdown();
        service.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);

        System.out.println("final param is :" + lists.toString());

    }

    static class task implements Runnable{
        List<String> lists;
        public task(List<String> lists) {
            this.lists = lists;
        }

        @Override
        public void run() {
            System.out.println("rev param is :" + lists.toString());
            lists.clear();
        }
    }
}
