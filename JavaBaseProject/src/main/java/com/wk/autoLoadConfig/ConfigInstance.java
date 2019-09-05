package com.wk.autoLoadConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigInstance extends AbstractLoadConfig {
    private static final Logger log = LoggerFactory.getLogger(ConfigInstance.class);

    @Override
    public String getPropertiesFilePath() {
        // 从环境变量中获取配置文件路径
        String filePath = System.getProperty("config.path");
        log.info("configpath is :"+filePath);
        return filePath;
    }

    /**
     *  获取key对应的值
     * @return
     */
    public String getConfigValue(String key){
        String value = getStringValue(key);
        return value;
    }
}
