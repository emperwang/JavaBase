package com.wk.kafka;

import com.google.gson.reflect.TypeToken;
import com.wk.collection.CollectionUtil;
import com.wk.file.FileUtil;
import com.wk.json.JsonUtil;
import com.wk.thread.CyclicTask;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.MetricName;
import org.apache.kafka.common.TopicPartition;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class KafkaSource<K,V> extends CyclicTask{
    protected static AtomicInteger id = new AtomicInteger(0);
    private final KafkaSourceConfig config;
    private SimpleKafkaConsumer<K,V> consumer;
    private final Collection<String> topic;
    private final Collection<TopicPartition> partitions;
    private RecordsProcessor<K,V> processor;
    private Map<String,Object> metrics;
    private final ReentrantLock lock;

    public KafkaSource(KafkaSourceConfig config){
        this.lock = new ReentrantLock();
        this.config = config;
        this.name = normalizeName(config.getName()+"-"+id.getAndIncrement());
        this.consumer = SimpleKafkaConsumer.createSimpleKafkaConsumer(config.getKafkaConfig());
        this.topic = config.getTopic();
        this.partitions = config.getPartitions();
        if(topic != null && !topic.isEmpty()){
            consumer.subscribe(topic);
        }else if(partitions !=null && !partitions.isEmpty()){
            consumer.assign(partitions);
        }else{
            throw new RuntimeException("no topic or partition specifie: " +
                    "topic(" + topic + ") partitions(" + partitions + ")");
        }


    }

    private void seefOffset(){
        if(StringUtils.isEmpty(config.getOffsetFile())){
            return;
        }
        if(FileUtil.exists(config.getOffsetFile())){
            return;
        }

        String json = FileUtil.readString(config.getOffsetFile());
        Map<TopicPartition,Long> offsets = JsonUtil.json2Bean(json,new TypeToken<Map<TopicPartition,Long>>(){}.getType());
        if(offsets != null && !offsets.isEmpty()){
            for(Map.Entry<TopicPartition,Long> entry:offsets.entrySet()){
                consumer.seek(entry.getKey(),entry.getValue());
            }
        }
    }

    @Override
    protected void doRun() {
        ConsumerRecords<K, V> records = consumer.poll();
        if(!records.isEmpty()){
            Map<TopicPartition, OffsetAndMetadata> offsetAndMetadataMap =
                    processor.process(records);
            if(offsetAndMetadataMap != null){
                consumer.commitSync(offsetAndMetadataMap);

            }
        }
    }

    private void putMetrics(ConsumerRecords<K,V> records){
        Set<TopicPartition> partitions = records.partitions();
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            for (TopicPartition partition : partitions) {
                metrics.putIfAbsent(partition.topic(), 0L);
            }
            for (TopicPartition partition : partitions) {
                Long i = (Long) metrics.get(partition.topic()) + records.records(partition).size();
                metrics.put(partition.topic(), 1);
            }
        }finally {
            lock.unlock();
        }
    }

    public Object metrics() {
        Map<String, Object> ms = metrics;
        ReentrantLock lock = this.lock;
        lock.lock();
        try {
            metrics = CollectionUtil.newLinkedHashMap();
        } finally {
            lock.unlock();
        }
        Map<String, Object> map = CollectionUtil.newLinkedHashMap();
        map.put("name", name);
        map.put("metrics", ms);
        map.put("consumer", getConsumerMetrics());
        return map;
    }

    private Object getConsumerMetrics(){
        Map<MetricName, Object> map = CollectionUtil.newLinkedHashMap();
        consumer.metrics().forEach((mn,o)->{
            List<Object> ms = CollectionUtil.newArrayList(2);
            ms.add(o.metricName());
            ms.add(String.valueOf(o.value()));
            map.put(mn,ms);
        });
        return map;
    }

    public void setProcessor(RecordsProcessor<K,V> processor){
        this.processor = processor;
    }
}
