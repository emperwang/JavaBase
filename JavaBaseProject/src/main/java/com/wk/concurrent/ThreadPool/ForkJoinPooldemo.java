package com.wk.concurrent.ThreadPool;


import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

public class ForkJoinPooldemo {
    static int[] nums = new int[1000000];
    static final int max_num = 50000;
    static Random r = new Random();

    static {
        for (int i = 0; i < nums.length; i++) {
            nums[i] = r.nextInt(100);
        }
        System.out.println(Arrays.stream(nums));
    }

    /*static class AddTask extends RecursiveAction{
        int start,end;

        AddTask(int a,int e){
            this.start = a;
            this.end = e;
        }

        @Override
        protected void compute() {
            if (end -start <= max_num){
                long sum = 0L;
                for (int i=start;i<=end;i++){
                    sum += nums[i];
                }
                System.out.println("from:"+start+",end:"+end+"="+sum);
            }else {
                int middle = start + (end-start)/2;
                AddTask addTask1 = new AddTask(start, middle);
                AddTask addTask = new AddTask(middle, end);
                addTask.fork();
                addTask1.fork();
            }
        }
    }*/
    static class AddTask extends RecursiveTask<Long> {
        int start, end;

        AddTask(int a, int e) {
            this.start = a;
            this.end = e;
        }

        @Override
        protected Long compute() {
            if (end - start <= max_num) {
                long sum = 0L;
                for (int i = start; i < end; i++) {
                    sum += nums[i];
                }
                return sum;
            }
            int middle = start + (end - start) / 2;
            AddTask addTask = new AddTask(start, middle);
            AddTask addTask1 = new AddTask(middle, end);
            addTask.fork();
            addTask1.fork();
            return addTask.join() + addTask1.join();
        }
    }
        public static void main(String[] args) throws IOException {
            ForkJoinPool joinPool = new ForkJoinPool();
            AddTask addTask = new AddTask(0, nums.length);
            joinPool.execute(addTask);
            //System.in.read();
            Long join = addTask.join();
            System.out.println("result is:"+join);
        }

    }
