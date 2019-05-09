package com.java.modules.sink;

import com.wk.thread.PipelineTask;

public abstract class PipelineSinker<T> extends PipelineTask<T> implements Sinker<T> {

    @Override
    public boolean drain(T t) {

        return offer(t);
    }
}
