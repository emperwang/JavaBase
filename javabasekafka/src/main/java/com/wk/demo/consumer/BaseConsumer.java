package com.wk.demo.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class BaseConsumer {
    public static final String brokerList = "192.168.72.18:9092";
    public static final String topic = "test";
    public static final String groupId = "group.demo";
    public static Boolean running = true;

    public static Properties initConfig(){
        Properties prop = new Properties();

        prop.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        prop.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());

        prop.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,brokerList);
        prop.put(ConsumerConfig.GROUP_ID_CONFIG,groupId);
        prop.put(ConsumerConfig.CLIENT_ID_CONFIG,"demo-client-id");

        prop.put(ConsumerConfig.INTERCEPTOR_CLASSES_CONFIG,"");
        return prop;
    }

    public static void main(String[] args) {
        Properties properties = initConfig();
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        // 订阅一个主题
        consumer.subscribe(Arrays.asList(topic));
        // 订阅topic的0分区
        consumer.assign(Arrays.asList(new TopicPartition(topic,0)));

        while (running){
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
            // 对获取到的records进行处理
        }
    }
}
