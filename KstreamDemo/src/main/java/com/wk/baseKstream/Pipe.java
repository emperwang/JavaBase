package com.wk.baseKstream;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.processor.internals.ProcessorTopology;

import java.util.Properties;

public class Pipe {
    public static void main(String[] args) {
        //配置基本的config
        Properties properties = new Properties();
        //设置一个应用的id
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG,"stream-pipe");
        //设置broker的地址
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,"192.168.30.10");
        //设置key的序列化方式
        properties.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        //反序列化方式
        properties.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG,Serdes.String().getClass());

        KStreamBuilder builder = new KStreamBuilder();
        //从topic--stream-input中读出数据，不经过任何处理，放入stream-output--topic
        builder.stream("stream-input").to("stream-output");
        KafkaStreams kafkaStreams = new KafkaStreams(builder, properties);

        Runtime.getRuntime().addShutdownHook(new Thread("stream-shutdown"){
            @Override
            public void run() {
                kafkaStreams.close();
            }
        });

        kafkaStreams.start();
    }
}
