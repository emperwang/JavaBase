package com.wk.baseKstream;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;

import java.util.Arrays;
import java.util.Locale;
import java.util.Properties;

public class WordCount {
    public static void main(String[] args) {
        Properties prop = new Properties();
        prop.put(StreamsConfig.APPLICATION_ID_CONFIG,"app-wordCount");
        prop.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,"192.168.30.10");
        prop.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        prop.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG,Serdes.String().getClass());

        KStreamBuilder builder = new KStreamBuilder();
        KStream<String, String> source = builder.stream("stream-input");
        //把读取的数据分割成单词
        KStream<String, String> words = source.flatMapValues(new ValueMapper<String, Iterable<String>>() {
            @Override
            public Iterable<String> apply(String s) {
                return Arrays.asList(s.toLowerCase(Locale.getDefault()).split(" "));
            }
        });
        KStream<String, Integer> values = words.mapValues(new ValueMapper<String, Integer>() {
            @Override
            public Integer apply(String s) {
                return 1;
            }
        });

        words.to("stream-output");
        KafkaStreams kafkaStreams = new KafkaStreams(builder, prop);
        kafkaStreams.start();
    }
}
