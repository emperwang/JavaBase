package com.wk.producer;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

import java.util.List;
import java.util.Map;


/**
 *  自定义分区器实现
 */
public class DemoPartitioner implements Partitioner {

    // 使用key的hash来进行分区,如果key为null，则随机选择一个分区
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        List<PartitionInfo> partitionInfos = cluster.partitionsForTopic(topic);
        int numPartions = partitionInfos.size();
        if (key != null){
            int i = key.hashCode() % numPartions;
            return i;
        }else {
            int i = (int) Math.random() * numPartions;
            return i;
        }
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
