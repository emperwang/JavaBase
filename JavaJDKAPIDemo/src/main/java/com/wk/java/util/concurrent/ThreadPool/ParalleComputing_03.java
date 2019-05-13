package com.wk.java.util.concurrent.ThreadPool;

import jdk.nashorn.internal.codegen.CompilerConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ParalleComputing_03 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();
        getPrime(1,200000);
        long end = System.currentTimeMillis();
        System.out.println(end - start);

        //创建线程池，把任务分解进行计算，这样能加快计算速度
        final int cpuCoreNum = 4;
        ExecutorService service = Executors.newFixedThreadPool(cpuCoreNum);
        MyTask myTask1 = new MyTask(1, 80000);
        MyTask myTask2 = new MyTask(80001, 130000);
        MyTask myTask3 = new MyTask(130001, 170000);
        MyTask myTask4 = new MyTask(170001, 200000);
        Future<List<Integer>> f1 = service.submit(myTask1);
        Future<List<Integer>> f2 = service.submit(myTask2);
        Future<List<Integer>> f3 = service.submit(myTask3);
        Future<List<Integer>> f4 = service.submit(myTask4);
        start = System.currentTimeMillis();
        f1.get();
        f2.get();
        f3.get();
        f4.get();
        end = System.currentTimeMillis();
        System.out.println(end - start);
    }
    //创建一个任务，判断一个区间的数有多少是偶数
    static class MyTask implements Callable<List<Integer>> {
        int startPos,endPos;
        MyTask(int s,int e){
            this.startPos = s;
            this.endPos = e;
        }
        @Override
        public List<Integer> call() throws Exception {
            List<Integer> result = getPrime(startPos,endPos);
            return result;
        }
    }
    //判断逻辑
    private static boolean isPrime(int num){
        for (int i=2;i<=num/2;i++){
            if (num % i ==0) return false;
        }
        return true;
    }
    //获取一个区间的结果
    private static List<Integer> getPrime(int start, int end) {
        List<Integer> result = new ArrayList<>();
        for (int i= start;i<=end;i++) {
            if (isPrime(i)){
                result.add(i);
            }
        }
        return result;

    }
}
