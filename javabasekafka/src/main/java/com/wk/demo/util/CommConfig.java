package com.wk.demo.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.net.URL;

@Slf4j
public class CommConfig extends AbstructConfigUtil {
    private static final String fileName = "common.properties";
    private static CommConfig config = new CommConfig();

    private CommConfig(){}

    @Override
    public String getPropertiesFilePath() {
        /*String path = Thread.currentThread().getContextClassLoader().getResource(fileName).getPath();
        String path1 = new File(path).getPath();
        log.info("get config path:{}",path1);*/
        String path = System.getProperty("config.path");
        return path;
    }

    @Override
    public String getPropertiesFileName() {
        return fileName;
    }

    private static final String serverKey = "bootstrap.server";
    public String getKafkaServer(){
        return getStringValue(serverKey, "192.168.30.10:9092");
    }

    private static final String ackKey = "kafka.ack";
    public String getKafkaAckConfig(){
        return getStringValue(ackKey, "0");
    }
    private static final String topicKey = "kafka.topic";
    public String getKafkaTopic(){
        return getStringValue(topicKey, "test");
    }

    public static CommConfig Instance(){
        return config;
    }
}
