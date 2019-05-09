package com.java.modules.sink.streamimg.sinker;

import java.util.Properties;

public class SinkerConfig {
    /**
     * Thread Group Name
     */
    private String name;
    /**
     * cached queue size
     */
    private int queueCapacity = 100;
    /**
     * max wait time before sending(milsec)
     */
    private int waitTime;
    /**
     * the number of parallel sinker
     */
    private int size = 10;
    /**
     * kafka configuration
     */
    private Properties kafkaConfig;

    private String encoding;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQueueCapacity() {
        return queueCapacity;
    }

    public void setQueueCapacity(int queueCapacity) {
        this.queueCapacity = queueCapacity;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
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

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
}
