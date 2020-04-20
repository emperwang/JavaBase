package com.wk.demo.util;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class ProducerUtil {

    private static CommConfig config = CommConfig.Instance();

    private static Properties initlize(){
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getKafkaServer());
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.ACKS_CONFIG, config.getKafkaAckConfig());
        return properties;
    }

    public static Producer<String, String> getProducer(){
        Properties prop = initlize();
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(prop);
        return kafkaProducer;
    }

}
