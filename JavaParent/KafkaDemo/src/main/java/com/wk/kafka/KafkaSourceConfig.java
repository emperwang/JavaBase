package com.wk.kafka;

import com.wk.collection.CollectionUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.common.TopicPartition;

import java.util.Collection;
import java.util.Properties;

public class KafkaSourceConfig {
    /**
     *
     */
    private String name;
    /**
     *  设置多少个消费者进行消费
     */
    private int size =10;

    private Properties kafkaConfig;

    private Collection<String> topic;

    private Collection<String> partitions;

    private String offsetFile;

    private boolean commitOffset;

    private String separator;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Properties getKafkaConfig() {
        return kafkaConfig;
    }

    public void setKafkaConfig(Properties kafkaConfig) {
        this.kafkaConfig = kafkaConfig;
    }

    public Collection<String> getTopic() {
        return topic;
    }

    public void setTopic(Collection<String> topic) {
        this.topic = topic;
    }

    public Collection<TopicPartition> getPartitions() {
        if(partitions == null || partitions.isEmpty()){
            return null;
        }
        Collection<TopicPartition> tps = CollectionUtil.newArrayList(partitions.size());
        for (String str:partitions){
            int i = StringUtils.lastIndexOf(str, '-');
            tps.add(new TopicPartition(str.substring(0,i++),Integer.parseInt(str.substring(i))));
        }
        return tps;
    }

    public void setPartitions(Collection<String> partitions) {
        this.partitions = partitions;
    }

    public String getOffsetFile() {
        return offsetFile;
    }

    public void setOffsetFile(String offsetFile) {
        this.offsetFile = offsetFile;
    }

    public boolean isCommitOffset() {
        return commitOffset;
    }

    public void setCommitOffset(boolean commitOffset) {
        this.commitOffset = commitOffset;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }
}
