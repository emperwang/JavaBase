package com.wk;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.processor.TopologyBuilder;

import java.util.Properties;

public class MainStarter {
    public static void main(String[] args) {
        Properties properties = new Properties();
        //应用ID
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG,"streams-demo");
        //kafka 集群
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,"192.168.72.18:9092");
        //key的序列化/  反序列化
        properties.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        //value的序列化/  反序列化
        properties.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG,Serdes.String().getClass().getName());
        //指定一个路径创建应用ID所属的文件
        properties.put(StreamsConfig.STATE_DIR_CONFIG, "F:\\test");
        StreamsConfig config = new StreamsConfig(properties);

        TopologyBuilder builder = new TopologyBuilder();
        //添加一个source,第一个参数是名称   param2 是从哪一个topic读取消息
        builder.addSource("source","mc_mme_combine")
                //添加一个process,param1是定义process名称,param2实现类,param3指定一个父名称
                .addProcessor("process1",MyProcessA::new,"source")
               //添加sink位置,param1添加sink名称,param2指定一个输出topic,param3指定接收哪一个process数据
                .addSink("sink","paritioner3","process1");

        KafkaStreams streams = new KafkaStreams(builder, config);
        streams.start();
    }
}








