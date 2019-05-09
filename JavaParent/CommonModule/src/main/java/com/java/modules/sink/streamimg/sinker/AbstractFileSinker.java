package com.java.modules.sink.streamimg.sinker;

import com.java.modules.sink.Coverter;
import com.java.modules.sink.PipelineSinker;
import com.wk.collection.CollectionUtil;
import com.wk.data.DataWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public abstract class AbstractFileSinker<T> extends PipelineSinker<T> {
    protected static final AtomicInteger id = new AtomicInteger(0);
    protected final ReentrantLock lock;
    protected final SinkerConfig config;
    protected final String tempSuffix = ".tmp";
    protected Coverter<T> converter;
    protected String lastFilePath;
    protected DataWriter writer;
    protected Map<String,Object> metricMap;
    protected long cnt;
    protected long size;

    protected AbstractFileSinker(SinkerConfig config){
        this.lock = new ReentrantLock();
        this.metricMap = CollectionUtil.newLinkedHashMap();
        this.config = config;
        this.name = normalizeName(config.getName())+"-"+id.getAndIncrement();
        this.lastFilePath = StringUtils.EMPTY;
    }

    @Override
    protected void process() {
        byte[] bytes = converter.toBytes(data);
        write(bytes);
    }

    protected void write(byte[] bytes) {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try{
            writer.writeBytes(bytes);
            cnt += converter.getLineCount();
            size += converter.getSize();
        }finally {
            lock.unlock();
        }
    }

    public void switchSlot(){
        String path = converter.getPath(data);
        if(lastFilePath.equals(path)){
            return;
        }
        final ReentrantLock lock = this.lock;
        lock.lock();
        try{
            if(writer != null){
                closeWriter();
                putMetric(lastFilePath,cnt,size);
            }
        }finally {
            lock.unlock();
        }
    }

    /**
     * add metric data {"path":"xxx","count":100,"size":2048}
     *
     * @param path lastFilepath
     * @param c    file line count
     * @param s    file size
     */
    private void putMetric(String path, long c, long s) {
        Map<String, Object> m = CollectionUtil.newLinkedHashMap();
        m.put("path", path);
        m.put("count", c);
        m.put("size", s);
        metricMap.put(path, m);
        if (metricMap.size() > 10) {
            metricMap.clear();
            log.info("Clearing metricMap.");
        }
    }
    @Override
    protected void close() throws IOException {
        stopRunning();
        closeWriter();
    }

    protected void closeWriter(){
        try{
            writer.close();
            rename(lastFilePath,tempSuffix);
        }catch (IOException e){

        }finally {
            log.info("close:" + lastFilePath + ",line count:" + cnt + ",size:" + size);
        }
    }

    public void setConverter(Coverter<T> coverter){
        this.converter = coverter;
    }

    protected abstract void rename(String path,String suffix);
    protected abstract DataWriter createWriter(String path,String suffix);
}
