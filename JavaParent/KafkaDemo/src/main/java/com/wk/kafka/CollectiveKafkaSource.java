package com.wk.kafka;

import com.wk.collection.CollectionUtil;
import com.wk.thread.ThreadUtil;

import java.util.List;

public class CollectiveKafkaSource<K,V> {
    private List<KafkaSource<K,V>> sources;
    private RecordsProcessor<K,V> processor;

    public CollectiveKafkaSource(KafkaSourceConfig config){
        this.sources = CollectionUtil.newArrayList(config.getSize());
        config.getKafkaConfig().remove("client.id");

        for(int i=0;i<config.getSize();i++){
            KafkaSource<K, V> kafkaSource = new KafkaSource<>(config);
            sources.add(kafkaSource);
        }
    }

    public void startWithProcess(RecordsProcessor<K,V> processor){
        this.processor = processor;
        for (KafkaSource<K, V> source : sources) {
            source.setProcessor(processor);
            ThreadUtil.runDefaultThread(source);
        }
    }

    public List<KafkaSource<K, V>> getSources() {
        return sources;
    }

    public static <KK,VV> CollectiveKafkaSource<KK,VV> createKafkaSource(KafkaSourceConfig config){
        return new CollectiveKafkaSource<>(config);
    }
}
