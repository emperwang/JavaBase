package com.wk.interpolation.test;


import java.util.Properties;

/**
 * Created by Michael Chyson on 1/4/2018.
 */
public class Config {
    private static final String DEFAULT_CONFIG_FILE = "smart-sms.json";
    private String name;
    private Properties kafka;
    private FileSinkerConfig hdfsSinkerConfig;

    public Properties getKafka() {
        return kafka;
    }

    public void setKafka(Properties kafka) {
        this.kafka = kafka;
    }

    public FileSinkerConfig getHdfsSinkerConfig() {
        return hdfsSinkerConfig;
    }

    public void setHdfsSinkerConfig(FileSinkerConfig hdfsSinkerConfig) {
        this.hdfsSinkerConfig = hdfsSinkerConfig;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
