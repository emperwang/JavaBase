package com.java.modules.sink.streamimg.sinker;

import com.wk.data.DataWriter;
import com.wk.hdfs.hadoop.SimpleHdfsFileWriter;
import com.wk.hdfs.hadoop.SimpleHdfsSingleton;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimpleHdfsSinker<T> extends AbstractFileSinker<T> {
    private SimpleHdfsSingleton hdfs;

    public SimpleHdfsSinker(SinkerConfig config){
        super(config);
        this.hdfs = SimpleHdfsSingleton.hdfs();
    }

    @Override
    protected void rename(String path, String suffix) {
        hdfs.move(path+suffix,path);
    }

    @Override
    protected DataWriter createWriter(String path, String suffix) {
        return new SimpleHdfsFileWriter(path+suffix);
    }

    @Override
    protected void afterProcess() {
        clearError();
    }

    @Override
    protected void dumpError(Throwable t) {
        if(hasError()){
            log.error(t.getMessage());
        }else{
            super.dumpError(t);
        }
        hdfs.checkTgt();
    }
}
