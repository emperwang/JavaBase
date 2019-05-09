package com.wk.thread;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public abstract class CyclicTask implements Runnable,Cloneable {

    private int state = 0;
    private Throwable error;

    public String name;

    @Override
    public void run() {
        error = null;
        state++;
        beforeRunning();
        while (state > 0){
            try{
                doRun();
            }catch (Throwable t){
                dumpError(t);
                setError(t);
            }
        }
        stopRunning();
        afterRunning();
        try{
            close();
        }catch (IOException e){
            dumpError(e);
        }
    }

    protected void close() throws IOException{
        stopRunning();
    }

    protected void afterRunning(){

    }

    protected void clearError(){
        error = null;
    }

    protected final void stopRunning(){
        state = 0;
    }

    protected  void setError(Throwable t){
        this.error = t;
    }

    protected void dumpError(Throwable t){
        log.error(t.getMessage(),t);
    }


    public boolean hasError(){
        return error != null;
    }

    protected abstract void doRun();

    protected void beforeRunning(){

    }

    public String getName(){
        return name;
    }

    public Throwable getError() {
        return error;
    }

    protected String normalizeName(String n){
        return n +"-"+getClass().getSimpleName();
    }
}
