package com.wk.queue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class MessageQueue<T> {
    private  QueueConfig queueConfig;
    private BlockingQueue<T> queue;

    private MessageQueue(QueueConfig config){
        this.queueConfig = config;
        this.queue = new ArrayBlockingQueue<>(this.queueConfig.getCapacity());
    }

    private MessageQueue(){
        this.queueConfig = null;
        this.queue = new LinkedBlockingQueue<>();
    }

    /**
     * 在队列尾部插入数据,如果不超过容量 返回true
     *  IllegalStateException  表示队列满
     * @param t  要插入的数据
     * @return
     */
    public Boolean add(T t){
        return queue.add(t);
    }

    /**
     *  立即将元素插入队列的尾部,成功返回true  如果队列满 返回false
     * @param t  要插入的数据
     * @return
     */
    public Boolean offer(T t){
        return queue.offer(t);
    }

    /**
     *  在队列为尾部插入指定的元素, 如果队列满,则等待空间变为可用
     * @param t
     * @throws InterruptedException
     */
    public void put(T t) throws InterruptedException {
        queue.put(t);
    }

    /**
     * 从对队列头取出，但不删除该数, 队列为空则返回null
     * @return
     */
    public T peek(){
        T peek = queue.peek();
        return peek;
    }


    /**
     * 在队列头部取数,取出的数会被队列删除, 如果没有元素 等待元素可用
     * @return
     * @throws InterruptedException
     */
    public T take() throws InterruptedException {
        return queue.take();
    }

    /**
     * 从队列头部取数,取出后此数被删除,  如果队列为空,返回null
     * @return
     */
    public T poll(){
        return queue.poll();
    }

    /**
     * 从队列头部取数,取出后该数会被删除,如果有必要,则等待空间变为可用
     * @param timeout
     * @param unit
     * @return
     * @throws InterruptedException
     */
    public T poll(long timeout, TimeUnit unit) throws InterruptedException {
        return  queue.poll(timeout,unit);
    }

    public int size(){
        return queue.size();
    }

    public static <E> MessageQueue<E> create(QueueConfig config){
        return new MessageQueue<>(config);
    }

    public static <E> MessageQueue<E> create(){
        return new MessageQueue<>();
    }
}

