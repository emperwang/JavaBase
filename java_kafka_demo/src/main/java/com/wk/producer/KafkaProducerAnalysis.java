package com.wk.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.Properties;

/**
 *  发送端是线程安全的
 */
public class KafkaProducerAnalysis {
    private static String topic = "test";
    private static String brokers = "192.168.72.18:9092";

    public static Properties initConfig(){
        Properties prop = new Properties();
        prop.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        prop.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        prop.put(ProducerConfig.ACKS_CONFIG,0);
        prop.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,brokers);
        return prop;
    }

    public static void main(String[] args) {
        Properties prop = initConfig();
        KafkaProducer<String, String> producer = new KafkaProducer<>(prop);

    }
}
