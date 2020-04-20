package com.wk.demo.producer;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 *  发送端是线程安全的
 */
public class KafkaProducerAnalysis {
    private static String topic = "test";
    private static String brokers = "192.168.72.18:9092";

    public static Properties initConfig(){
        Properties prop = new Properties();
        //prop.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        //prop.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        prop.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        prop.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());
        prop.put(ProducerConfig.ACKS_CONFIG,0);
        prop.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,brokers);
        // 设置分区器
        prop.put(ProducerConfig.PARTITIONER_CLASS_CONFIG,DemoPartitioner.class.getName());
        // 设置拦截器
        prop.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG,DemoProducerInterceptor.class.getName());
        return prop;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Properties prop = initConfig();
        KafkaProducer<String, String> producer = new KafkaProducer<>(prop);
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, "key", "value");
        // 同步发送消息
        Future<RecordMetadata> future = producer.send(record);
        RecordMetadata recordMetadata = future.get();
        recordMetadata.offset();  // 获取偏移量

        // 有回调函数的发送
        producer.send(record, new Callback() {
            @Override
            public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                // 发送完后的回调函数
            }
        });
    }
}
