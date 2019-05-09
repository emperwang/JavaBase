package com.wk.thread;

import java.io.Closeable;
import java.io.IOException;

public class CollectivePipeline<D> implements Closeable {

    protected  int size;
    private ThreadLocal<Integer> position;
    protected PipelineTask<D>[] pipelineTasks;

    protected CollectivePipeline(PipelineTask<D>[] pipeline){
        this.position = new ThreadLocal<>();
        if(pipeline == null){
            throw new NullPointerException("param pipelines is null");
        }
        if(pipeline.length <= 0){
            throw new RuntimeException("pipelines length can't be less than zero :"+pipeline.length);
        }
        this.size = pipeline.length;
        this.pipelineTasks = pipeline;
    }

    public boolean offer(D data){
        int p = getPosition();
        return pipelineTasks[p].offer(data);
    }

    public String name(){
        return getClass().getSimpleName();
    }

    private int getPosition(){
        Integer p = position.get();
        if(p != null){
            int p1 = p + 1;
            position.set((size>p1)?p1:0);
            return p;
        }
        position.set(0);
        return 0;
    }

    protected boolean filter(D data){
        return false;
    }

    public PipelineTask<D>[] tasks() {
        return pipelineTasks;
    }

    @Override
    public void close() throws IOException {

    }

    public static void startPipeline(PipelineTask<?> p1){
        ThreadUtil.runThread(p1,p1.getName());
    }
}
