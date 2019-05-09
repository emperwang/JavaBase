package com.wk.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.util.Map;

public interface RecordsProcessor<K,V> {

    Map<TopicPartition,OffsetAndMetadata> process(ConsumerRecords<K,V> records);
}
