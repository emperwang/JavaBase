package com.wk.java.util.concurrent.container;

import java.util.concurrent.LinkedTransferQueue;

public class TransferQueue01 {
    public static void main(String[] args) throws InterruptedException {
        LinkedTransferQueue<String> transferQueue = new LinkedTransferQueue<>();
        new Thread(()->{
            try {
                System.out.println(transferQueue.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        transferQueue.transfer("ccc");  //如果由线程再等待消费，此api是把数据发送过去
        /*transferQueue.put("aaa");
        new Thread(()->{
            try {
                System.out.println(transferQueue.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();*/
    }
}
