package com.wk.kafka;

import com.wk.queue.MessageQueue;
import kafka.common.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.util.Map;

public class KafkaOffsetQueue {
    private static KafkaOffsetQueue INSTANCE = new KafkaOffsetQueue();
    private MessageQueue<Map<TopicPartition,OffsetAndMetadata>> queue;

    private KafkaOffsetQueue(){
        this.queue = MessageQueue.create();
    }

    public static KafkaOffsetQueue getInstance(){
        return INSTANCE;
    }

    public Map<TopicPartition,OffsetAndMetadata> offset(){
        Map<TopicPartition,OffsetAndMetadata> offset = null;
        Map<TopicPartition,OffsetAndMetadata> poll;

        while((poll = queue.poll())!=null){
            if(offset == null){
                offset = poll;
            }else{
                updateOffset(offset,poll);
            }
        }
        return offset;
    }

    private void updateOffset(Map<TopicPartition,OffsetAndMetadata> offset,
                              Map<TopicPartition,OffsetAndMetadata> newOffset){
        for (Map.Entry<TopicPartition, OffsetAndMetadata> entry : newOffset.entrySet()) {
            TopicPartition key = entry.getKey();
            if(offset.get(key) ==null ||
                    offset.get(key).offset() < entry.getValue().offset()){
                offset.put(key,entry.getValue());
            }
        }
    }

    public boolean offer(Map<TopicPartition,OffsetAndMetadata> offfset){
        return queue.offer(offfset);
    }

}
