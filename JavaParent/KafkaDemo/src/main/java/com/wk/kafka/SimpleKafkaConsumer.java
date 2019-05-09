package com.wk.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.apache.kafka.common.TopicPartition;

import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class SimpleKafkaConsumer<K,V> {
    /**
     * consumer poll time out 500ms
     */
    private long timeOut = 500L;
    private KafkaConsumer<K,V> consumer;

    public SimpleKafkaConsumer(Properties properties){
        this.consumer = new KafkaConsumer<K,V>(properties);
    }

    public void subscribe(Collection<String> topics){
        consumer.subscribe(topics);
    }

    public void assign(Collection<TopicPartition> partitions){
        this.consumer.assign(partitions);
    }

    public Set<TopicPartition> assignment(){
        Set<TopicPartition> assignment = this.consumer.assignment();
        return assignment;
    }

    public ConsumerRecords<K,V> poll(){
        return this.consumer.poll(timeOut);
    }

    public void commitSync(Map<TopicPartition,OffsetAndMetadata> offsetAndMetadataMap){
        consumer.commitSync(offsetAndMetadataMap);
    }

    private void close(){
        consumer.close();
    }

    public void seek(TopicPartition partition,long offset){
        consumer.seek(partition,offset);
    }

    public Map<MetricName,? extends Metric> metrics(){
        return consumer.metrics();
    }

    public static <KK,VV> SimpleKafkaConsumer<KK,VV> createSimpleKafkaConsumer(Properties kafkaConfig){
        return new SimpleKafkaConsumer<>(kafkaConfig);
    }

}
