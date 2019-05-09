package com.wk.kafka;

import com.wk.queue.MessageQueue;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.concurrent.Future;

public class SimpleKafkaProducer<K,V> {
    private Producer<K,V> producer;
    private MessageQueue<ProducerRecord<K,V>> queue;

    public SimpleKafkaProducer(Properties properties){
        this.producer = new KafkaProducer<K, V>(properties);
    }

    public Future<RecordMetadata> send(String topic,K key,V value){
        return send(new ProducerRecord<>(topic,key,value));
    }

    public Future<RecordMetadata> send(ProducerRecord<K,V> record){
        return  this.producer.send(record);
    }

    public void close(){
        producer.close();
    }

    public static <KK,VV> SimpleKafkaProducer<KK,VV> create(Properties properties){
        return new SimpleKafkaProducer<>(properties);
    }

    public static  SimpleKafkaProducer<String,String> createStringProducer(Properties properties){
        return SimpleKafkaProducer.create(properties);
    }

}










