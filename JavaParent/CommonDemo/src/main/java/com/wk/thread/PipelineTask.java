package com.wk.thread;

import com.wk.queue.MessageQueue;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class PipelineTask<D> extends CyclicTask{
    private MessageQueue<D> cacheQueue = MessageQueue.create();
    protected D data;
    public boolean offer(D d){
        return cacheQueue.offer(d);
    }

    public D poll() throws InterruptedException {
        return cacheQueue.poll(10, TimeUnit.SECONDS);
    }

    public int cacheSize(){
        return cacheQueue.size();
    }

    @Override
    protected final void doRun() {
        try{
            data = poll();
            if(data != null){
                process();
                afterProcess();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            dumpError(e);
            stopRunning();
        }
    }

    protected void afterProcess(){

    }

    protected abstract void process();

    @Override
    protected void afterRunning() {
        log.info("cacheQueue.size="+cacheSize());
    }
}
