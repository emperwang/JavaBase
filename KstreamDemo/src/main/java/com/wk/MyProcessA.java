package com.wk;

import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;

public class MyProcessA implements Processor<String,String> {

    private ProcessorContext context;

    /**
     *  init函数做初始化
     * @param processorContext  上下文
     */
    @Override
    public void init(ProcessorContext processorContext) {
        this.context = processorContext;
        this.context.schedule(1000);
    }

    /**
     *  接收到key/value parir ,对value做处理,最后在里面做forward
     * @param key  消息的key
     * @param value  消息的value
     */
    @Override
    public void process(String key, String value) {
        System.out.println("key == "+key);
        System.out.println("value == "+value);
        String line = value + "MyProcessor A ---- ";
        System.out.println("line == "+line);
        //将处理完成的数据转发到downstream processor
        //比如当前是processor1处理器
        //通过forward流向导processor2处理器
        context.forward(key,line);
    }

    @Override
    public void punctuate(long l) {

    }

    @Override
    public void close() {

    }
}
