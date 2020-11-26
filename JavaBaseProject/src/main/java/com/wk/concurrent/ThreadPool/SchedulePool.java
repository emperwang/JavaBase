package com.wk.concurrent.ThreadPool;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.*;

//调度线程池
public class SchedulePool {
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public void demo1(){
        //创建核心线程是4的线程池
        ScheduledExecutorService service = Executors.newScheduledThreadPool(3);
        //添加任务到线程池，每500ms执行一次
        service.scheduleAtFixedRate(()->{
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName());
        },0,500,TimeUnit.MILLISECONDS);
    }
    // fixRate运行任务, 任务运行时间小于  fixRate时间
    public void demo2(){
        final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(10, new ThreadPoolExecutor.AbortPolicy());
        executor.scheduleAtFixedRate(()->{
            System.out.println("demo5 begin. Time: " + format.format(new Date()));
            sleep(4);
            System.out.println("demo5 end. Time: "+ format.format(new Date()));
        },2,5,TimeUnit.SECONDS);
        /*
        demo5 begin. Time: 2020-11-26 10:24:28
        into sleep
        demo5 end. Time: 2020-11-26 10:24:32
        demo5 begin. Time: 2020-11-26 10:24:33
        下次执行时间: 28+5 = 33
         */
    }
    // fixRate运行任务, 任务运行时间大于  fixRate时间
    public void demo3(){
        final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(10, new ThreadPoolExecutor.AbortPolicy());
        executor.scheduleAtFixedRate(()->{
            System.out.println("demo5 begin. Time: " + format.format(new Date()));
            sleep(8);
            System.out.println("demo5 end. Time: "+ format.format(new Date()));
        },2,5,TimeUnit.SECONDS);
        /*
        demo5 begin. Time: 2020-11-26 10:25:30
        into sleep
        demo5 end. Time: 2020-11-26 10:25:38
        demo5 begin. Time: 2020-11-26 10:25:38
        into sleep
        demo5 end. Time: 2020-11-26 10:25:46
        demo5 begin. Time: 2020-11-26 10:25:46
        下次开始时间: 30 + 5 = 35  因为休眠8s已经大于了真正的开始时间,即执行完成后 在38s处就直接开始了任务,即任务延迟了
         */
    }

    // fixDelay运行任务, 任务运行时间大于  fixDelay时间
    public void demo4(){
        final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(10, new ThreadPoolExecutor.AbortPolicy());
        executor.scheduleWithFixedDelay(()->{
            System.out.println("demo5 begin. Time: " + format.format(new Date()));
            sleep(11);
            System.out.println("demo5 end. Time: "+ format.format(new Date()));
        },2,5,TimeUnit.SECONDS);
        /*
        demo5 begin. Time: 2020-11-26 10:22:11
        into sleep
        demo5 end. Time: 2020-11-26 10:22:22
        demo5 begin. Time: 2020-11-26 10:22:27
        下次开始时间 = 11 + 11(休眠时间) + 5(延迟时间)  = 27
         */
    }

    // fixDelay运行任务, 任务运行时间小于  fixDelay时间
    public void demo5(){
        final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(10, new ThreadPoolExecutor.AbortPolicy());
        executor.scheduleWithFixedDelay(()->{
            System.out.println("demo5 begin. Time: " + format.format(new Date()));
            sleep(4);
            System.out.println("demo5 end. Time: "+ format.format(new Date()));
        },2,5,TimeUnit.SECONDS);
        /*
        demo5 begin. Time: 2020-11-26 10:19:52
        into sleep
        demo5 end. Time: 2020-11-26 10:19:56
        demo5 begin. Time: 2020-11-26 10:20:01
        下次开始时间 = 52 + 4(休眠时间) + 5(延迟时间)  = 20:01
         */
    }

    public void sleep(int sec){
        try{
            System.out.println("into sleep");
            TimeUnit.SECONDS.sleep(sec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        final SchedulePool pool = new SchedulePool();
        //pool.demo5();
        pool.demo3();
    }
}
