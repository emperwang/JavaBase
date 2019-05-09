package com.wk.baseKstream;

import com.sun.xml.internal.ws.api.pipe.StreamSOAPCodec;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.ValueMapper;

import java.util.Arrays;
import java.util.Properties;
import java.util.stream.Stream;

public class LineSplit {
    public static void main(String[] args) {
        Properties prop = new Properties();
        prop.put(StreamsConfig.APPLICATION_ID_CONFIG,"app-line-split");
        prop.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,"192.168.30.10");
        prop.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        prop.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG,Serdes.String().getClass());

        KStreamBuilder builder = new KStreamBuilder();
        //从topic中读取数据
        KStream<String, String> source = builder.stream("stream-input");
        //把读入的记录分割成单词
        KStream<String, String> words = source.flatMapValues(new ValueMapper<String, Iterable<String>>() {
            @Override
            public Iterable<String> apply(String s) {
                return Arrays.asList(s.split(" "));
            }
        });
        //输入到另一个topic
        words.to("stream-output");
        //创建流
        KafkaStreams kafkaStreams = new KafkaStreams(builder, prop);

        //开始执行任务
        kafkaStreams.start();
        //停止    监听ctrl+c
        Runtime.getRuntime().addShutdownHook(new Thread("shutdown"){
            @Override
            public void run() {
                kafkaStreams.close();
            }
        });
    }
}
